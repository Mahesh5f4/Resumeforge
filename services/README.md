# ResumeForge Backend Services

Collection of microservices powering the ResumeForge platform.

## Services Overview

### Auth Service (Java/Spring Boot)
Authentication, authorization, and user management service.

**Port:** 8081

### Resume Core Service (Java/Spring Boot)
Resume upload, parsing, storage, and retrieval.

**Port:** 8082

### Rewrite Service (Java/Spring Boot)
AI-powered resume section rewrites and suggestions.

**Port:** 8083

### NLP Service (FastAPI)
Natural Language Processing for resume analysis and ATS scoring.

**Port:** 8084

## Quick Start

Each service can be run independently:

### Java Services

```bash
cd auth-service  # or resume-core-service, rewrite-service
mvn clean install
mvn spring-boot:run
```

### NLP Service

```bash
cd nlp-service
python -m venv venv
source venv/bin/activate  # or venv\Scripts\activate on Windows
pip install -r requirements.txt
uvicorn main:app --reload --port 8084
```

## Service Communication

Services communicate via:
- **HTTP/REST** for synchronous operations
- **Redis** for pub/sub and caching
- **Shared PostgreSQL** (Supabase) for data persistence

## Database

All services connect to the same Supabase PostgreSQL instance for data consistency.

Configure `SUPABASE_DB_URL` in `.env` for database connection.

## Development Notes

- Keep business logic in individual services
- Use Redis for cache layers
- Implement circuit breakers for external API calls
- Ensure proper error handling and logging
