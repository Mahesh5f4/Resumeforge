# Resume Core Service

Resume upload, parsing, storage, and retrieval for ResumeForge.

**Technology:** Java 21 + Spring Boot 3

**Port:** 8082

## Overview

Handles:
- Resume file upload and validation
- Resume parsing and metadata extraction
- Resume storage and retrieval
- Version history and tracking
- Resume search and filtering

## Quick Start

```bash
mvn clean install
mvn spring-boot:run
```

Or build and run JAR:

```bash
mvn clean package
java -jar target/resume-core-service-*.jar
```

Service runs on `http://localhost:8082`.

## Project Structure

```
resume-core-service/
├── src/
│   ├── main/
│   │   ├── java/com/resumeforge/resume/
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
SUPABASE_DB_URL=postgresql://...
SUPABASE_STORAGE_URL=https://...
```

## Dependencies

Key Spring Boot starters used:
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- commons-io (file handling)
