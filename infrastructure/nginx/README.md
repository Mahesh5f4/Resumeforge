# Nginx Configuration

Nginx reverse proxy configuration for ResumeForge.

## Overview

Nginx serves as the main entry point for the application:

- Routes requests to appropriate backend services
- Hosts frontend static assets
- Handles SSL/TLS termination (production)
- Manages CORS headers
- Provides load balancing capabilities

## Configuration Files

- `nginx.conf` — Main Nginx configuration
- `conf.d/` — Service-specific routing configurations

## Local Development

Nginx is not required for local development. Services run on separate ports and communicate directly.

## Production Deployment

Nginx configuration should include:

```nginx
server {
    listen 80;
    server_name yourdomain.com;

    # Redirect to HTTPS
    return 301 https://$server_name$request_uri;
}

server {
    listen 443 ssl;
    server_name yourdomain.com;

    ssl_certificate /path/to/cert.pem;
    ssl_certificate_key /path/to/key.pem;

    # Frontend
    location / {
        root /var/www/html;
        index index.html;
        try_files $uri $uri/ /index.html;
    }

    # API routes
    location /api/auth {
        proxy_pass http://auth-service:8081;
    }

    location /api/resume {
        proxy_pass http://resume-core-service:8082;
    }

    # ... more routes
}
```

## Development Testing

To test Nginx locally:

```bash
docker run -d -p 80:80 -v $(pwd)/infrastructure/nginx:/etc/nginx/conf.d:ro nginx:alpine
```

Then access `http://localhost`.
