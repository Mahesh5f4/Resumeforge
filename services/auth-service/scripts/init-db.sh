#!/bin/bash

# Database initialization script for ResumeForge Auth Service

set -e

# Colors for output
GREEN='\033[0;32m'
BLUE='\033[0;34m'
RED='\033[0;31m'
NC='\033[0m'

echo -e "${BLUE}ResumeForge Auth Service - Database Initialization${NC}"

# Check if PostgreSQL is running
if ! command -v psql &> /dev/null; then
    echo -e "${RED}Error: psql command not found. Please install PostgreSQL client.${NC}"
    exit 1
fi

# Database configuration
DB_HOST=${1:-localhost}
DB_PORT=${2:-5432}
DB_USER=${3:-postgres}
DB_NAME=${4:-resumeforge_auth}

echo -e "${BLUE}Connecting to PostgreSQL at ${DB_HOST}:${DB_PORT}...${NC}"

# Create database
echo -e "${BLUE}Creating database: ${DB_NAME}${NC}"
psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -tc \
    "SELECT 1 FROM pg_database WHERE datname = '${DB_NAME}'" | grep -q 1 && \
    echo -e "${GREEN}Database ${DB_NAME} already exists${NC}" || \
    psql -h "$DB_HOST" -p "$DB_PORT" -U "$DB_USER" -c "CREATE DATABASE ${DB_NAME};"

echo -e "${GREEN}✓ Database initialization complete${NC}"
