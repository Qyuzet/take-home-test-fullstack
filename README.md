# Task Management App

A fullstack task management application built with Vue.js and Spring Boot, featuring JWT authentication and PostgreSQL database.

## Tech Stack

### Frontend

- Vue.js 3.5.22 (Composition API)
- Vue Router 4.6.3
- Vite 7.1.11
- Tailwind CSS 4.1.17
- shadcn-vue (UI Components)
- Axios 1.13.2

### Backend

- Java 17
- Spring Boot 3.5.7
- Spring Security
- Spring Data JPA
- PostgreSQL
- JWT Authentication (JJWT 0.11.5)
- Maven

### Database

- PostgreSQL (Supabase hosted)

## Features

- User authentication with JWT tokens
- CRUD operations for tasks
- Task fields: Title, Description, Status, Type, Priority
- Client-side form validation
- Responsive dark theme UI with glassmorphism design
- RESTful API architecture
- Secure authentication with stateless sessions

## Prerequisites

- Node.js (v20.19.0 or v22.12.0+)
- Java 17 or higher
- Maven 3.x
- PostgreSQL database

## Project Structure

```
take-home-test-fullstack/
├── backend/                    # Spring Boot backend
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/taskapp/backend/
│   │   │   │   ├── config/           # Security configuration
│   │   │   │   ├── controller/       # REST controllers
│   │   │   │   ├── dto/              # Data transfer objects
│   │   │   │   ├── model/            # JPA entities
│   │   │   │   ├── repository/       # JPA repositories
│   │   │   │   ├── security/         # JWT utilities
│   │   │   │   └── service/          # Business logic
│   │   │   └── resources/
│   │   │       └── application.properties
│   │   └── test/
│   └── pom.xml
│
└── frontend/                   # Vue.js frontend
    ├── src/
    │   ├── assets/             # CSS and static files
    │   ├── components/         # Vue components (shadcn-vue)
    │   ├── router/             # Vue Router configuration
    │   ├── services/           # API service layer
    │   ├── views/              # Page components
    │   ├── App.vue
    │   └── main.js
    ├── package.json
    └── vite.config.js
```

## Installation and Setup

### Backend Setup

1. Navigate to the backend directory:

```bash
cd backend
```

2. Configure database connection in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:postgresql://your-host:5432/your-database
spring.datasource.username=your-username
spring.datasource.password=your-password

jwt.secret=your-secret-key
jwt.expiration=86400000
```

3. Build and run the application:

```bash
# Using Maven wrapper (recommended)
./mvnw clean install
./mvnw spring-boot:run

# Or using Maven directly
mvn clean install
mvn spring-boot:run
```

The backend will start on `http://localhost:8080`

### Frontend Setup

1. Navigate to the frontend directory:

```bash
cd frontend
```

2. Install dependencies:

```bash
npm install
```

3. Configure API endpoint in `src/services/taskService.js` if needed:

```javascript
const API_URL = "http://localhost:8080/api";
```

4. Run the development server:

```bash
npm run dev
```

The frontend will start on `http://localhost:5173`

## Usage

### Login Credentials

The application uses hardcoded credentials for authentication:

- Username: `admin`
- Password: `admin123`

### API Endpoints

#### Authentication

- `POST /api/auth/login` - User login (returns JWT token)

#### Tasks

- `GET /api/tasks` - Get all tasks
- `GET /api/tasks/{id}` - Get task by ID
- `POST /api/tasks` - Create new task
- `PUT /api/tasks/{id}` - Update task
- `DELETE /api/tasks/{id}` - Delete task

### Task Model

```json
{
  "id": 1,
  "title": "Task title",
  "description": "Task description",
  "status": "pending",
  "type": "Feature",
  "priority": "High"
}
```

**Status options:** `pending`, `in_progress`, `completed`

**Type options:** `Bug`, `Feature`, `Documentation`

**Priority options:** `Low`, `Medium`, `High`

## Building for Production

### Backend

```bash
cd backend
./mvnw clean package
java -jar target/backend-0.0.1-SNAPSHOT.jar
```

### Frontend

```bash
cd frontend
npm run build
npm run preview
```

## Architecture

### Backend Architecture

- **Controller Layer**: Handles HTTP requests and responses
- **Service Layer**: Contains business logic
- **Repository Layer**: Data access using Spring Data JPA
- **Security Layer**: JWT-based authentication with Spring Security
- **DTO Layer**: Data transfer objects for API communication

### Frontend Architecture

- **Views**: Page-level components (LoginView, TaskViewNew)
- **Components**: Reusable UI components (shadcn-vue)
- **Services**: API communication layer (taskService)
- **Router**: Client-side routing with navigation guards

### Security

- JWT tokens for stateless authentication
- CORS enabled for cross-origin requests
- Password validation on both client and server
- Protected routes with authentication guards

## Deployment (Free Hosting)

### Backend Deployment (Railway - Free Tier)

1. Create account at [Railway.app](https://railway.app)
2. Click "New Project" → "Deploy from GitHub repo"
3. Select your backend repository
4. Add environment variables:
   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `JWT_SECRET`
   - `JWT_EXPIRATION`
5. Railway will auto-detect Spring Boot and deploy
6. Copy the generated URL (e.g., `https://your-app.railway.app`)

Alternative: **Render.com** (also free tier available)

### Frontend Deployment (Vercel - Free)

1. Create account at [Vercel.com](https://vercel.com)
2. Click "New Project" → Import your frontend repository
3. Configure build settings:
   - Framework Preset: Vite
   - Build Command: `npm run build`
   - Output Directory: `dist`
4. Add environment variable:
   - `VITE_API_URL` = `https://your-backend-url.railway.app/api`
5. Deploy

Your app will be live at `https://your-app.vercel.app`

### Environment Variables

Frontend `.env` file:

```
VITE_API_URL=https://your-backend-url.railway.app/api
```

Backend `application.properties` (use Railway environment variables):

```
spring.datasource.url=${SPRING_DATASOURCE_URL}
spring.datasource.username=${SPRING_DATASOURCE_USERNAME}
spring.datasource.password=${SPRING_DATASOURCE_PASSWORD}
jwt.secret=${JWT_SECRET}
jwt.expiration=${JWT_EXPIRATION}
```

## Development Notes

- Backend uses Hibernate with `ddl-auto=update` for automatic schema updates
- Frontend uses Vite for fast development and hot module replacement
- UI components from shadcn-vue with Tailwind CSS for styling
- Axios for HTTP requests with JWT token injection
- Vue Router for client-side navigation with authentication guards
- Environment variables for flexible deployment across environments
