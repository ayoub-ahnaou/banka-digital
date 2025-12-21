# banka-digital

A Java Spring Boot backend application for a simple digital banking system (accounts, operations, documents, user authentication and roles).

This README is tailored to the repository layout found in this workspace (Java 21, Spring Boot 4.x, Maven). It includes quick start, configuration, Docker, and development notes.

Contents
- Project summary
- Tech stack
- Quick start (local)
- Environment configuration (.env and system properties)
- Run with Docker / docker-compose
- Build and tests
- API overview (endpoints & auth)
- Configuration reference (important properties / env vars)
- Project structure
- Contributing and developer notes


Project summary

banka-digital is a backend service exposing REST endpoints to manage user accounts, operations (deposit/withdraw/transfer), document uploads and basic role-based authorization for admin and bank agents.

Key behaviors discovered in the codebase:
- Uses Java 21 and Spring Boot 4
- Loads secrets/config from a `.env` file at startup using `java-dotenv` in `BankaDigitalApplication` and copies them to system properties
- JWT authentication (io.jsonwebtoken) with a `JwtUtil` helper and a `JwtAuthenticationFilter`
- Security configuration declares public endpoints (authentication and swagger), and role-restricted endpoints for admin and bank agents
- Spring Data JPA with PostgreSQL (driver dependency present)
- File uploads are saved to `uploads/` by default
- MapStruct + Lombok are used for mapping and boilerplate reduction


Tech stack

- Java 21
- Spring Boot 4
- Spring Data JPA (Hibernate)
- Spring Security with JWT
- PostgreSQL (runtime dependency)
- MapStruct
- Lombok
- java-dotenv for loading `.env` file
- Maven (wrapper provided)


Quick start (local, Windows)

1. Prerequisites
- JDK 21 installed and `JAVA_HOME` set
- Docker (optional, for docker-compose option)
- Maven (you can use the included wrapper `mvnw.cmd`)

2. Create a `.env` file at the project root (example below). The application reads this file on startup and sets system properties used by `application.properties`.

Example `.env`:

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=banka_db
DB_USER=banka_user
DB_PASSWORD=changeme

JWT_SECRET=replace-with-very-long-random-secret-ensure-32-bytes-or-more
JWT_EXPIRATION=86400000 # token ttl in ms (1 day)
JWT_REFRESH_EXPIRATION=2592000000 # optional refresh token ttl in ms (30 days)

UPLOAD_DIR=./uploads
```

3. Start a local PostgreSQL instance and create the DB and user above, or use docker-compose (see Docker section).

4. Build and run with Maven wrapper (Windows PowerShell):

```powershell
# run with embedded devtools (restarts on change)
.\\mvnw.cmd spring-boot:run

# or build a jar and run
.\\mvnw.cmd clean package -DskipTests
java -jar target\banka-digital-0.0.1-SNAPSHOT.jar
```

Application will be available at `http://localhost:8080` (unless configured otherwise).


Run with Docker (recommended for reproducible environment)

The repository includes a `Dockerfile` and `docker-compose.yml` that define two services: `db` (postgres) and `app` (this Spring Boot application).

1. Create a `.env` file in the project root (same as above). The docker-compose file references these environment variables.

2. Start services using Docker Compose (PowerShell):

```powershell
docker-compose up --build
```

This will expose PostgreSQL on port 5432 and the app on port 8080. The app depends on the `db` service.

Notes: the `app` service builds the jar from `target/*.jar`, so ensure you run `.\mvnw.cmd clean package -DskipTests` before `docker-compose up --build` or adapt the Dockerfile to build inside the image.


Configuration (.env, application.properties)

- The application loads a `.env` file using `Dotenv` in `BankaDigitalApplication` and sets the following system properties. These map into `src/main/resources/application.properties` which uses placeholders like `${DB_HOST}`.

Required environment variables (recommended names):
- DB_HOST - Postgres hostname (e.g. localhost)
- DB_PORT - Postgres port (e.g. 5432)
- DB_NAME - Database name
- DB_USER - DB username
- DB_PASSWORD - DB password
- JWT_SECRET - Secret used to sign JWT tokens (must be long enough for HMAC-SHA)
- JWT_EXPIRATION - token TTL in milliseconds (e.g. 86400000 = 1 day)

Optional:
- JWT_REFRESH_EXPIRATION - refresh token TTL in ms
- UPLOAD_DIR - directory used for file uploads (defaults to ./uploads)

