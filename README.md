# Carrito de Compras — Spring Boot

Aplicación monolítica de carrito de compras construida con **Spring Boot 3**, **Spring Data JPA** y base de datos relacional **H2** (desarrollo) / **PostgreSQL** (producción).

---

## 📐 Arquitectura

```
com.carrito/
├── model/                  # Entidades JPA (Carrito, ItemCarrito, Producto)
├── repository/             # Interfaces Spring Data (CarritoRepository, ProductoRepository)
├── service/                # Lógica de negocio (CarritoService, ProductoService)
├── controller/             # Controladores REST (CarritoController, ProductoController)
├── dto/                    # Objetos de transferencia de datos
│   ├── AgregarProductoDTO  # Request: agregar producto al carrito
│   ├── CarritoDTO          # Response: carrito completo
│   └── ItemCarritoDTO      # Response: ítem dentro del carrito
└── exception/              # Excepciones personalizadas y manejador global
```

### Diagrama de entidades (Base de datos)

```
producto          carrito           item_carrito
─────────         ────────          ─────────────
id (PK)           id (PK)           id (PK)
nombre            fecha_creacion    cantidad
precio                              precio_unitario
stock                               carrito_id (FK)
                                    producto_id (FK)
```

---

## 🚀 Cómo ejecutar

### Requisitos
- Java 17+
- Maven 3.8+

### Pasos

```bash
# 1. Clonar / descomprimir el proyecto
cd carrito-compras

# 2. Compilar y ejecutar (usa H2 en memoria por defecto)
./mvnw spring-boot:run

# 3. La API queda disponible en:
#    http://localhost:8080
```

### Consola H2 (base de datos en desarrollo)
Visita: [http://localhost:8080/h2-console](http://localhost:8080/h2-console)
- **JDBC URL**: `jdbc:h2:mem:carritobd`
- **User**: `sa` | **Password**: *(vacío)*

---

## 🔌 Endpoints REST

### Productos

| Método | URL | Descripción |
|--------|-----|-------------|
| GET | `/api/productos` | Listar todos los productos |
| GET | `/api/productos/{id}` | Obtener un producto |
| GET | `/api/productos?nombre=X` | Buscar por nombre |
| POST | `/api/productos` | Crear producto |
| PUT | `/api/productos/{id}` | Actualizar producto |
| DELETE | `/api/productos/{id}` | Eliminar producto |

### Carritos

| Método | URL | Descripción |
|--------|-----|-------------|
| POST | `/api/carritos` | Crear carrito vacío |
| POST | `/api/carritos/agregar-producto` | Agregar producto al carrito |
| GET | `/api/carritos/{id}` | Ver carrito con ítems y total |
| GET | `/api/carritos/{id}/total` | Obtener solo el total |

---

## 🧪 Ejemplos con curl

```bash
# 1. Ver productos disponibles (cargados automáticamente al iniciar)
curl http://localhost:8080/api/productos

# 2. Crear un carrito nuevo
curl -X POST http://localhost:8080/api/carritos

# 3. Agregar producto al carrito (carritoId=1, productoId=1, cantidad=2)
curl -X POST http://localhost:8080/api/carritos/agregar-producto \
  -H "Content-Type: application/json" \
  -d '{"carritoId": 1, "productoId": 1, "cantidad": 2}'

# 4. Ver el carrito completo
curl http://localhost:8080/api/carritos/1

# 5. Ver solo el total
curl http://localhost:8080/api/carritos/1/total

# 6. Crear un producto nuevo
curl -X POST http://localhost:8080/api/productos \
  -H "Content-Type: application/json" \
  -d '{"nombre": "Webcam HD", "precio": 45.99, "stock": 20}'
```

### Respuesta ejemplo de `GET /api/carritos/1`

```json
{
  "id": 1,
  "items": [
    {
      "nombreProducto": "Laptop Dell 15\"",
      "cantidad": 2,
      "subtotal": 1799.98
    }
  ],
  "total": 1799.98
}
```

---

## 🐘 Cambiar a PostgreSQL (producción)

Editar `src/main/resources/application.properties`:

```properties
# Comentar configuración H2
# spring.datasource.url=jdbc:h2:mem:carritobd...

# Descomentar configuración PostgreSQL
spring.datasource.url=jdbc:postgresql://localhost:5432/carritobd
spring.datasource.driver-class-name=org.postgresql.Driver
spring.datasource.username=postgres
spring.datasource.password=tu_password
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.PostgreSQLDialect
```

Crear la base de datos en PostgreSQL:
```sql
CREATE DATABASE carritobd;
```

---

## ✅ Ejecutar pruebas

```bash
./mvnw test
```
