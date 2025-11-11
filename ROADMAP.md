# 🗓️ DayMate Backend - Development Roadmap

## 📊 Current Status

### ✅ Completed Features
- [x] User registration with password encryption (BCrypt)
- [x] Task CRUD operations
- [x] Notes CRUD operations with tags
- [x] Task-Notes linking
- [x] User-scoped data isolation
- [x] Input validation
- [x] Database models and relationships
- [x] RESTful API endpoints

### ⚠️ Current Limitations
- Using `X-User-Id` header for authentication (temporary)
- No JWT token-based authentication
- No login endpoint
- No reminder scheduler
- No email sending implementation
- No proper exception handling
- No configuration classes

---

## 🎯 Phase 1: Authentication & Security (HIGH PRIORITY)

### 1.1 Add JWT Dependencies
**Status:** Not Started  
**Priority:** 🔴 Critical  
**Estimated Time:** 15 minutes

**Tasks:**
- [ ] Add JWT library to `pom.xml`
  ```xml
  <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-api</artifactId>
      <version>0.12.3</version>
  </dependency>
  <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-impl</artifactId>
      <version>0.12.3</version>
      <scope>runtime</scope>
  </dependency>
  <dependency>
      <groupId>io.jsonwebtoken</groupId>
      <artifactId>jjwt-jackson</artifactId>
      <version>0.12.3</version>
      <scope>runtime</scope>
  </dependency>
  ```

**Files to Modify:**
- `pom.xml`

---

### 1.2 Create JWT Utility Service
**Status:** Not Started  
**Priority:** 🔴 Critical  
**Estimated Time:** 1-2 hours

**Tasks:**
- [ ] Create `service/JwtService.java`
  - Methods: `generateToken()`, `extractUsername()`, `validateToken()`, `extractExpiration()`
- [ ] Add JWT secret key to `application.properties`
- [ ] Add JWT expiration time configuration

**Files to Create:**
- `src/main/java/com/daymate/backend/service/JwtService.java`

**Files to Modify:**
- `src/main/resources/application.properties`

---

### 1.3 Create Authentication DTOs
**Status:** Not Started  
**Priority:** 🔴 Critical  
**Estimated Time:** 30 minutes

**Tasks:**
- [ ] Create `dto/LoginRequest.java` (email, password)
- [ ] Create `dto/AuthResponse.java` (token, user info)
- [ ] Create `dto/ExceptionResponse.java` (for error handling)

**Files to Create:**
- `src/main/java/com/daymate/backend/dto/LoginRequest.java`
- `src/main/java/com/daymate/backend/dto/AuthResponse.java`
- `src/main/java/com/daymate/backend/dto/ExceptionResponse.java`

---

### 1.4 Implement Login Endpoint
**Status:** Not Started  
**Priority:** 🔴 Critical  
**Estimated Time:** 1 hour

**Tasks:**
- [ ] Add `login()` method to `UserService`
  - Validate email/password
  - Return JWT token
- [ ] Add `POST /api/users/login` endpoint to `UserController`
- [ ] Handle authentication exceptions

**Files to Modify:**
- `src/main/java/com/daymate/backend/service/UserService.java`
- `src/main/java/com/daymate/backend/controllers/UserController.java`

---

### 1.5 Create Security Configuration
**Status:** Not Started  
**Priority:** 🔴 Critical  
**Estimated Time:** 2-3 hours

**Tasks:**
- [ ] Create `config/SecurityConfig.java`
  - Configure JWT authentication filter
  - Set public endpoints (register, login)
  - Secure all other endpoints
  - Configure CORS
- [ ] Create `config/JwtAuthenticationFilter.java`
  - Extract token from Authorization header
  - Validate token
  - Set authentication in SecurityContext
- [ ] Create `config/JwtAuthenticationEntryPoint.java` (for unauthorized access)

**Files to Create:**
- `src/main/java/com/daymate/backend/config/SecurityConfig.java`
- `src/main/java/com/daymate/backend/config/JwtAuthenticationFilter.java`
- `src/main/java/com/daymate/backend/config/JwtAuthenticationEntryPoint.java`

---

### 1.6 Update Controllers to Use JWT
**Status:** Not Started  
**Priority:** 🔴 Critical  
**Estimated Time:** 1-2 hours

**Tasks:**
- [ ] Remove `X-User-Id` header from all controllers
- [ ] Extract user from `SecurityContext` or `@AuthenticationPrincipal`
- [ ] Update `TaskController`, `NotesController`, `UserController`

**Files to Modify:**
- `src/main/java/com/daymate/backend/controllers/TaskController.java`
- `src/main/java/com/daymate/backend/controllers/NotesController.java`
- `src/main/java/com/daymate/backend/controllers/UserController.java`
- `src/main/java/com/daymate/backend/service/TaskService.java`
- `src/main/java/com/daymate/backend/service/NotesService.java`

---

## 📧 Phase 2: Email Reminder System (MEDIUM PRIORITY)

### 2.1 Complete Email Configuration
**Status:** Not Started  
**Priority:** 🟡 Medium  
**Estimated Time:** 30 minutes

