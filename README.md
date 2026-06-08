# 🚀 JobPortal — Spring Boot Backend

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-25-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![JWT](https://img.shields.io/badge/JWT-JJWT%200.13-000000?style=for-the-badge&logo=jsonwebtokens&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED?style=for-the-badge&logo=docker&logoColor=white)
![AWS](https://img.shields.io/badge/AWS-RDS%20%2B%20EC2-FF9900?style=for-the-badge&logo=amazonaws&logoColor=white)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI%203-85EA2D?style=for-the-badge&logo=swagger&logoColor=black)

> A production-grade **Job Portal REST API** built with **Spring Boot 4.0** and **Java 25**, featuring JWT-based authentication, role-based access control, Swagger UI documentation, Caffeine caching, OpenTelemetry observability, and MySQL persistence — containerized with Docker Compose and deployable to AWS.

---

## 📋 Table of Contents

- [Project Overview](#-project-overview)
- [Tech Stack](#-tech-stack--learning-outcomes)
- [Key Features](#-key-features)
- [Prerequisites](#-prerequisites)
- [Getting Started](#-getting-started)
- [API Endpoints](#-api-endpoints)
- [Security Architecture](#-security-architecture)
- [Caching Strategy](#-caching-strategy)
- [Observability & Monitoring](#-observability--monitoring)
- [Project Structure](#-project-structure)
- [AWS Deployment](#-aws-deployment-guide)
- [Key Learning Takeaways](#-key-learning-takeaways)

---

## 📖 Project Overview

**JobPortal** is a fully-featured backend application built using **Spring Boot 4.0** and **Java 25**. It demonstrates modern enterprise Java development practices including:

- Stateless JWT-based security with role-based access control
- Caffeine in-process caching for high-performance reads
- Distributed tracing with OpenTelemetry + structured Logback logging
- One-command Docker Compose local setup
- AWS-ready deployment packaged as `jobportal-aws-deployment.jar`

---

## 🛠️ Tech Stack & Learning Outcomes

| Layer | Technology | What I Learned |
|---|---|---|
| Core Framework | Spring Boot 4.0 + Java 25 | Latest LTS features, Virtual Threads, modern APIs |
| API Layer | Spring Web MVC | REST API design, controller advice, request validation |
| Security | Spring Security + JWT (JJWT 0.13) | Auth filters, role-based access, token lifecycle |
| Database | MySQL + Spring Data JPA | ORM, entity relationships, query optimizations |
| Caching | Caffeine Cache 3.2 | In-memory caching, TTL, eviction policies |
| API Docs | SpringDoc OpenAPI 3 (Swagger UI) | API documentation, schema annotation, versioning |
| Observability | Actuator + OpenTelemetry + Logback | Metrics, distributed tracing, structured logging |
| HTTP Client | Spring RestClient | Modern declarative HTTP calls, reactive-style API |
| Containerization | Docker Compose | Local dev environment, service orchestration |
| Deployment | AWS RDS + EC2/ECS | Cloud DB provisioning, app hosting, IAM roles |
| Dev Productivity | Lombok + DevTools | Boilerplate reduction, hot reload, annotation processing |
| Validation | Spring Validation (Jakarta) | Bean validation, constraint handling, custom validators |

---

## ✨ Key Features

| Feature | Description |
|---|---|
| 🔐 JWT Authentication | Stateless token-based auth with role-based access control |
| 💼 Job Management APIs | CRUD for job listings, applications, and user profiles |
| 📄 Swagger UI | Interactive API docs via SpringDoc OpenAPI 3 |
| ⚡ Caffeine Caching | High-performance in-process cache to reduce DB round-trips |
| 📡 OpenTelemetry Tracing | Distributed tracing and structured Logback appender integration |
| 🐳 Docker Compose Support | One-command local dev environment with MySQL and app containers |
| ✅ Input Validation | Jakarta Bean Validation with custom constraint handlers |
| ☁️ AWS-ready Deployment | Packaged as `jobportal-aws-deployment.jar` for EC2/ECS hosting |
| 🌐 RestClient HTTP Layer | Modern Spring declarative HTTP client for external integrations |
| 🩺 Actuator Health Checks | Production-ready endpoints for health, metrics, and info |

---

## 📦 Prerequisites

| Tool | Version | Notes |
|---|---|---|
| Java | 25+ | JDK — Temurin or Oracle |
| Maven | 3.9+ | Build tool for dependency management |
| Docker | Latest | Docker Desktop for running MySQL via Compose |
| MySQL | 8+ | Optional if using Docker Compose |
| AWS CLI | Latest | Required for cloud deployment steps |

---

## 🚀 Getting Started

### 1. Clone the Repository

```bash
git clone https://github.com/your-username/jobPortal.git
cd jobPortal
```

### 2. Configure Application Properties

Edit `src/main/resources/application.yml`:

```yaml
spring:
  datasource:
    url: jdbc:mysql://localhost:3306/jobportal
    username: root
    password: yourpassword
  jpa:
    hibernate:
      ddl-auto: update

jwt:
  secret: your-256-bit-secret-key
  expiration: 86400000
```

### 3. Start with Docker Compose (Recommended)

```bash
docker-compose up -d
```

> This starts MySQL automatically — no separate DB installation needed.

### 4. Build and Run

```bash
./mvnw clean package -DskipTests
java -jar target/jobportal-aws-deployment.jar
```

### 5. Access Swagger UI

```
http://localhost:8080/swagger-ui.html
```

---

## 📡 API Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register` | Public | Register a new user |
| `POST` | `/api/auth/login` | Public | Authenticate and receive JWT |
| `GET` | `/api/jobs` | Authenticated | List all available job postings |
| `POST` | `/api/jobs` | ROLE_EMPLOYER | Create a new job listing |
| `GET` | `/api/jobs/{id}` | Authenticated | Get job details by ID |
| `PUT` | `/api/jobs/{id}` | ROLE_EMPLOYER | Update an existing job listing |
| `DELETE` | `/api/jobs/{id}` | ROLE_EMPLOYER | Delete a job listing |
| `POST` | `/api/applications` | ROLE_CANDIDATE | Apply for a job |
| `GET` | `/api/applications/mine` | ROLE_CANDIDATE | List my applications |
| `GET` | `/api/applications/{jobId}` | ROLE_EMPLOYER | View applicants for a job |
| `GET` | `/actuator/health` | Public | Health check endpoint |
| `GET` | `/swagger-ui.html` | Public | Interactive API documentation |

---

## 🔐 Security Architecture

### Authentication Flow

```
Client → POST /api/auth/login
       → Server validates credentials
       → Issues signed JWT (JJWT 0.13)
       → Client sends: Authorization: Bearer <token>
       → JwtAuthFilter validates token
       → Sets SecurityContext with roles
       → Spring Security enforces @PreAuthorize
```

### Roles

| Role | Permissions |
|---|---|
| `ROLE_CANDIDATE` | Register, browse jobs, submit applications |
| `ROLE_EMPLOYER` | Create/manage job listings, view applicants |
| `ROLE_ADMIN` | Full access including user management |

---

## ⚡ Caching Strategy

Caffeine is configured as the default `CacheManager` via Spring Cache abstraction.

```java
@Cacheable("jobListings")
public List<Job> getAllJobs() { ... }

@CacheEvict(value = "jobListings", allEntries = true)
public Job createJob(JobRequest request) { ... }
```

**Cache Names:**
- `jobListings` — active job postings
- `userProfiles` — user profile lookups
- `applicationStatus` — application status checks

**Strategy:** Evict on write/update to keep data consistent under concurrent access.

---

## 📡 Observability & Monitoring

- **Spring Boot Actuator** — exposes `/actuator/health`, `/actuator/metrics`, `/actuator/info`
- **OpenTelemetry SDK** — auto-instruments HTTP requests, JPA queries, and RestClient calls
- **Logback OTLP Appender** — ships structured logs with `trace_id` and `span_id` for log-trace correlation
- **Compatible backends** — Grafana Tempo, Jaeger, AWS X-Ray (any OTLP-capable collector)

---

## 📁 Project Structure

```
src/
├── main/
│   ├── java/com/spring/learning/jobPortal/
│   │   ├── config/          # SecurityConfig, CacheConfig, OpenApiConfig
│   │   ├── controller/      # AuthController, JobController, ApplicationController
│   │   ├── dto/             # Request/Response DTOs with validation annotations
│   │   ├── entity/          # JPA Entities: User, Job, Application
│   │   ├── exception/       # GlobalExceptionHandler, custom exceptions
│   │   ├── filter/          # JwtAuthFilter
│   │   ├── repository/      # Spring Data JPA Repositories
│   │   ├── service/         # Business logic layer
│   │   └── util/            # JwtUtil, ResponseBuilder
│   └── resources/
│       ├── application.yml  # App configuration
│       └── compose.yml      # Docker Compose (MySQL)
└── test/                    # Unit + Integration tests
```

---

## ☁️ AWS Deployment Guide

### Option A: EC2 + RDS

```bash
# 1. Build the JAR
./mvnw clean package -DskipTests

# 2. Copy to EC2
scp target/jobportal-aws-deployment.jar ec2-user@<your-ec2-ip>:~/

# 3. SSH into EC2 and run
ssh ec2-user@<your-ec2-ip>
java -jar jobportal-aws-deployment.jar \
  --spring.datasource.url=jdbc:mysql://<rds-endpoint>:3306/jobportal \
  --spring.datasource.username=admin \
  --spring.datasource.password=<secret>
```

> **RDS Tip:** Use `db.t3.micro` for dev/learning (Free Tier eligible).

### Option B: ECS (Fargate)

1. Dockerize the app and push image to ECR
2. Create ECS Task Definition referencing the ECR image
3. Configure ECS Service with ALB for load balancing
4. Store DB credentials in **AWS Secrets Manager** and inject via environment variables

---

## 🎯 Key Learning Takeaways

- How Spring Security filter chain integrates with JWT for stateless authentication
- Role serialization in JWT claims and deserialization into `GrantedAuthority`
- CORS configuration with `allowCredentials` and specific origin patterns
- Spring Cache abstraction decouples business logic from cache implementation
- OpenTelemetry auto-instrumentation with zero changes to business logic
- Docker Compose support module in Spring Boot 3+ for seamless local dev
- Spring Boot 4.0 changes: updated auto-configuration, Virtual Thread support
- Jakarta Bean Validation for request validation with meaningful error responses
- SpringDoc vs Springfox: migrating to OpenAPI 3 with `springdoc-openapi`
- AWS RDS instance selection, VPC configuration, and security group setup

---

## 📝 Resume One-liner

> *Built a cloud-deployable Job Portal REST API using Spring Boot 4.0, Java 25, JWT security, Caffeine cache, OpenTelemetry tracing, and MySQL — containerized with Docker Compose and deployed on AWS RDS + EC2.*

---

## 👨‍💻 Author

**Arvind Kumar Singh**
Senior Java Backend Developer | 6.5+ Years Experience
Spring Boot • Microservices • Kafka • Redis • Docker • Kubernetes

---

*JobPortal • Spring Boot 4.0 • Java 25*
