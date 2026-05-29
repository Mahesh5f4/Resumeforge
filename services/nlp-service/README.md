# NLP Service

Natural Language Processing service for resume analysis and ATS optimization.

**Technology:** Python 3.11+ + FastAPI

**Port:** 8084

## Overview

Handles:
- Resume text analysis and parsing
- ATS keyword and format detection
- Resume scoring and recommendations
- Skill extraction and categorization
- Content optimization suggestions

## Quick Start

```bash
# Create and activate virtual environment
python -m venv venv

# Activate virtual environment
# On Windows:
venv\Scripts\activate
# On macOS/Linux:
source venv/bin/activate

# Install dependencies
pip install -r requirements.txt

# Run the service
uvicorn main:app --reload --port 8084
```

Service runs on `http://localhost:8084`.

API documentation available at `http://localhost:8084/docs`.

## Project Structure

```
nlp-service/
├── main.py                 # FastAPI application entry point
├── requirements.txt        # Python dependencies
├── routers/                # API route handlers
├── models/                 # Pydantic models (validation)
├── services/               # Core business logic
├── utils/                  # Utility functions
└── tests/                  # Unit tests
```

## Environment Variables

```env
GROQ_API_KEY=your-api-key
PYTHON_ENV=development
NLP_SERVICE_PORT=8084
```

## Key Dependencies

- `fastapi` — Web framework
- `uvicorn` — ASGI server
- `pydantic` — Data validation
- `groq` — Groq API client
- `python-multipart` — Form data parsing

Update dependencies:

```bash
pip freeze > requirements.txt
```

## API Documentation

FastAPI provides automatic interactive API documentation:

- **Swagger UI:** `http://localhost:8084/docs`
- **ReDoc:** `http://localhost:8084/redoc`

## Development

Run with auto-reload on code changes:

```bash
uvicorn main:app --reload --port 8084
```

Run tests:

```bash
pytest tests/
```

## Production

Use a production ASGI server:

```bash
pip install gunicorn
gunicorn -w 4 -k uvicorn.workers.UvicornWorker main:app
```
