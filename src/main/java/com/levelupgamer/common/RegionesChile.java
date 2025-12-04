package com.levelupgamer.common;

import java.util.*;

public class RegionesChile {
    
    private static final Map<String, List<String>> REGIONES_COMUNAS = new LinkedHashMap<>();
    
    static {
        REGIONES_COMUNAS.put("Arica y Parinacota", Arrays.asList("Arica", "Camarones", "Putre", "General Lagos"));
        REGIONES_COMUNAS.put("Tarapacá", Arrays.asList("Iquique", "Alto Hospicio", "Pozo Almonte", "Camiña", "Colchane", "Huara", "Pica"));
        REGIONES_COMUNAS.put("Antofagasta", Arrays.asList("Antofagasta", "Mejillones", "Sierra Gorda", "Taltal", "Calama", "Ollagüe", "San Pedro de Atacama", "Tocopilla", "María Elena"));
        REGIONES_COMUNAS.put("Atacama", Arrays.asList("Copiapó", "Caldera", "Tierra Amarilla", "Chañaral", "Diego de Almagro", "Vallenar", "Alto del Carmen", "Freirina", "Huasco"));
        REGIONES_COMUNAS.put("Coquimbo", Arrays.asList("La Serena", "Coquimbo", "Andacollo", "La Higuera", "Paiguano", "Vicuña", "Ovalle", "Combarbalá", "Monte Patria", "Punitaqui", "Río Hurtado", "Illapel", "Canela", "Los Vilos", "Salamanca"));
        REGIONES_COMUNAS.put("Valparaíso", Arrays.asList("Valparaíso", "Viña del Mar", "Concón", "Quintero", "Puchuncaví", "Casablanca", "Juan Fernández", "Isla de Pascua", "Quilpué", "Villa Alemana", "Limache", "Olmué", "San Antonio", "Cartagena", "El Tabo", "El Quisco", "Algarrobo", "Santo Domingo", "San Felipe", "Llay-Llay", "Los Andes", "La Ligua", "Cabildo", "Petorca", "Zapallar"));
        REGIONES_COMUNAS.put("Metropolitana de Santiago", Arrays.asList("Santiago", "Cerrillos", "Cerro Navia", "Conchalí", "El Bosque", "Estación Central", "Huechuraba", "Independencia", "La Cisterna", "La Florida", "La Granja", "La Pintana", "La Reina", "Las Condes", "Lo Barnechea", "Lo Espejo", "Lo Prado", "Macul", "Maipú", "Ñuñoa", "Pedro Aguirre Cerda", "Peñalolén", "Providencia", "Pudahuel", "Quilicura", "Quinta Normal", "Recoleta", "Renca", "San Joaquín", "San Miguel", "San Ramón", "Vitacura", "Puente Alto", "Pirque", "San José de Maipo", "Colina", "Lampa", "Tiltil", "San Bernardo", "Buin", "Calera de Tango", "Paine", "San Pedro", "Melipilla", "Alhué", "Curacaví", "María Pinto", "San Pedro", "Talagante", "El Monte", "Isla de Maipo", "Padre Hurtado", "Peñaflor"));
        REGIONES_COMUNAS.put("Lib. Gral. Bdo. O'Higgins", Arrays.asList("Rancagua", "Codegua", "Coinco", "Coltauco", "Doñihue", "Graneros", "Las Cabras", "Machalí", "Malloa", "Mostazal", "Olivar", "Peumo", "Pichidegua", "Quinta de Tilcoco", "Rengo", "Requínoa", "San Vicente", "San Fernando", "Chépica", "Chimbarongo", "Lolol", "Nancagua", "Palmilla", "Peralillo", "Placilla", "Pumanque", "Santa Cruz", "Pichilemu", "La Estrella", "Litueche", "Marchihue", "Navidad", "Paredones"));
        REGIONES_COMUNAS.put("Maule", Arrays.asList("Talca", "Constitución", "Curepto", "Empedrado", "Maule", "Pelarco", "Pencahue", "Río Claro", "San Clemente", "San Rafael", "Cauquenes", "Chanco", "Pelluhue", "Curicó", "Hualañé", "Licantén", "Molina", "Rauco", "Romeral", "Sagrada Familia", "Teno", "Vichuquén", "Linares", "Colbún", "Longaví", "Parral", "Retiro", "San Javier", "Villa Alegre", "Yerbas Buenas"));
        REGIONES_COMUNAS.put("Ñuble", Arrays.asList("Chillán", "Bulnes", "Cobquecura", "Coelemu", "Coihueco", "El Carmen", "Ninhue", "Ñiquén", "Pemuco", "Pinto", "Portezuelo", "Quillón", "Quirihue", "Ránquil", "San Carlos", "San Fabián", "San Ignacio", "San Nicolás", "Treguaco", "Yungay", "Chillán Viejo"));
        REGIONES_COMUNAS.put("Biobío", Arrays.asList("Concepción", "Coronel", "Chiguayante", "Florida", "Hualpén", "Hualqui", "Lota", "Penco", "San Pedro de la Paz", "Santa Juana", "Talcahuano", "Tomé", "Lebu", "Arauco", "Cañete", "Contulmo", "Curanilahue", "Los Álamos", "Tirúa", "Los Ángeles", "Antuco", "Cabrero", "Laja", "Mulchén", "Nacimiento", "Negrete", "Quilaco", "Quilleco", "San Rosendo", "Santa Bárbara", "Tucapel", "Yumbel"));
        REGIONES_COMUNAS.put("Araucanía", Arrays.asList("Temuco", "Carahue", "Cholchol", "Cunco", "Curarrehue", "Freire", "Galvarino", "Gorbea", "Lautaro", "Loncoche", "Melipeuco", "Nueva Imperial", "Padre Las Casas", "Perquenco", "Pitrufquén", "Pucón", "Saavedra", "Teodoro Schmidt", "Toltén", "Vilcún", "Villarrica", "Angol", "Collipulli", "Curacautín", "Ercilla", "Lonquimay", "Los Sauces", "Lumaco", "Purén", "Renaico", "Traiguén", "Victoria"));
        REGIONES_COMUNAS.put("Los Ríos", Arrays.asList("Valdivia", "Corral", "Lanco", "Los Lagos", "Máfil", "Mariquina", "Paillaco", "Panguipulli", "La Unión", "Futrono", "Lago Ranco", "Río Bueno"));
        REGIONES_COMUNAS.put("Los Lagos", Arrays.asList("Puerto Montt", "Calbuco", "Cochamó", "Fresia", "Frutillar", "Los Muermos", "Llanquihue", "Maullín", "Puerto Varas", "Osorno", "Puerto Octay", "Purranque", "Puyehue", "Río Negro", "San Juan de la Costa", "San Pablo", "Castro", "Ancud", "Chonchi", "Curaco de Vélez", "Dalcahue", "Puqueldón", "Queilén", "Quellón", "Quemchi", "Quinchao", "Chaitén", "Futaleufú", "Hualaihué", "Palena"));
        REGIONES_COMUNAS.put("Aysén del Gral. Carlos Ibáñez del Campo", Arrays.asList("Coyhaique", "Lago Verde", "Aysén", "Cisnes", "Guaitecas", "Chile Chico", "Río Ibáñez", "Cochrane", "O'Higgins", "Tortel"));
        REGIONES_COMUNAS.put("Magallanes y de la Antártica Chilena", Arrays.asList("Punta Arenas", "Laguna Blanca", "Río Verde", "San Gregorio", "Cabo de Hornos", "Antártica", "Porvenir", "Primavera", "Timaukel", "Natales", "Torres del Paine"));
    }
    
    public static List<RegionDTO> getRegiones() {
        List<RegionDTO> regiones = new ArrayList<>();
        for (Map.Entry<String, List<String>> entry : REGIONES_COMUNAS.entrySet()) {
            regiones.add(new RegionDTO(entry.getKey(), entry.getValue()));
        }
        return regiones;
    }
    
    public static Map<String, List<String>> getComunasPorRegion() {
        return new LinkedHashMap<>(REGIONES_COMUNAS);
    }
}

