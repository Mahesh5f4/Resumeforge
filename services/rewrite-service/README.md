# Rewrite Service

AI-powered resume rewriting and suggestion engine for ResumeForge.

**Technology:** Java 21 + Spring Boot 3

**Port:** 8083

## Overview

Handles:
- Resume section rewrites
- Content suggestions and improvements
- Tone and format optimization
- Change tracking and versioning
- ATS compatibility enhancements

## Quick Start

```bash
mvn clean install
mvn spring-boot:run
```

Or build and run JAR:

```bash
mvn clean package
java -jar target/rewrite-service-*.jar
```

Service runs on `http://localhost:8083`.

## Project Structure

```
rewrite-service/
├── src/
│   ├── main/
│   │   ├── java/com/resumeforge/rewrite/
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
GROQ_API_KEY=your-api-key
```

## Integration with NLP Service

Communicates with NLP Service for advanced text analysis before rewriting.

## Dependencies

Key Spring Boot starters used:
- spring-boot-starter-web
- spring-boot-starter-data-jpa
- spring-boot-starter-validation
- okhttp ( HTTP client for NLP service calls)
