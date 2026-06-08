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
- [API Endpoints](#-api-endpoints)
- [Security Architecture](#-security-architecture)
- [Caching Strategy](#-caching-strategy)
- [Observability & Monitoring](#-observability--monitoring)
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
| Deployment | AWS RDS + Elastic Beanstalk | Cloud DB provisioning, app hosting, IAM roles |
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

## 📡 API Endpoints

### 🔓 Public Endpoints

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `POST` | `/api/auth/register/public` | Public | Register a new user |
| `POST` | `/api/auth/login/public` | Public | Authenticate and receive JWT |
| `GET` | `/api/companies/public` | Public | List public company info |
| `GET` | `/api/contacts/public` | Public | Public contact information |
| `GET` | `/api/csrf-token/public` | Public | Fetch CSRF token |
| `GET` | `/api/logging/public` | Public | Public logging endpoint |
| `GET` | `/swagger-ui/**` | Public | Swagger UI assets |
| `GET` | `/api/v3/api-docs/**` | Public | OpenAPI docs |
| `GET` | `/swagger-ui.html` | Public | Interactive API documentation |
| `GET` | `/webjars/**` | Public | Static web assets |
| `GET` | `/jobportal/actuator/**` | Public | Actuator health & metrics |
| `GET/POST` | `/api/todos/**` | Public | Todo endpoints |
| `GET/POST` | `/api/posts/**` | Public | Posts endpoints |

### 💼 Employer Endpoints (`ROLE_EMPLOYER`)

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET/POST` | `/api/jobs/employer` | ROLE_EMPLOYER | Manage job listings |
| `PATCH` | `/api/jobs/{jobId}/status/employer` | ROLE_EMPLOYER | Update job status |
| `GET` | `/api/jobs/applications/{jobId}/employer` | ROLE_EMPLOYER | View applicants for a specific job |
| `GET` | `/api/jobs/applications/employer` | ROLE_EMPLOYER | Update Job Application status |

### 👤 Jobseeker Endpoints (`ROLE_JOBSEEKER`)

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET/PUT` | `/api/users/profile/jobseeker` | ROLE_JOBSEEKER | View / update profile |
| `PUT` | `/api/users/profile/picture/jobseeker` | ROLE_JOBSEEKER | Upload profile picture |
| `PUT` | `/api/users/profile/resume/jobseeker` | ROLE_JOBSEEKER | Upload resume |
| `POST/DELETE` | `/api/users/saved-jobs/{jobId}/jobseeker` | ROLE_JOBSEEKER | Save / unsave a job |
| `GET` | `/api/users/saved-jobs/jobseeker` | ROLE_JOBSEEKER | List all saved jobs |
| `GET/POST` | `/api/users/job-applications/jobseeker` | ROLE_JOBSEEKER | List my applications / Apply for a job |
| `POST` | `/api/users/job-applications/{jobId}/jobseeker` | ROLE_JOBSEEKER | Withdraw appliation for a job |

### 🛡️ Admin Endpoints (`ROLE_ADMIN`)

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET` | `/api/contacts/admin` | ROLE_ADMIN | View all contacts |
| `GET` | `/api/contacts/sort/admin` | ROLE_ADMIN | Get contacts sorted |
| `GET` | `/api/contacts/page/admin` | ROLE_ADMIN | Get contacts paginated |
| `PATCH` | `/api/contacts/{id}/status/admin` | ROLE_ADMIN | Update contact status |
| `PUT/DELETE` | `/api/companies/{id}/admin` | ROLE_ADMIN | Update or delete a company |
| `GET/POST` | `/api/companies/admin` | ROLE_ADMIN | Manage all companies |
| `GET` | `/api/users/search/admin` | ROLE_ADMIN | Search users |
| `PATCH` | `/api/users/{userId}/role/employer/admin` | ROLE_ADMIN | Assign user to employer role |
| `PUT` | `/api/users/{userId}/company/{companyId}/admin` | ROLE_ADMIN | Assign user to a company |

### 🔒 Authenticated Endpoints (`Any Role`)

| Method | Endpoint | Access | Description |
|---|---|---|---|
| `GET/POST` | `/api/**` | Authenticated | All other secured API routes |

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

*JobPortal • Spring Boot 4.0 • Java 25*