**Tasks:**
- [ ] Complete email configuration in `application.properties`
  - Add `spring.mail.username`
  - Add `spring.mail.password`
  - Add `spring.mail.port`
  - Add `spring.mail.properties.mail.smtp.auth`
  - Add `spring.mail.properties.mail.smtp.starttls.enable`

**Files to Modify:**
- `src/main/resources/application.properties`

---

### 2.2 Create Email Service
**Status:** Not Started  
**Priority:** 🟡 Medium  
**Estimated Time:** 1-2 hours

**Tasks:**
- [ ] Create `service/EmailService.java`
  - Method: `sendTaskReminder(Task task, User user)`
  - Use `JavaMailSender` to send emails
  - Create HTML email template for task reminders

**Files to Create:**
- `src/main/java/com/daymate/backend/service/EmailService.java`

---

### 2.3 Create Reminder Scheduler
**Status:** Not Started  
**Priority:** 🟡 Medium  
**Estimated Time:** 2-3 hours

**Tasks:**
- [ ] Enable scheduling in `BackendApplication.java` (`@EnableScheduling`)
- [ ] Create `scheduler/ReminderScheduler.java`
  - `@Scheduled` method to run periodically (e.g., every 15 minutes)
  - Query tasks with due dates in next 24 hours
  - Filter tasks where `reminderSent = false`
  - Send email reminders
  - Update `reminderSent = true` after sending
- [ ] Add configuration for reminder time window (e.g., 24 hours before due date)

**Files to Create:**
- `src/main/java/com/daymate/backend/scheduler/ReminderScheduler.java`

**Files to Modify:**
- `src/main/java/com/daymate/backend/BackendApplication.java`
- `src/main/resources/application.properties`

---

## 🛡️ Phase 3: Error Handling & Validation (MEDIUM PRIORITY)

### 3.1 Global Exception Handler
**Status:** Not Started  
**Priority:** 🟡 Medium  
**Estimated Time:** 1-2 hours

**Tasks:**
- [ ] Create `exception/GlobalExceptionHandler.java`
  - Handle `RuntimeException` (user not found, etc.)
  - Handle validation errors
  - Handle authentication/authorization errors
  - Return consistent error response format

**Files to Create:**
- `src/main/java/com/daymate/backend/exception/GlobalExceptionHandler.java`
- `src/main/java/com/daymate/backend/exception/ResourceNotFoundException.java`
- `src/main/java/com/daymate/backend/exception/EmailAlreadyExistsException.java`
- `src/main/java/com/daymate/backend/exception/InvalidCredentialsException.java`

**Files to Modify:**
- `src/main/java/com/daymate/backend/service/UserService.java` (use custom exceptions)

---

### 3.2 Improve Validation
**Status:** Not Started  
**Priority:** 🟡 Medium  
**Estimated Time:** 1 hour

**Tasks:**
- [ ] Add validation annotations to `TaskRequest`
- [ ] Add validation annotations to `NotesRequest`
- [ ] Add custom validation messages

**Files to Modify:**
- `src/main/java/com/daymate/backend/dto/TaskRequest.java`
- `src/main/java/com/daymate/backend/dto/NotesRequest.java`

---

## 🧪 Phase 4: Testing (MEDIUM PRIORITY)

### 4.1 Unit Tests
**Status:** Not Started  
**Priority:** 🟡 Medium  
**Estimated Time:** 4-6 hours

**Tasks:**
- [ ] Write tests for `UserService`
- [ ] Write tests for `TaskService`
- [ ] Write tests for `NotesService`
- [ ] Write tests for `JwtService`
- [ ] Write tests for `EmailService`

**Files to Create:**
- `src/test/java/com/daymate/backend/service/UserServiceTest.java`
- `src/test/java/com/daymate/backend/service/TaskServiceTest.java`
- `src/test/java/com/daymate/backend/service/NotesServiceTest.java`
- `src/test/java/com/daymate/backend/service/JwtServiceTest.java`

---

### 4.2 Integration Tests
**Status:** Not Started  
**Priority:** 🟡 Medium  
**Estimated Time:** 3-4 hours

**Tasks:**
- [ ] Write integration tests for controllers
- [ ] Test authentication flow
- [ ] Test protected endpoints

**Files to Create:**
- `src/test/java/com/daymate/backend/controllers/UserControllerTest.java`
- `src/test/java/com/daymate/backend/controllers/TaskControllerTest.java`
- `src/test/java/com/daymate/backend/controllers/NotesControllerTest.java`

---

## 🚀 Phase 5: Advanced Features (LOW PRIORITY)

### 5.1 Task Filtering & Sorting
**Status:** Not Started  
**Priority:** 🟢 Low  
**Estimated Time:** 2-3 hours

**Tasks:**
- [ ] Add query parameters to `GET /api/tasks`
  - Filter by: `urgency`, `completed`, `dueDate`
  - Sort by: `dueDate`, `urgency`, `createdAt`
- [ ] Update `TaskService` and `TaskRepository`

**Files to Modify:**
- `src/main/java/com/daymate/backend/controllers/TaskController.java`
- `src/main/java/com/daymate/backend/service/TaskService.java`
- `src/main/java/com/daymate/backend/repository/TaskRepository.java`

