# Docker Configuration

Docker configuration and build contexts for ResumeForge services.

## Structure

```
docker/
├── Dockerfile.backend      # Template for Java services
├── Dockerfile.nlp          # Template for FastAPI service
└── .dockerignore           # Files to exclude from Docker build
```

## Building Containers

### Java Backend Services

```bash
docker build -f infrastructure/docker/Dockerfile.backend \
  -t resumeforge/auth-service:latest \
  services/auth-service/
```

### NLP Service

```bash
docker build -f infrastructure/docker/Dockerfile.nlp \
  -t resumeforge/nlp-service:latest \
  services/nlp-service/
```

## Running Containers

See root-level `docker-compose.yml` for the complete stack definition.

## Best Practices

- Keep images small (use alpine base images when possible)
- Use multi-stage builds for production
- Don't run as root inside containers
- Map volumes for development
- Use environment variables for configuration
