package com.levelupgamer.gamificacion;

import com.levelupgamer.gamificacion.dto.*;
import com.levelupgamer.gamificacion.cupones.Cupon;
import com.levelupgamer.gamificacion.cupones.CuponService;
import com.levelupgamer.gamificacion.cupones.dto.CuponDTO;
import com.levelupgamer.usuarios.Usuario;
import com.levelupgamer.usuarios.UsuarioRepository;
import java.util.*;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PuntosService {
    
    // Definición de niveles
    private static final Map<String, NivelInfo> NIVELES = Map.of(
        "Bronce", new NivelInfo(0, 999, "#CD7F32", 
            Arrays.asList("Acceso a ofertas especiales", "Puntos por cada compra")),
        "Plata", new NivelInfo(1000, 4999, "#C0C0C0",
            Arrays.asList("5% de descuento adicional", "Acceso anticipado a nuevos productos", "Puntos dobles en eventos")),
        "Oro", new NivelInfo(5000, 14999, "#FFD700",
            Arrays.asList("10% de descuento adicional", "Envío gratis en todas las compras", "Soporte prioritario", "Acceso a productos exclusivos")),
        "Platino", new NivelInfo(15000, Integer.MAX_VALUE, "#E5E4E2",
            Arrays.asList("15% de descuento adicional", "Envío express gratis", "Soporte 24/7", "Productos exclusivos y ediciones limitadas", "Invitaciones a eventos privados"))
    );
    
    private static class NivelInfo {
        final int minimo;
        final int maximo;
        final String color;
        final List<String> beneficios;
        
        NivelInfo(int minimo, int maximo, String color, List<String> beneficios) {
            this.minimo = minimo;
            this.maximo = maximo;
            this.color = color;
            this.beneficios = beneficios;
        }
    }
    
    private final PuntosRepository puntosRepository;
    private final UsuarioRepository usuarioRepository;
    private final MovimientoPuntosRepository movimientoPuntosRepository;
    private final CuponService cuponService;

    public PuntosService(PuntosRepository puntosRepository,
                         UsuarioRepository usuarioRepository,
                         MovimientoPuntosRepository movimientoPuntosRepository,
                         CuponService cuponService) {
        this.puntosRepository = puntosRepository;
        this.usuarioRepository = usuarioRepository;
        this.movimientoPuntosRepository = movimientoPuntosRepository;
        this.cuponService = cuponService;
    }

    @Transactional(readOnly = true)
    public PuntosDTO obtenerPuntosPorUsuario(Long usuarioId) {
        Long safeUsuarioId = Objects.requireNonNull(usuarioId, "usuarioId no puede ser nulo");
        return puntosRepository.findByUsuarioId(safeUsuarioId)
                .map(p -> new PuntosDTO(p.getUsuarioId(), p.getPuntosAcumulados()))
                .orElse(new PuntosDTO(safeUsuarioId, 0));
    }

    @Transactional
    public PuntosDTO sumarPuntos(PuntosDTO dto) {
        Long usuarioId = Objects.requireNonNull(dto.getUsuarioId(), "usuarioId es requerido");
        Integer puntosASumar = Objects.requireNonNull(dto.getPuntosAcumulados(), "puntosAcumulados es requerido");

        Puntos puntos = puntosRepository.findByUsuarioId(usuarioId)
                .orElseGet(() -> {
                    
                    Usuario usuario = usuarioRepository.findById(usuarioId)
                            .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado para asignar puntos"));
                    Puntos nuevo = new Puntos();
                    nuevo.setUsuario(usuario);
                    
                    nuevo.setPuntosAcumulados(0);
                    return nuevo;
                });

        puntos.setPuntosAcumulados(puntos.getPuntosAcumulados() + puntosASumar);
        Puntos savedPuntos = puntosRepository.save(puntos);
        registrarMovimiento(savedPuntos, puntosASumar, TipoMovimientoPuntos.GANANCIA,
                "Suma directa de puntos");

        return new PuntosDTO(savedPuntos.getUsuarioId(), savedPuntos.getPuntosAcumulados());
    }

    @Transactional
    public PuntosDTO canjearPuntos(PuntosDTO dto) {
        Long usuarioId = Objects.requireNonNull(dto.getUsuarioId(), "usuarioId es requerido");
        Integer puntosACanjear = Objects.requireNonNull(dto.getPuntosAcumulados(), "puntosAcumulados es requerido");

        Puntos puntos = puntosRepository.findByUsuarioId(usuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario sin puntos para canjear"));
        
        if (puntos.getPuntosAcumulados() < puntosACanjear) {
            throw new IllegalArgumentException("No tiene suficientes puntos para canjear");
        }
        
        puntos.setPuntosAcumulados(puntos.getPuntosAcumulados() - puntosACanjear);
        Puntos saved = puntosRepository.save(puntos);
        registrarMovimiento(saved, puntosACanjear, TipoMovimientoPuntos.CANJE,
                "Canje manual de puntos");

        return new PuntosDTO(saved.getUsuarioId(), saved.getPuntosAcumulados());
    }

    @Transactional
    public PuntosDTO restarPuntosPorAjuste(Long usuarioId, Integer puntosAjustados, String descripcion) {
        Long safeUsuarioId = Objects.requireNonNull(usuarioId, "usuarioId es requerido");
        Integer safePuntos = Objects.requireNonNull(puntosAjustados, "puntosAjustados es requerido");

        Puntos puntos = puntosRepository.findByUsuarioId(safeUsuarioId)
                .orElseThrow(() -> new IllegalArgumentException("Usuario sin puntos registrados"));

        if (puntos.getPuntosAcumulados() < safePuntos) {
            throw new IllegalArgumentException("No tiene suficientes puntos para revertir la operación solicitada");
        }

        puntos.setPuntosAcumulados(puntos.getPuntosAcumulados() - safePuntos);
        Puntos saved = puntosRepository.save(puntos);
        registrarMovimiento(saved, safePuntos, TipoMovimientoPuntos.CANJE,
                descripcion != null ? descripcion : "Ajuste de puntos");

        return new PuntosDTO(saved.getUsuarioId(), saved.getPuntosAcumulados());
    }

    @SuppressWarnings("null")
    private void registrarMovimiento(Puntos puntos, Integer cantidad, TipoMovimientoPuntos tipo, String descripcion) {
        MovimientoPuntos movimiento = MovimientoPuntos.builder()
                .puntos(puntos)
                .puntosAfectados(cantidad)
                .tipo(tipo)
                .descripcion(descripcion)
                .build();
        MovimientoPuntos persisted = movimientoPuntosRepository.save(movimiento);
        puntos.agregarMovimiento(persisted);
    }

    /**
     * Obtiene el nivel actual del usuario basado en sus puntos
     */
    @Transactional(readOnly = true)
    public NivelUsuarioDTO obtenerNivelUsuario(Long usuarioId) {
        Objects.requireNonNull(usuarioId, "usuarioId no puede ser nulo");
        PuntosDTO puntosDTO = obtenerPuntosPorUsuario(usuarioId);
        int puntos = puntosDTO.getPuntosAcumulados();
        
        String nivelActual = determinarNivel(puntos);
        NivelInfo nivelInfo = NIVELES.get(nivelActual);
        String siguienteNivel = obtenerSiguienteNivel(nivelActual);
        NivelInfo siguienteInfo = siguienteNivel != null ? NIVELES.get(siguienteNivel) : null;
        
        int puntosParaSiguiente = siguienteInfo != null ? siguienteInfo.minimo - puntos : 0;
        double progreso = siguienteInfo != null 
            ? Math.min(100.0, Math.max(0.0, ((double)(puntos - nivelInfo.minimo) / (siguienteInfo.minimo - nivelInfo.minimo)) * 100))
            : 100.0;
        
        return NivelUsuarioDTO.builder()
            .nombreNivel(nivelActual)
            .puntosActuales(puntos)
            .puntosMinimos(nivelInfo.minimo)
            .puntosMaximos(nivelInfo.maximo == Integer.MAX_VALUE ? null : nivelInfo.maximo)
            .color(nivelInfo.color)
            .descripcion(obtenerDescripcionNivel(nivelActual))
            .siguienteNivel(siguienteNivel)
            .puntosParaSiguienteNivel(puntosParaSiguiente > 0 ? puntosParaSiguiente : null)
            .progresoPorcentaje(progreso)
            .beneficios(nivelInfo.beneficios)
            .build();
    }
    
    private String determinarNivel(int puntos) {
        if (puntos >= 15000) return "Platino";
        if (puntos >= 5000) return "Oro";
        if (puntos >= 1000) return "Plata";
        return "Bronce";
    }
    
    private String obtenerSiguienteNivel(String nivelActual) {
        return switch (nivelActual) {
            case "Bronce" -> "Plata";
            case "Plata" -> "Oro";
            case "Oro" -> "Platino";
            default -> null;
        };
    }
    
    private String obtenerDescripcionNivel(String nivel) {
        return switch (nivel) {
            case "Bronce" -> "Nivel inicial - ¡Bienvenido a Level-Up Gamer!";
            case "Plata" -> "Gamer activo - ¡Sigue así!";
            case "Oro" -> "Gamer VIP - ¡Eres parte de la élite!";
            case "Platino" -> "Leyenda Gamer - ¡El nivel más alto!";
            default -> "";
        };
    }
    
    /**
     * Obtiene la lista de items canjeables disponibles
     */
    @Transactional(readOnly = true)
    public List<ItemCanjeableDTO> obtenerItemsCanjeables() {
        List<ItemCanjeableDTO> items = new ArrayList<>();
        
        // Cupones de descuento
        items.add(ItemCanjeableDTO.builder()
            .id(1L)
            .nombre("Cupón 10% Descuento")
            .descripcion("Descuento del 10% en tu próxima compra")
            .puntosRequeridos(500)
            .tipo("descuento")
            .valor(10)
            .build());
            
        items.add(ItemCanjeableDTO.builder()
            .id(2L)
            .nombre("Cupón 20% Descuento")
            .descripcion("Descuento del 20% en tu próxima compra")
            .puntosRequeridos(1000)
            .tipo("descuento")
            .valor(20)
            .build());
            
        items.add(ItemCanjeableDTO.builder()
            .id(3L)
            .nombre("Envío Express Gratis")
            .descripcion("Envío express gratis en tu próxima compra")
            .puntosRequeridos(300)
            .tipo("envio")
            .build());
            
        items.add(ItemCanjeableDTO.builder()
            .id(4L)
            .nombre("Mousepad Gamer")
            .descripcion("Mousepad gamer personalizado Level-Up")
            .puntosRequeridos(800)
            .tipo("producto")
            .valor(0)
            .build());
            
        items.add(ItemCanjeableDTO.builder()
            .id(5L)
            .nombre("Polera Level-Up")
            .descripcion("Polera exclusiva Level-Up Gamer")
            .puntosRequeridos(1200)
            .tipo("producto")
            .valor(0)
            .build());
            
        items.add(ItemCanjeableDTO.builder()
            .id(6L)
            .nombre("Cupón 30% Descuento")
            .descripcion("Descuento del 30% en tu próxima compra")
            .puntosRequeridos(2000)
            .tipo("descuento")
            .valor(30)
            .build());
        
        return items;
    }
    
    /**
     * Canjea puntos por un item específico
     */
    @Transactional
    public Cupon canjearItem(CanjeRequestDTO request) {
        Objects.requireNonNull(request.getUsuarioId(), "usuarioId es requerido");
        Objects.requireNonNull(request.getPuntosRequeridos(), "puntosRequeridos es requerido");
        
        // Validar que el usuario tenga suficientes puntos
        PuntosDTO puntosDTO = obtenerPuntosPorUsuario(request.getUsuarioId());
        if (puntosDTO.getPuntosAcumulados() < request.getPuntosRequeridos()) {
            throw new IllegalArgumentException("No tienes suficientes puntos. Necesitas " + 
                request.getPuntosRequeridos() + " puntos.");
        }
        
        // Si es un cupón de descuento, usar CuponService que maneja la conversión
        if ("descuento".equals(request.getTipoItem()) && request.getValor() != null) {
            // El CuponService tiene una tabla de conversión fija (500->5%, 1000->10%, etc.)
            // Verificamos si los puntos coinciden con la tabla
            Map<Integer, Integer> conversion = cuponService.obtenerTablaConversion();
            if (conversion.containsKey(request.getPuntosRequeridos())) {
                // Usar CuponService que maneja el canje completo (descuenta puntos y crea cupón)
                CuponDTO cuponDTO = cuponService.canjearPorCupon(
                    new com.levelupgamer.gamificacion.cupones.dto.RedeemCouponRequest(
                        request.getUsuarioId(),
                        request.getPuntosRequeridos()
                    )
                );
                // Buscar el cupón creado para retornarlo como entidad
                return cuponService.buscarCuponValido(
                    request.getUsuarioId(), 
                    null, 
                    cuponDTO.getCodigo()
                ).orElse(null);
            } else {
                // Si no coincide con la tabla de conversión, solo descontar puntos
                // (no se crea cupón automático, requeriría extender CuponService)
                canjearPuntos(new PuntosDTO(request.getUsuarioId(), request.getPuntosRequeridos()));
                return null;
            }
        }
        
        // Para otros tipos (producto, envio), descontar puntos
        canjearPuntos(new PuntosDTO(request.getUsuarioId(), request.getPuntosRequeridos()));
        // El producto o beneficio se entregaría manualmente o mediante otro sistema
        return null;
    }
}
