# Infrastructure Scripts

Utility scripts for ResumeForge development and deployment.

## Purpose

- Local development environment setup
- Database initialization and migration
- Service health checks
- Container management
- Deployment helpers

## Scripts

Add useful shell scripts here as the project grows:

### Example Scripts (to be created)

- `setup-local.sh` — Initial development environment setup
- `health-check.sh` — Check all service health endpoints
- `db-init.sh` — Initialize database schema
- `docker-cleanup.sh` — Clean up Docker resources

## Running Scripts

On macOS/Linux:

```bash
./scripts/setup-local.sh
```

On Windows (PowerShell):

```powershell
.\scripts\setup-local.ps1
```

## Adding New Scripts

1. Create script file in this directory
2. Add description at top of file
3. Make executable: `chmod +x script-name.sh`
4. Document usage in this README
