package com.levelupgamer.productos;

import com.levelupgamer.productos.dto.ProductoDTO;
import com.levelupgamer.productos.dto.ProductoRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.io.IOException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/products")
@Tag(name = "Catálogo de Productos", description = "Gestión del catálogo de productos gaming. Los vendedores solo pueden gestionar sus propios productos.")
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Operation(summary = "Obtener todos los productos disponibles",
            description = "Retorna el catálogo completo para administradores y clientes. Los vendedores solo verán sus propios productos.")
    @GetMapping
    public ResponseEntity<List<ProductoDTO>> obtenerCatalogo() {
        List<ProductoDTO> productos = productoService.listarProductos();
        return ResponseEntity.ok(productos);
    }

    @Operation(summary = "Obtener productos destacados",
            description = "Retorna los productos con mayor puntuación LevelUp para mostrar en la página principal")
    @GetMapping("/featured")
    public ResponseEntity<List<ProductoDTO>> obtenerDestacados() {
        List<ProductoDTO> destacados = productoService.listarDestacados();
        return ResponseEntity.ok(destacados);
    }

    @Operation(summary = "Obtener detalles de un producto",
            description = "Retorna la información completa de un producto por su ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductoDTO> obtenerDetalleProducto(@PathVariable Long id) {
        return productoService.buscarPorId(id)
                .map(ProductoMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Agregar nuevo producto al catálogo",
            description = "Permite crear un nuevo producto. El producto se asocia automáticamente al usuario autenticado. Requiere rol de administrador o vendedor.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    @PostMapping(consumes = { MediaType.MULTIPART_FORM_DATA_VALUE })
    public ResponseEntity<ProductoDTO> agregarProducto(
            @RequestPart("producto") @Valid ProductoRequest producto,
            @RequestPart(value = "imagen", required = false) MultipartFile imagen) throws IOException {
        ProductoDTO nuevoProducto = productoService.crearProducto(producto, imagen);
        return ResponseEntity.ok(nuevoProducto);
    }

    @Operation(summary = "Modificar información de un producto",
            description = "Actualiza los datos de un producto existente. Los vendedores solo pueden modificar sus propios productos.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    @PutMapping("/{id}")
    public ResponseEntity<ProductoDTO> modificarProducto(
            @PathVariable Long id, 
            @Valid @RequestBody ProductoRequest producto) {
        return productoService.actualizarProducto(id, producto)
                .map(ProductoMapper::toDTO)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @Operation(summary = "Eliminar producto del catálogo",
            description = "Elimina permanentemente un producto. Los vendedores solo pueden eliminar sus propios productos.")
    @PreAuthorize("hasAnyRole('ADMINISTRADOR','VENDEDOR')")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removerProducto(@PathVariable Long id) {
        boolean eliminado = productoService.eliminarProducto(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
