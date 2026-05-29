# Infrastructure

Infrastructure configuration, Docker setup, and deployment scripts for ResumeForge.

## Contents

### docker/
Docker configuration files and build contexts for containerizing services.

### nginx/
Nginx reverse proxy configuration for:
- Route traffic to backend services
- SSL/TLS termination
- Load balancing
- Static asset serving

### scripts/
Utility scripts for:
- Local development setup
- Database initialization
- Service health checks
- Deployment utilities

## Docker Setup

Start local development environment:

```bash
docker compose up -d
```

View running containers:

```bash
docker ps
```

View logs:

```bash
docker compose logs -f
```

Stop containers:

```bash
docker compose down
```

## Nginx Configuration

Nginx acts as the main entry point, routing requests:

```
Client
  ↓
Nginx (port 80 / 443)
  ↓
  ├→ Frontend (Vite dev server or static files)
  ├→ Auth Service (8081)
  ├→ Resume Core Service (8082)
  ├→ Rewrite Service (8083)
  └→ NLP Service (8084)
```

## Environment & Secrets

- `.env` file in root contains all service configuration
- Never commit `.env` with real credentials
- Use `.env.example` as template

See [`.env.example`](../.env.example) for available variables.

## Health Checks

Services expose health endpoints:

```bash
# Check service health
curl http://localhost:8081/health
curl http://localhost:8082/health
curl http://localhost:8083/health
curl http://localhost:8084/health
```

## Production Considerations

- Use managed PostgreSQL (Supabase ✓)
- Enable HTTPS with proper SSL certificates
- Implement database backups
- Configure proper logging and monitoring
- Use container registries (Docker Hub, ECR, GCR)
- Implement API rate limiting
- Set up proper authentication for admin endpoints
