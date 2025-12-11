# Boat Application

A full-stack application for managing boats with JWT authentication and role-based access control (RBAC).

## Tech Stack

- **Backend**: Spring Boot 3.4, Java 21, H2 Database
- **Frontend**: Next.js 14, React 18, TypeScript, Tailwind CSS

## Getting Started

### Prerequisites
- **For local development**: Java 21, Node.js 16+ and npm, Gradle
- **For Docker**: Docker and Docker Compose

### Running with Docker (Recommended)

```bash
docker-compose up --build
```

This will start both backend and frontend:
- Backend: `http://localhost:8080`
- Frontend: `http://localhost:3000`

### Running Locally

**Backend:**
```bash
cd backend
./gradlew bootRun
```
Backend runs on `http://localhost:8080`

**Frontend:**
```bash
cd frontend
npm install
npm run dev
```
Frontend runs on `http://localhost:3000`

## Default Credentials

- **Admin**: `admin` / `admin123`
- **User**: `user1` / `user12345`

## Features

- JWT-based authentication with refresh tokens
- Role-based access control (RBAC)
  - **ADMIN**: Can create, read, update, and delete boats
  - **USER**: Can create, read, and update boats (delete restricted)
- CRUD operations for boats
- RESTful API

## Database

The application uses H2 in-memory database. The database is automatically initialized with default users on startup.

To access the H2 console:
- URL: `http://localhost:8080/h2-console`
- JDBC URL: `jdbc:h2:mem:boatdb`
- Username: `boat`
- Password: (empty)

