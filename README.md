# 🚀 TechShare - Sistema de E-commerce

Aplicación backend para un sistema de e-commerce con autenticación JWT, roles (Admin/User) y manejo de productos, categorías y ventas.

## 🔧 Tecnologías Principales
- **Spring Boot 3**
- **Spring Security + JWT**
- **OAuth2 (Google)**
- **Base de datos**: PostgreSQL/MySQL
- **Manejo de imágenes**: Almacenamiento local

## 📂 Estructura de Controladores

### 🔐 Autenticación (`/auth`)
| Endpoint              | Método | Descripción                              | Acceso       |
|-----------------------|--------|------------------------------------------|--------------|
| `/auth/login`         | POST   | Login con JWT (guarda token en cookie)   | Público      |
| `/auth/register`      | POST   | Registro de usuario (envía email)        | Público      |
| `/auth/verify-account`| GET    | Verifica cuenta con token                | Público      |

### 🌐 OAuth2 (`/oauth2`)
| Endpoint              | Método | Descripción                              |
|-----------------------|--------|------------------------------------------|
| `/oauth2/login/google`| GET    | Inicia flujo OAuth2 con Google          |
| `/oauth2/success`     | GET    | Callback éxito (retorna token)           |
| `/oauth2/error`       | GET    | Callback error                           |

## 👨‍💻 Panel de Administrador (`/admin/**`)
*(Requiere rol ADMIN)*

### 📊 Dashboard
- `GET /dashboard`: Mensaje de bienvenida admin

### 🗃️ Categorías (`/admin/category`)
| Endpoint              | Método | Descripción                              |
|-----------------------|--------|------------------------------------------|
| `/save`              | POST   | Crea categoría (con imagen)              |
| `/{id}`              | GET    | Obtiene categoría por ID                 |
| `/{id}`              | PUT    | Actualiza categoría                      |
| `/all`               | GET    | Lista todas las categorías               |
| `/{id}`              | DELETE | Elimina categoría                        |

### 📦 Materiales (`/admin/material`)
*(Similar estructura a categorías, con imágenes)*

### 🔄 Movimientos (`/admin/movement`)
| Endpoint              | Método | Descripción                              |
|-----------------------|--------|------------------------------------------|
| `/`                  | POST   | Crea movimiento                          |
| `/all`               | GET    | Lista paginada (9 items/página)          |
| `/{id}`              | GET    | Obtiene movimiento por ID                |

### 🖼️ Imágenes (`/images`)
| Endpoint              | Método | Descripción                              |
|-----------------------|--------|------------------------------------------|
| `/{imageName}`       | GET    | Devuelve imagen binaria                  |

## 👤 Área de Usuario (`/api/**`)

### 🏠 Home (`/api`)
| Endpoint                     | Método | Descripción                              |
|------------------------------|--------|------------------------------------------|
| `/category/all`             | GET    | Lista categorías                         |
| `/material/all`             | GET    | Lista materiales                         |
| `/material/filter`          | GET    | Filtra por categoría/subcategoría        |
| `/material/highest-price`   | GET    | Material con precio más alto             |

### 🛒 Ventas (`/api/sale`)
| Endpoint              | Método | Descripción                              | Acceso       |
|-----------------------|--------|------------------------------------------|--------------|
| `/`                  | POST   | Crea venta (asociada al usuario logueado)| USER         |
| `/user`              | GET    | Historial de compras del usuario         | USER         |
| `/{status}`          | GET    | Ventas por estado (ej: "completed")      | ADMIN        |
| `/{id}/status`       | PATCH  | Cambia estado de venta                   | ADMIN        |

## 🔒 Configuración de Seguridad
- **Roles**: 
  - `ADMIN`: Acceso total
  - `USER`: Solo operaciones de compra/consulta
- **Protección de endpoints**: 
  ```java
  @PreAuthorize("hasRole('ADMIN')")
