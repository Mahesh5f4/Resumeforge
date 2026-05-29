# ResumeForge

ATS Resume Optimization SaaS — A clean, scalable monorepo for optimizing resumes to pass applicant tracking systems.

## 🎯 Project Overview

ResumeForge is an MVP-focused SaaS application designed to help job seekers optimize their resumes for ATS (Applicant Tracking System) compatibility. The platform analyzes resumes, identifies ATS-blocking issues, and provides AI-powered rewrites to improve job application success rates.

## 🏗️ Architecture Overview

ResumeForge follows a modular monorepo architecture with clear separation of concerns:

```
Frontend (React 18 + Vite)
        ↓
    Nginx Reverse Proxy
        ↓
┌───────────────────────────────────────┐
│         Microservices (Backend)       │
├──────────────────────────────────────┤
│ • Auth Service (Spring Boot)          │
│ • Resume Core Service (Spring Boot)   │
│ • Rewrite Service (Spring Boot)       │
│ • NLP Service (FastAPI)               │
└───────────────────────────────────────┘
        ↓
┌───────────────────────────────────────┐
│      Shared Infrastructure            │
├──────────────────────────────────────┤
│ • Supabase PostgreSQL (cloud)         │
│ • Redis (local cache)                 │
└───────────────────────────────────────┘
```

## 📁 Monorepo Structure

```plaintext
resumeforge/
├── frontend/                   # React 18 + Vite frontend application
│
├── services/                   # Backend microservices
│   ├── auth-service/          # Authentication & authorization (Spring Boot)
│   ├── resume-core-service/   # Resume parsing & analysis (Spring Boot)
│   ├── rewrite-service/       # AI-powered resume rewrites (Spring Boot)
│   └── nlp-service/           # NLP & text analysis (FastAPI)
│
├── infrastructure/
│   ├── docker/                # Docker configurations & Dockerfiles
│   ├── nginx/                 # Nginx reverse proxy config
│   └── scripts/               # Utility scripts for development
│
├── docs/                       # Project documentation
│
├── docker-compose.yml         # Local development environment
├── .env.example               # Environment variables template
├── .gitignore                 # Git ignore rules
└── README.md                  # This file
```

## 🛠️ Tech Stack

### Frontend
- **React 18** — UI library
- **Vite** — Fast build tool and dev server
- **TailwindCSS** — Utility-first CSS framework

### Backend Services
- **Java 21** — JDK version
- **Spring Boot 3** — Framework for Java services
- **Maven** — Dependency management and build automation

### NLP Service
- **FastAPI** — Modern Python web framework
- **Python 3.11+** — Programming language

### Infrastructure & Data
- **Docker** — Containerization
- **Docker Compose** — Local orchestration
- **Redis 7** — In-memory cache and session store
- **Supabase PostgreSQL** — Cloud-hosted relational database
- **Nginx** — Reverse proxy and load balancing

## 📋 Prerequisites

Before getting started, ensure you have installed:

