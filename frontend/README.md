# ResumeForge Frontend

React 18 + Vite frontend application for ResumeForge.

## Overview

Modern, fast, and responsive web interface built with React 18 and Vite. Uses TailwindCSS for styling and connects to backend services via REST API.

## Quick Start

```bash
npm install
npm run dev
```

Frontend runs on `http://localhost:5173`.

## Project Structure

```
frontend/
├── src/
│   ├── components/      # Reusable UI components
│   ├── pages/          # Page components
│   ├── hooks/          # Custom React hooks
│   ├── utils/          # Utility functions
│   ├── api/            # API client code
│   ├── styles/         # Global and component styles
│   ├── App.jsx         # Main app component
│   └── main.jsx        # Entry point
├── public/             # Static assets
├── vite.config.js      # Vite configuration
├── tailwind.config.js  # TailwindCSS config
└── package.json        # Dependencies
```

## Available Scripts

- `npm run dev` — Start development server
- `npm run build` — Build for production
- `npm run preview` — Preview production build
- `npm run lint` — Run ESLint

## Environment Variables

Create `.env.local` file:

```env
VITE_API_URL=http://localhost:8080
VITE_ENV=development
```

## Technologies

- React 18
- Vite
- TailwindCSS
- Axios (HTTP client)
- React Router (navigation)
