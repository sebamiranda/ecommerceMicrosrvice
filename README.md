# 🛒 E-commerce Microservices Architecture

Sistema de microservicios para e-commerce desarrollado con **Spring Boot**, implementando una arquitectura escalable y modular con múltiples servicios especializados.

## 🚀 Tecnologías Utilizadas

- **Java 17** - Lenguaje de programación
- **Spring Boot 3.x** - Framework principal
- **Spring Data JPA** - Persistencia relacional (PostgreSQL)
- **Spring Data MongoDB** - Persistencia documental (MongoDB)
- **Spring Web** - API REST
- **Maven** - Gestión de dependencias
- **Docker & Docker Compose** - Contenedorización
- **PostgreSQL** - Base de datos relacional (Products & Categories)
- **MongoDB** - Base de datos NoSQL (Customers)
- **Lombok** - Reducción de código boilerplate
- **Bean Validation** - Validación de datos

## 🏗️ Arquitectura de Microservicios

El proyecto está compuesto por tres microservicios independientes:

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│   Customer      │     │    Product      │     │    Category     │
│   Service       │     │    Service      │     │    Service      │
│   (MongoDB)     │     │  (PostgreSQL)   │     │  (PostgreSQL)   │
└─────────────────┘     └─────────────────┘     └─────────────────┘
```

### Estructura del Proyecto

```
src/
├── main/
│   ├── java/com/seba/microservices/
│   │   ├── customer/
│   │   │   ├── controller/      # Customer REST API
│   │   │   ├── service/         # Lógica de negocio
│   │   │   ├── repository/      # MongoDB Repository
│   │   │   └── model/           # Documentos MongoDB
│   │   ├── product/
│   │   │   ├── controller/      # Product REST API
│   │   │   ├── service/         # Lógica de negocio
│   │   │   ├── repository/      # JPA Repository
│   │   │   └── model/           # Entidades JPA
│   │   └── category/
│   │       ├── controller/      # Category REST API
│   │       ├── service/         # Lógica de negocio
│   │       ├── repository/      # JPA Repository
│   │       └── model/           # Entidades JPA
│   └── resources/
│       └── application.yml      # Configuración
└── test/                        # Tests unitarios e integración
```

## 📡 API Endpoints

### 🧑 Customer Service (MongoDB)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/customers` | Crear nuevo cliente |
| `GET` | `/api/customers` | Obtener todos los clientes |
| `GET` | `/api/customers/{customerId}` | Obtener cliente por ID |
| `PUT` | `/api/customers` | Actualizar cliente |
| `DELETE` | `/api/customers/{customerId}` | Eliminar cliente |

**Ejemplo Request (POST /api/customers):**
```json
{
  "firstName": "Juan",
  "lastName": "Pérez",
  "email": "juan.perez@email.com",
  "address": {
    "street": "Av. Siempre Viva 123",
    "city": "San Salvador de Jujuy",
    "zipCode": "4600"
  }
}
```

---

### 📦 Product Service (PostgreSQL)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/products` | Crear nuevo producto |
| `GET` | `/api/products` | Obtener todos los productos |
| `GET` | `/api/products/{id}` | Obtener producto por ID |
| `GET` | `/api/products/category/{id}` | Obtener productos por categoría |
| `PUT` | `/api/products` | Actualizar producto |
| `DELETE` | `/api/products/{id}` | Eliminar producto |
| `POST` | `/api/products/purchase` | Procesar compra (reduce stock) |
| `POST` | `/api/products/restock` | Reabastecer inventario |

**Ejemplo Request (POST /api/products):**
```json
{
  "name": "Laptop Dell XPS 15",
  "description": "Laptop de alto rendimiento para profesionales",
  "price": 1299.99,
  "availableQuantity": 50,
  "categoryId": 1
}
```

**Ejemplo Request (POST /api/products/purchase):**
```json
[
  {
    "productId": 1,
    "quantity": 2
  },
  {
    "productId": 3,
    "quantity": 1
  }
]
```

---

### 🏷️ Category Service (PostgreSQL)

| Método | Endpoint | Descripción |
|--------|----------|-------------|
| `POST` | `/api/categories` | Crear nueva categoría |
| `GET` | `/api/categories` | Obtener todas las categorías |
| `GET` | `/api/categories/{id}` | Obtener categoría por ID |
| `PUT` | `/api/categories` | Actualizar categoría |
| `DELETE` | `/api/categories/{id}` | Eliminar categoría |

**Ejemplo Request (POST /api/categories):**
```json
{
  "name": "Electronics",
  "description": "Dispositivos electrónicos y gadgets"
}
```

---

## 🐳 Instalación con Docker

### Prerrequisitos

- Docker Desktop instalado
- Java 17 o superior
- Maven 3.6+

### 1. Levantar las bases de datos

```bash
docker-compose up -d
```

Esto iniciará:
- **PostgreSQL** en `localhost:5432` (Products & Categories)
  - Database: `product_db`
  - User: `postgres`
  - Password: `postgres`
  
