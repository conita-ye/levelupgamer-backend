# Conexión Frontend-Backend - Level-Up Gamer Store

## Arquitectura General

El frontend (React + Vite) se comunica con el backend (Spring Boot) mediante una API REST. La comunicación se realiza mediante peticiones HTTP usando Axios.

```
Frontend (React)  ←→  API REST  ←→  Backend (Spring Boot)  ←→  PostgreSQL + S3
```

## Configuración del Frontend

### Variables de Entorno

El frontend usa variables de entorno para configurar la URL del backend:

**Archivo**: `frontend/fullstrak_cony2/.env` o `.env.local`

```bash
VITE_API_BASE_URL=http://localhost:8081/api/v1
```

**Para producción:**
```bash
VITE_API_BASE_URL=https://tu-dominio-backend.com/api/v1
```

### Servicio API

El frontend tiene un servicio centralizado en `src/services/api.ts` que:

1. **Configura Axios** con la URL base del backend
2. **Interceptores de Request**: Agrega automáticamente el token JWT a todas las peticiones
3. **Interceptores de Response**: Maneja la renovación automática de tokens cuando expiran

```typescript
// Ejemplo de uso en el frontend
import { apiService } from '@/services/api';

// Login
const response = await apiService.login(email, password);

// Obtener productos
const productos = await apiService.getProductos();

// Agregar al carrito
await apiService.addToCart(productId, quantity);
```

## Configuración del Backend

### Puerto y CORS

El backend está configurado para:

- **Puerto**: `8081` (configurable con `SERVER_PORT`)
- **CORS**: Habilitado para todos los orígenes (`*`)
- **Base Path**: `/api/v1`

### Endpoints Principales

#### Autenticación
- `POST /api/v1/auth/login` - Iniciar sesión
- `POST /api/v1/auth/register` - Registrar usuario
- `POST /api/v1/auth/refresh` - Renovar token

#### Productos
- `GET /api/v1/products` - Listar productos
- `GET /api/v1/products/{id}` - Obtener producto
- `GET /api/v1/products/featured` - Productos destacados

#### Carrito
- `GET /api/v1/cart/{userId}` - Obtener carrito
- `POST /api/v1/cart/{userId}/items` - Agregar producto
- `DELETE /api/v1/cart/{userId}/items/{itemId}` - Eliminar producto

#### Usuarios
- `GET /api/v1/users/{id}` - Obtener perfil
- `PUT /api/v1/users/{id}` - Actualizar perfil

#### Regiones
- `GET /api/v1/regions` - Obtener regiones de Chile

## Flujo de Autenticación

### 1. Login

```typescript
// Frontend envía credenciales
POST /api/v1/auth/login
{
  "email": "usuario@example.com",
  "password": "password123"
}

// Backend responde con tokens
{
  "accessToken": "eyJhbGciOiJIUzI1NiIs...",
  "refreshToken": "eyJhbGciOiJIUzI1NiIs...",
  "user": {
    "id": 1,
    "nombre": "Usuario",
    "email": "usuario@example.com",
    "roles": ["CLIENTE"]
  }
}
```

### 2. Almacenamiento de Tokens

El frontend almacena los tokens en `localStorage`:

```typescript
localStorage.setItem('accessToken', accessToken);
localStorage.setItem('refreshToken', refreshToken);
localStorage.setItem('user', JSON.stringify(user));
```

### 3. Peticiones Autenticadas

El interceptor de Axios agrega automáticamente el token:

```typescript
// Interceptor agrega: Authorization: Bearer <token>
GET /api/v1/users/1
Headers: {
  "Authorization": "Bearer eyJhbGciOiJIUzI1NiIs..."
}
```

### 4. Renovación Automática

Si el token expira (401), el interceptor intenta renovarlo:

```typescript
// 1. Detecta 401
// 2. Usa refreshToken para obtener nuevo accessToken
POST /api/v1/auth/refresh
{
  "refreshToken": "eyJhbGciOiJIUzI1NiIs..."
}

// 3. Reintenta la petición original con el nuevo token
```

## Manejo de Errores

### Errores HTTP

El frontend maneja errores comunes:

- **401 Unauthorized**: Token expirado o inválido → Renueva token o redirige a login
- **403 Forbidden**: Sin permisos → Muestra mensaje de error
- **404 Not Found**: Recurso no encontrado → Muestra mensaje apropiado
- **500 Server Error**: Error del servidor → Muestra mensaje genérico

### Ejemplo de Manejo

```typescript
try {
  const producto = await apiService.getProducto(id);
} catch (error) {
  if (error.response?.status === 404) {
    toast.error('Producto no encontrado');
  } else {
    toast.error('Error al cargar el producto');
  }
}
```

## Contextos de React

El frontend usa Context API para compartir estado:

### AuthContext
- Maneja autenticación del usuario
- Proporciona: `user`, `isAuthenticated`, `login()`, `logout()`, `register()`

### CartContext
- Maneja el carrito de compras
- Proporciona: `cart`, `addToCart()`, `removeFromCart()`, `clearCart()`

### NavigationContext
- Maneja navegación entre páginas
- Proporciona: `onNavigate()`


## Estructura de Datos

### Producto

```typescript
interface Product {
  id: number;
  codigo: string;
  nombre: string;
  descripcion: string;
  precio: number;
  stock: number;
  categoria: {
    id: number;
    nombre: string;
    codigo: string;
  };
  imagenes: string[];
  puntosLevelUp?: number;
}
```

### Usuario

```typescript
interface User {
  id: number;
  run: string;
  nombre: string;
  apellidos: string;
  email: string;
  fechaNacimiento: string;
  direccion: string;
  region: string;
  comuna: string;
  roles: string[];
  puntosLevelUp: number;
}
```

## Desarrollo Local

### Iniciar Backend

```bash
cd backend/LevelUpGamer-backend
./mvnw spring-boot:run
```

Backend disponible en: `http://localhost:8081`

### Iniciar Frontend

```bash
cd frontend/fullstrak_cony2
npm install
npm run dev
```

Frontend disponible en: `http://localhost:5173`

### Verificar Conexión

1. Abre el navegador en `http://localhost:5173`
2. Abre la consola del navegador (F12)
3. Verifica que las peticiones vayan a `http://localhost:8081/api/v1`
4. No debe haber errores de CORS

## Producción

### Configuración Backend

Variables de entorno en EC2 (`/etc/levelupgamer/environment.conf`):

```bash
DB_URL=jdbc:postgresql://tu-rds-endpoint:5432/levelupgamer
DB_USER=admin
DB_PASSWORD=tu_password
S3_BUCKET_NAME=levelupgamer-assets
S3_BUCKET_URL=https://levelupgamer-assets.s3.us-east-1.amazonaws.com
AWS_REGION=us-east-1
STORAGE_PROVIDER=s3
SERVER_PORT=8081
SPRING_PROFILES_ACTIVE=prod
```

### Configuración Frontend

Variables de entorno en el build:

```bash
VITE_API_BASE_URL=https://tu-dominio-backend.com/api/v1
```

O configura en el servidor web (nginx, etc.) para hacer proxy.

### CORS en Producción

El backend ya está configurado con CORS en `*`, pero puedes restringirlo:

```java
// SecurityConfig.java
configuration.setAllowedOriginPatterns(Arrays.asList(
    "https://tu-dominio-frontend.com",
    "https://www.tu-dominio-frontend.com"
));
```

## Troubleshooting

### Error: CORS bloqueado

**Solución**: Verifica que `SecurityConfig.java` tenga CORS habilitado y que el backend esté corriendo.

### Error: 401 Unauthorized

**Solución**: Verifica que el token esté en `localStorage` y que no haya expirado. El interceptor debería renovarlo automáticamente.

### Error: Network Error

**Solución**: 
1. Verifica que el backend esté corriendo
2. Verifica la URL en `VITE_API_BASE_URL`
3. Verifica que no haya firewall bloqueando

### Las imágenes no cargan

**Solución**: 
1. Verifica que S3 esté configurado correctamente
2. Verifica que las URLs de las imágenes sean accesibles públicamente
3. Verifica CORS en el bucket S3

## Swagger UI

El backend incluye Swagger UI para documentación de la API:

**URL**: `http://localhost:8081/swagger-ui/index.html`

Aquí puedes ver todos los endpoints disponibles y probarlos directamente.