application.properties highlights (src/main/resources/application.properties):
- spring.datasource.url=jdbc:postgresql://${DB_HOST}:${DB_PORT}/${DB_NAME}
- spring.datasource.username=${DB_USER}
- spring.datasource.password=${DB_PASSWORD}
- jwt.secret=${JWT_SECRET}
- jwt.expiration=${JWT_EXPIRATION}
- file.upload-dir=${UPLOAD_DIR:./uploads}


API overview

Base URL: /api

Auth
- POST /api/auth/register - register a new user
  - Request: JSON RegisterRequest
  - Response: ApiResponseSuccess with RegisterResponse (201)

- POST /api/auth/login - login
  - Request: JSON LoginRequest
  - Response: ApiResponseSuccess with LoginResponse (contains JWT token)

Accounts
- GET /api/accounts/account - retrieves account for current authenticated user (ROLE_CLIENT or others)

Operations
- POST /api/operations/deposit - deposit for current user (body: DepositRequest)
- POST /api/operations/withdraw - withdraw for current user (body: WithdrawRequest)
- POST /api/operations/transfer - transfer for current user (body: TransferRequest)
- GET /api/operations?status={status} - (ROLE_BANK_AGENT or ROLE_ADMIN required) list operations optionally filtered by status
- PATCH /api/operations/{id}/approve - approve operation (ROLE_BANK_AGENT or ROLE_ADMIN)
- PATCH /api/operations/{id}/reject - reject operation (ROLE_BANK_AGENT or ROLE_ADMIN)

Documents
- POST /api/documents/upload/{operationId} - upload a document for an operation (multipart/form-data)
- GET /api/documents/order/{orderId} - (ROLE_BANK_AGENT or ROLE_ADMIN) get documents for an operation

Security and tokens
- JWT tokens are issued by `JwtUtil` and expected in `Authorization: Bearer {token}` header.
- The app protects endpoints using roles: CLIENT, BANK_AGENT, ADMIN (configured in `SecurityConfig`).
- Public endpoints: `/api/auth/**`, swagger and public resources.

Response format
- Successful responses follow `ApiResponseSuccess<T>` with fields: status, message, timestamp, data
- Errors are returned as `ApiResponseError` with a list of errors


Project structure (key folders)

- src/main/java/com/digital/banka/
  - BankaDigitalApplication.java - main entry (loads .env)
  - config/ - Spring configuration (SecurityConfig, etc.)
  - controller/ - REST controllers (AuthController, AccountController, OperationController, DocumentController)
  - dto/ - request/response DTOs and ApiResponse wrappers
  - service/ - service interfaces and implementations
  - security/ - JWT util, filter, and other security classes
  - repository/ - Spring Data JPA repositories
  - model/ - JPA Entities and enums
  - mapper/ - MapStruct mappers (mapping between entities and DTOs)

Other files:
- Dockerfile - minimal runtime image using Eclipse Temurin JRE 21
- docker-compose.yml - postgres + app composition
- pom.xml - maven build (Java 21, Spring Boot 4, MapStruct, Lombok)


Development notes & tips

- Lombok is used in the codebase. If using an IDE, enable Lombok plugin and annotation processing.
- MapStruct requires annotation processing at compile time (configured in `pom.xml`).
- The `BankaDigitalApplication` expects `.env` variables to exist; missing required values cause a NullPointerException because `Objects.requireNonNull` is used. Make sure `.env` contains all required keys.
- JWT secret must be long enough for HMAC SHA key generation (use a securely-generated random string of sufficient length, e.g. 64+ characters)


Running tests

- Run all tests with the Maven wrapper:

```powershell
.\\mvnw.cmd test
```


Common commands

- Build: `.\\mvnw.cmd clean package`
- Run: `.\\mvnw.cmd spring-boot:run`
- Run jar: `java -jar target\banka-digital-0.0.1-SNAPSHOT.jar`
- Docker compose up: `docker-compose up --build`


Contributing

- Use `feature/*` branch naming and open PRs against `main`.
- Add unit tests for new logic.
- Ensure `mvnw.cmd test` passes before opening PRs.


Appendix: Example .env for development (do not commit)

```
DB_HOST=localhost
DB_PORT=5432
DB_NAME=banka_dev
DB_USER=banka_user
DB_PASSWORD=changeme

JWT_SECRET=verylongrandomsecretthatshouldbeatleast32bytes
JWT_EXPIRATION=86400000
UPLOAD_DIR=./uploads
```


If you'd like, I can:
- add a `README.md` improvements like API example requests and response samples,
- create Postman/Insomnia collection stub,
- add a simple Dockerfile improvement to build the JAR inside the image,
- add a GitHub Actions workflow for CI (build & tests).

