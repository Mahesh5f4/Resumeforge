# Auth Service

Authentication and authorization service for ResumeForge.

**Technology:** Java 21 + Spring Boot 3

**Port:** 8081

## Overview

Handles:
- User registration and login
- JWT token generation and validation
- Role-based access control (RBAC)
- Session management via Redis
- Password hashing and security

## Quick Start

```bash
mvn clean install
mvn spring-boot:run
```

Or build and run JAR:

```bash
mvn clean package
java -jar target/auth-service-*.jar
```

Service runs on `http://localhost:8081`.

## Project Structure

```
auth-service/
├── src/
│   ├── main/
│   │   ├── java/com/resumeforge/auth/
│   │   │   ├── controller/     # REST endpoints
│   │   │   ├── service/        # Business logic
│   │   │   ├── model/          # Entity classes
│   │   │   ├── repository/     # Data access
│   │   │   └── config/         # Configuration
│   │   └── resources/
│   │       └── application.yml # Spring config
│   └── test/                   # Unit tests
│
├── pom.xml                     # Maven configuration
└── README.md
```

## Environment Variables

```env
JWT_SECRET=your-secret-key
SUPABASE_DB_URL=postgresql://...
REDIS_HOST=localhost
REDIS_PORT=6379
```

## Dependencies

Key Spring Boot starters used:
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-security
- spring-boot-starter-validation