- **Java 21** or later ([Download](https://www.oracle.com/java/technologies/downloads/#java21))
- **Node.js 18+** ([Download](https://nodejs.org/))
- **Python 3.11+** ([Download](https://www.python.org/downloads/))
- **Docker Desktop** ([Download](https://www.docker.com/products/docker-desktop))
- **Git** ([Download](https://git-scm.com/))

### Verify Installations

```bash
# Check Java version
java -version

# Check Node.js and npm
node --version
npm --version

# Check Python
python --version

# Check Docker
docker --version
docker compose --version
```

## 🚀 Local Development Setup

### 1. Clone the Repository

```bash
git clone https://github.com/your-org/resumeforge.git
cd resumeforge
```

### 2. Environment Configuration

Copy `.env.example` to `.env` and populate with your credentials:

```bash
cp .env.example .env
```

Update the following in `.env`:
- Supabase credentials (project URL and keys)
- Supabase database URL
- JWT secret (generate a strong random value)
- GROQ API key (for NLP service)

### 3. Start Redis (Docker)

Start the Redis container for local caching:

```bash
docker compose up -d
```

Verify Redis is running:

```bash
docker ps
```

You should see `resumeforge-redis` container running.

### 4. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs on `http://localhost:5173` (Vite default).

### 5. Backend Services Setup

Each Java service follows the same pattern:

```bash
cd services/auth-service
mvn clean install
mvn spring-boot:run
```

Or build and run the JAR:

```bash
mvn clean package
java -jar target/auth-service-*.jar
```

### 6. NLP Service Setup

```bash
cd services/nlp-service
python -m venv venv

# Activate virtual environment
# On Windows
venv\Scripts\activate
# On macOS/Linux
source venv/bin/activate

pip install -r requirements.txt
uvicorn main:app --reload --port 8084
```

## 🐳 Docker & Deployment

### Start All Services

```bash
docker compose up -d
```

### View Running Containers

```bash
docker ps
```

### View Logs

```bash
# All services
docker compose logs -f

# Specific service
docker compose logs -f redis
```

### Stop All Services

```bash
docker compose down
```

### Stop Services and Remove Volumes

```bash
docker compose down -v
```

## 📚 Service Overview

### Auth Service
- User registration and login
- JWT token generation and validation
- Role-based access control (RBAC)
- Session management via Redis

**Port:** 8081

### Resume Core Service
- Resume upload and parsing
- Resume metadata storage
- Retrieval and listing
- Resume version history

**Port:** 8082

### Rewrite Service
- AI-powered resume section rewrites
- Suggestion engine
- Change tracking and comparison

**Port:** 8083

### NLP Service
- ATS keyword analysis
- Resume scoring and recommendations
- Format issue detection
- Skill extraction

**Port:** 8084

## 🔄 Development Workflow

### Adding a New Dependency

**Java Service:**
```bash
# Add to pom.xml
cd services/your-service
mvn dependency:tree  # View current dependencies
```

**Node.js Frontend:**
```bash
cd frontend
npm install <package-name>
```

**Python NLP Service:**
```bash
cd services/nlp-service
pip install <package-name>
pip freeze > requirements.txt
```

### Building for Production

```bash
# Frontend
cd frontend
npm run build  # Creates dist/ folder

# Java Services
cd services/your-service
mvn clean package -DskipTests

# NLP Service
# Uses uvicorn in production with gunicorn or similar
```

## 📝 Environment Variables

See [`.env.example`](.env.example) for all available environment variables and their descriptions.

**⚠️ Important:** Never commit `.env` file with real credentials. Only `.env.example` should be versioned.

## 🤝 Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Commit changes: `git commit -m 'Add feature description'`
3. Push to branch: `git push origin feature/your-feature`
4. Submit a pull request

## 📖 Documentation

Additional documentation is available in the [docs/](docs/) directory:

- Architecture decisions
- API specifications
- Database schema
- Deployment guides

## 🔒 Security Notes

- Store all secrets in `.env` (`.env` is gitignored)
- Use HTTPS in production
- Rotate JWT secrets regularly
- Enable CORS only for trusted domains
- Validate all user inputs
- Keep dependencies updated: `npm audit`, `pip audit`, `mvn dependency-check:check`

## 🐛 Troubleshooting

### Redis Connection Issues
```bash
# Check if Redis is running
docker ps | grep redis

# Restart Redis
docker compose restart redis

# Check Redis logs
docker compose logs redis
```

### Port Already in Use
```bash
# Find process using port (e.g., 8080)
netstat -ano | findstr :8080  # Windows
lsof -i :8080                  # macOS/Linux

# Kill process and restart
```

### Java Version Mismatch
```bash
# Verify Java version matches requirement (21)
java -version

# Set JAVA_HOME if needed
export JAVA_HOME=/path/to/jdk21
```

## 📄 License

[Your License Here]

## 📧 Support

For issues, questions, or feature requests, please open an issue on GitHub.

---

**Last Updated:** May 2026