---

### 5.2 Notes Search & Filtering
**Status:** Not Started  
**Priority:** 🟢 Low  
**Estimated Time:** 2-3 hours

**Tasks:**
- [ ] Add search by tags endpoint
- [ ] Add full-text search for note content
- [ ] Add filtering by linked task

**Files to Modify:**
- `src/main/java/com/daymate/backend/controllers/NotesController.java`
- `src/main/java/com/daymate/backend/service/NotesService.java`
- `src/main/java/com/daymate/backend/repository/NotesRepository.java`

---

### 5.3 AI Integration (Optional)
**Status:** Not Started  
**Priority:** 🟢 Low  
**Estimated Time:** 4-6 hours

**Tasks:**
- [ ] Research AI API (OpenAI, Anthropic, etc.)
- [ ] Create `service/AIService.java`
- [ ] Add endpoints:
  - `POST /api/notes/{id}/summarize`
  - `POST /api/notes/{id}/rephrase`
- [ ] Add API key configuration

**Files to Create:**
- `src/main/java/com/daymate/backend/service/AIService.java`

**Files to Modify:**
- `src/main/java/com/daymate/backend/controllers/NotesController.java`
- `pom.xml` (add HTTP client dependency)
- `src/main/resources/application.properties`

---

### 5.4 User Profile Management
**Status:** Not Started  
**Priority:** 🟢 Low  
**Estimated Time:** 2-3 hours

**Tasks:**
- [ ] Add `PUT /api/users/{id}` endpoint (update profile)
- [ ] Add `PUT /api/users/{id}/password` endpoint (change password)
- [ ] Add password validation

**Files to Modify:**
- `src/main/java/com/daymate/backend/controllers/UserController.java`
- `src/main/java/com/daymate/backend/service/UserService.java`
- `src/main/java/com/daymate/backend/dto/UpdateUserRequest.java`
- `src/main/java/com/daymate/backend/dto/ChangePasswordRequest.java`

---

## 📝 Phase 6: Documentation & Deployment (LOW PRIORITY)

### 6.1 API Documentation
**Status:** Not Started  
**Priority:** 🟢 Low  
**Estimated Time:** 2-3 hours

**Tasks:**
- [ ] Add Swagger/OpenAPI documentation
- [ ] Document all endpoints
- [ ] Add request/response examples

**Files to Create/Modify:**
- Add Swagger dependency to `pom.xml`
- Create `config/OpenApiConfig.java`

---

### 6.2 Deployment Configuration
**Status:** Not Started  
**Priority:** 🟢 Low  
**Estimated Time:** 2-3 hours

**Tasks:**
- [ ] Create `Dockerfile`
- [ ] Create `docker-compose.yml` (for local development)
- [ ] Add production profile configuration
- [ ] Environment variable setup

**Files to Create:**
- `Dockerfile`
- `docker-compose.yml`
- `src/main/resources/application-prod.properties`

---

## 📋 Implementation Order Recommendation

### Sprint 1 (Week 1): Authentication
1. Phase 1.1 - Add JWT Dependencies
2. Phase 1.2 - Create JWT Utility Service
3. Phase 1.3 - Create Authentication DTOs
4. Phase 1.4 - Implement Login Endpoint
5. Phase 1.5 - Create Security Configuration
6. Phase 1.6 - Update Controllers to Use JWT

### Sprint 2 (Week 2): Reminders & Error Handling
1. Phase 2.1 - Complete Email Configuration
2. Phase 2.2 - Create Email Service
3. Phase 2.3 - Create Reminder Scheduler
4. Phase 3.1 - Global Exception Handler
5. Phase 3.2 - Improve Validation

### Sprint 3 (Week 3): Testing & Polish
1. Phase 4.1 - Unit Tests
2. Phase 4.2 - Integration Tests
3. Phase 5.1 - Task Filtering & Sorting
4. Phase 5.2 - Notes Search & Filtering

### Sprint 4 (Week 4): Advanced Features
1. Phase 5.3 - AI Integration (if needed)
2. Phase 5.4 - User Profile Management
3. Phase 6.1 - API Documentation
4. Phase 6.2 - Deployment Configuration

---

## 🔧 Dependencies to Add

```xml
<!-- JWT -->
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-api</artifactId>
    <version>0.12.3</version>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-impl</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>io.jsonwebtoken</groupId>
    <artifactId>jjwt-jackson</artifactId>
    <version>0.12.3</version>
    <scope>runtime</scope>
</dependency>

<!-- Swagger/OpenAPI (Optional) -->
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>2.3.0</version>
</dependency>
```

---

## 📌 Notes

- **Current Branch:** `feat/smart-notes`
- **Database:** MySQL (`daymate_db`)
- **Java Version:** 17
- **Spring Boot Version:** 3.5.7

---

## ✅ Quick Start Checklist

Before starting development:
- [ ] Ensure MySQL is running
- [ ] Database `daymate_db` exists
- [ ] All current tests pass
- [ ] Review current codebase structure
- [ ] Set up development environment variables

---

**Last Updated:** [Current Date]  
**Maintainer:** Development Team

