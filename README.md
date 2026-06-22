# 💬 Chat Backend

A real-time chat application backend built with **Spring Boot**, featuring WebSocket-based messaging, JWT authentication, and MongoDB persistence.

---

## 🚀 Tech Stack

| Layer | Technology |
|---|---|
| Framework | Spring Boot 3.5.x |
| Language | Java 17 |
| Database | MongoDB Atlas |
| Authentication | JWT (JJWT 0.12.5) |
| Real-time Messaging | WebSocket + STOMP + SockJS |
| Security | Spring Security |
| Build Tool | Maven |
| Containerization | Docker |

---

## ✨ Features

- 🔐 **JWT Authentication** — Stateless auth with secure HTTP-only cookies (SameSite=None, Secure)
- 💬 **Real-time Messaging** — WebSocket with STOMP protocol and SockJS fallback
- 🗄️ **MongoDB Persistence** — All messages and users stored in MongoDB Atlas
- 👤 **User Management** — Signup, login, logout, and profile endpoints
- 🛡️ **Spring Security** — Route-level access control with a custom JWT filter
- 🐳 **Docker Support** — Fully containerized and ready to deploy

---

## 📁 Project Structure

```
src/main/java/com/chatapp/
├── config/
│   ├── CorsConfig.java          # CORS configuration
│   ├── SecurityConfig.java      # Spring Security filter chain
│   └── WebSocketConfig.java     # STOMP broker & endpoint setup
├── controller/
│   ├── AuthController.java      # Signup, Login, Logout endpoints
│   ├── MessageController.java   # Fetch chat history
│   └── UserController.java      # Current user & all users endpoints
├── dto/                         # Request/Response DTOs
├── model/
│   ├── User.java                # User MongoDB document
│   └── Message.java             # Message MongoDB document
├── repository/                  # Spring Data MongoDB repositories
├── security/
│   ├── JwtUtil.java             # JWT generation & validation
│   └── JwtAuthenticationFilter.java  # Per-request JWT filter
├── service/
│   └── AuthService.java         # Signup & login business logic
└── websocket/
    ├── ChatWebSocketController.java  # Handles /app/send messages
    └── ChatMessageDto.java           # WebSocket message payload
```

---

## 🔌 API Endpoints

### Auth — `/user`

| Method | Endpoint | Auth Required | Description |
|--------|----------|:---:|-------------|
| `POST` | `/user/signup` | ❌ | Register a new user |
| `POST` | `/user/login` | ❌ | Login and receive JWT cookie |
| `GET` | `/user/logout` | ✅ | Clear the auth cookie |
| `GET` | `/user/me` | ✅ | Get current logged-in user |
| `GET` | `/user/all` | ✅ | Get all users (except self) |

### Messages — `/message`

| Method | Endpoint | Auth Required | Description |
|--------|----------|:---:|-------------|
| `GET` | `/message/{receiverId}` | ✅ | Fetch conversation history with a user |

### WebSocket — `/chat`

| Destination | Direction | Description |
|---|---|---|
| `/chat` | Client → Server | SockJS/STOMP connection endpoint |
| `/app/send` | Client → Server | Send a new message |
| `/topic/messages/{userId}` | Server → Client | Receive messages in real-time |

---

## 🔐 Authentication Flow

1. **Signup / Login** — Client sends credentials to `/user/signup` or `/user/login`.
2. **JWT Issued** — Server generates a JWT token and sets it as a `chat-token` HTTP-only cookie.
3. **Subsequent Requests** — The `JwtAuthenticationFilter` reads the cookie, validates the token, and populates the Spring Security context.
4. **Logout** — Client calls `/user/logout`, which clears the cookie by setting `maxAge=0`.

---

## ⚙️ Configuration

Update `src/main/resources/application.yaml` or set the following environment variables:

```yaml
spring:
  data:
    mongodb:
      uri: <your-mongodb-connection-string>

server:
  port: ${PORT:8080}

jwt:
  secret: <your-jwt-secret-key>        # Must be at least 256 bits for HS256
  expiration: 1296000000               # 15 days in milliseconds
```

> ⚠️ **Never commit real credentials.** Add `.env` and sensitive configs to `.gitignore`.

---

## 🏃 Running Locally

### Prerequisites

- Java 17+
- Maven 3.8+
- MongoDB Atlas URI (or a local MongoDB instance)

### Steps

```bash
# 1. Clone the repository
git clone <repository-url>
cd chat-backend

# 2. Update application.yaml with your MongoDB URI and JWT secret

# 3. Build and run
./mvnw spring-boot:run
```

The server starts on **http://localhost:8080**.

---

## 🐳 Docker

### Build & Run

```bash
# Build the Docker image
docker build -t chat-backend .

# Run the container
docker run -p 8080:8080 \
  -e SPRING_DATA_MONGODB_URI=<your-mongodb-uri> \
  chat-backend
```

### Dockerfile Overview

```dockerfile
FROM eclipse-temurin:17-jdk
WORKDIR /app
COPY . .
RUN chmod +x mvnw && ./mvnw clean package -DskipTests
EXPOSE 8080
CMD ["java", "-jar", "target/chat-backend-0.0.1-SNAPSHOT.jar"]
```

---

## 🛡️ Security Notes

- CSRF is **disabled** (stateless JWT-based API).
- CORS is configured via `CorsConfig.java`.
- Auth cookies are `HttpOnly`, `Secure`, and `SameSite=None` (required for cross-origin deployments).
- Public routes: `/user/login`, `/user/signup` — all other routes require a valid JWT.

---

## 📦 Dependencies

| Dependency | Purpose |
|---|---|
| `spring-boot-starter-web` | REST API |
| `spring-boot-starter-websocket` | WebSocket + STOMP |
| `spring-boot-starter-data-mongodb` | MongoDB integration |
| `spring-boot-starter-security` | Authentication & authorization |
| `spring-boot-starter-validation` | Request validation |
| `jjwt-api / jjwt-impl / jjwt-jackson` | JWT generation & parsing |
| `lombok` | Boilerplate reduction |

---