- **MongoDB** en `localhost:27017` (Customers)
  - User: `root`
  - Password: `pass`

### 2. Verificar contenedores activos

```bash
docker ps
```

Deberías ver:
- `mongo_db` (MongoDB)
- `postgresql_db` (PostgreSQL)

### 3. Compilar y ejecutar los microservicios

```bash
# Compilar el proyecto
mvn clean install

# Ejecutar la aplicación
mvn spring-boot:run
```

La aplicación estará disponible en: `http://localhost:8080`

---

## 🔧 Configuración

### application.yml

Configuración de conexión a las bases de datos:

```yaml
spring:
  # PostgreSQL - Products & Categories
  datasource:
    url: jdbc:postgresql://localhost:5432/product_db
    username: postgres
    password: postgres
  jpa:
    hibernate:
      ddl-auto: update
    properties:
      hibernate:
        dialect: org.hibernate.dialect.PostgreSQLDialect

  # MongoDB - Customers  
  data:
    mongodb:
      host: localhost
      port: 27017
      database: customer_db
      username: root
      password: pass
      authentication-database: admin

server:
  port: 8080
```

---



### Probar los endpoints con cURL

**Crear un cliente:**
```bash
curl -X POST http://localhost:8080/api/customers \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "María",
    "lastName": "González",
    "email": "maria@email.com"
  }'
```

**Obtener todos los productos:**
```bash
curl http://localhost:8080/api/products
```

**Crear una categoría:**
```bash
curl -X POST http://localhost:8080/api/categories \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Laptops",
    "description": "Computadoras portátiles"
  }'
```

---

## 📦 Generar JAR ejecutable

```bash
mvn clean package
java -jar target/ecommerce-microservice-0.0.1-SNAPSHOT.jar
```

---

## 🗄️ Gestión de Bases de Datos

### Conectarse a PostgreSQL

```bash
docker exec -it postgresql_db psql -U postgres -d product_db
```

Comandos útiles:
```sql
\dt                    -- Listar tablas
SELECT * FROM product; -- Ver productos
SELECT * FROM category; -- Ver categorías
```

### Conectarse a MongoDB

```bash
docker exec -it mongo_db mongosh -u root -p pass --authenticationDatabase admin
```

Comandos útiles:
```javascript
show dbs                    // Listar bases de datos
use customer_db             // Seleccionar base de datos
db.customer.find()          // Ver todos los clientes
db.customer.countDocuments() // Contar documentos
```

---

## 🛠️ Características Implementadas

- ✅ **Arquitectura de Microservicios** - Servicios independientes y especializados
- ✅ **Persistencia Híbrida** - PostgreSQL para datos relacionales, MongoDB para documentos
- ✅ **CRUD Completo** - Operaciones completas en todos los servicios
- ✅ **Validación de Datos** - Bean Validation en todos los endpoints
- ✅ **Gestión de Inventario** - Sistema de compra y reabastecimiento
- ✅ **Relaciones** - Productos asociados a categorías
- ✅ **Dockerización** - Bases de datos containerizadas
- ✅ **Manejo de Excepciones** - Respuestas HTTP apropiadas

---

## 🚀 Próximas Mejoras

- [ ] **API Gateway** - Centralizar el acceso a los microservicios
- [ ] **Service Discovery** - Eureka Server para registro de servicios
- [ ] **Seguridad** - Spring Security + JWT
- [ ] **Documentación API** - Swagger/OpenAPI 3.0
- [ ] **Order Service** - Microservicio de pedidos
- [ ] **Payment Service** - Integración de pagos
- [ ] **Message Queue** - RabbitMQ o Kafka para comunicación asíncrona
- [ ] **Caché Distribuido** - Redis para mejorar performance
- [ ] **Monitoreo** - Spring Boot Actuator + Prometheus + Grafana
- [ ] **Circuit Breaker** - Resilience4j para tolerancia a fallos
- [ ] **Tests de Integración** - TestContainers
- [ ] **CI/CD** - GitHub Actions

---

## 📋 Comandos Docker Útiles

```bash
# Iniciar los contenedores
docker-compose up -d

# Ver logs
docker-compose logs -f

# Detener los contenedores
docker-compose down

# Detener y eliminar volúmenes (¡cuidado, elimina los datos!)
docker-compose down -v

# Reiniciar un servicio específico
docker-compose restart postgresql
```

---

## 👨‍💻 Autor

**Sebastián Miranda**
- GitHub: [@sebamiranda](https://github.com/sebamiranda)
- LinkedIn: [(https://www.linkedin.com/in/sebastian-miranda-/)]



## 🤝 Contribuciones

Las contribuciones son bienvenidas. Para cambios importantes:

1. Fork el proyecto
2. Crea una rama para tu feature (`git checkout -b feature/AmazingFeature`)
3. Commit tus cambios (`git commit -m 'Add some AmazingFeature'`)
4. Push a la rama (`git push origin feature/AmazingFeature`)
5. Abre un Pull Request

---

⭐ **Si este proyecto te resultó útil, dale una estrella en GitHub!**