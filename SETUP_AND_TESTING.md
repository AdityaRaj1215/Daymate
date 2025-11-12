# 🚀 DayMate Backend - Setup & Testing Guide

## Prerequisites

1. **Java 17+** (You have Java 21 ✓)
2. **MySQL Server** (Must be running)
3. **Maven** (or use Maven Wrapper: `mvnw.cmd`)

## Step 1: Start MySQL Server

### Windows:
1. Open **Services** (Win + R → `services.msc`)
2. Find **MySQL** service
3. Right-click → **Start** (if not running)

OR use Command Prompt as Administrator:
```cmd
net start MySQL
```

### Verify MySQL is Running:
```cmd
mysql -u root -p
```
Enter password: `root` (or your MySQL password)

## Step 2: Create Database

Connect to MySQL and create the database:

```sql
CREATE DATABASE IF NOT EXISTS daymate_db;
USE daymate_db;
```

Or use MySQL Workbench/phpMyAdmin to create the database.

## Step 3: Verify Database Configuration

Check `src/main/resources/application.properties`:
- **URL**: `jdbc:mysql://localhost:3306/daymate_db`
- **Username**: `root`
- **Password**: `root` (change if different)

If your MySQL password is different, update:
```properties
spring.datasource.password=your_mysql_password
```

## Step 4: Run the Application

### Option 1: Using IntelliJ IDEA
1. Right-click on `BackendApplication.java`
2. Select **Run 'BackendApplication'**

### Option 2: Using Maven Wrapper
```cmd
.\mvnw.cmd spring-boot:run
```

### Option 3: Using Maven (if installed)
```cmd
mvn spring-boot:run
```

## Step 5: Verify Application Started

Look for this in the console:
```
Started BackendApplication in X.XXX seconds
```

The application runs on: **http://localhost:8080**

---

## 📮 Postman Testing Guide

### Base URL
```
http://localhost:8080
```

### 1. Register a New User

**Request:**
```
POST http://localhost:8080/api/users/register
Content-Type: application/json
```

**Body:**
```json
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "password123"
}
```

**Expected Response (200 OK):**
```json
{
  "id": "uuid-here",
  "name": "John Doe",
  "email": "john@example.com"
}
```

---

### 2. Login

**Request:**
```
POST http://localhost:8080/api/users/login
Content-Type: application/json
```

**Body:**
```json
{
  "email": "john@example.com",
  "password": "password123"
}
```

**Expected Response (200 OK):**
```json
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "uuid-here",
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

**⚠️ IMPORTANT:** Copy the `token` value - you'll need it for all protected endpoints!

---

### 3. Create a Task

**Request:**
```
POST http://localhost:8080/api/tasks
Content-Type: application/json
Authorization: Bearer <your-token-here>
```

**Body:**
```json
{
  "title": "Complete project documentation",
  "description": "Write comprehensive docs for DayMate",
  "dueDate": "2025-11-15T10:00:00",
  "urgency": "HIGH"
}
```

**Expected Response (200 OK):**
```json
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write comprehensive docs for DayMate",
  "dueDate": "2025-11-15T10:00:00",
  "urgency": "HIGH",
  "completed": false,
  "reminderSent": false,
  "createdAt": "2025-11-11T18:30:00",
  "updatedAt": "2025-11-11T18:30:00"
}
```

---

### 4. Get All Tasks

**Request:**
```
GET http://localhost:8080/api/tasks
Authorization: Bearer <your-token-here>
```

**Expected Response (200 OK):**
```json
[
  {
    "id": 1,
    "title": "Complete project documentation",
    "description": "Write comprehensive docs for DayMate",
    "dueDate": "2025-11-15T10:00:00",
    "urgency": "HIGH",
    "completed": false,
    "reminderSent": false,
    "createdAt": "2025-11-11T18:30:00",
    "updatedAt": "2025-11-11T18:30:00"
  }
]
```

---

### 5. Update a Task

**Request:**
```
PUT http://localhost:8080/api/tasks/1
Content-Type: application/json
Authorization: Bearer <your-token-here>
```

**Body:**
```json
{
  "title": "Complete project documentation - UPDATED",
  "description": "Write comprehensive docs for DayMate",
  "dueDate": "2025-11-16T10:00:00",
  "urgency": "MEDIUM"
}
```

---

### 6. Mark Task as Complete

**Request:**
```
PATCH http://localhost:8080/api/tasks/1/complete
Authorization: Bearer <your-token-here>
```

---

### 7. Delete a Task

**Request:**
```
DELETE http://localhost:8080/api/tasks/1
Authorization: Bearer <your-token-here>
```

**Expected Response:** 204 No Content

---

### 8. Create a Note

**Request:**
```
POST http://localhost:8080/api/notes
Content-Type: application/json
Authorization: Bearer <your-token-here>
```

**Body:**
```json
{
  "title": "Meeting Notes",
  "content": "Discussed project timeline and deliverables",
  "tags": "meeting,project,important",
  "linkedTaskId": null
}
```

**Or link to a task:**
```json
{
  "title": "Task Notes",
  "content": "Additional details for the task",
  "tags": "task,details",
  "linkedTaskId": 1
}
```

---

### 9. Get All Notes

**Request:**
```
GET http://localhost:8080/api/notes
Authorization: Bearer <your-token-here>
```

---

### 10. Update a Note

**Request:**
```
PUT http://localhost:8080/api/notes/1
Content-Type: application/json
Authorization: Bearer <your-token-here>
```

**Body:**
```json
{
  "title": "Updated Meeting Notes",
  "content": "Updated content here",
  "tags": "meeting,updated",
  "linkedTaskId": 1
}
```

---

### 11. Delete a Note

**Request:**
```
DELETE http://localhost:8080/api/notes/1
Authorization: Bearer <your-token-here>
```

---

### 12. Get User Profile

**Request:**
```
GET http://localhost:8080/api/users/{userId}
Authorization: Bearer <your-token-here>
```

---

## 🔧 Postman Collection Setup

### Setting Up Environment Variables in Postman:

1. Create a new **Environment** in Postman
2. Add variables:
   - `baseUrl`: `http://localhost:8080`
   - `token`: (will be set after login)

### Using Pre-request Scripts:

For protected endpoints, add this in **Pre-request Script**:
```javascript
pm.request.headers.add({
    key: 'Authorization',
    value: 'Bearer ' + pm.environment.get('token')
});
```

### Auto-save Token After Login:

In the **Login** request, add this in **Tests** tab:
```javascript
if (pm.response.code === 200) {
    var jsonData = pm.response.json();
    pm.environment.set("token", jsonData.token);
    console.log("Token saved:", jsonData.token);
}
```

---

## 🐛 Troubleshooting

### Error: "Communications link failure"
- **Solution**: MySQL server is not running. Start MySQL service.

### Error: "Access denied for user 'root'@'localhost'"
- **Solution**: Check MySQL username/password in `application.properties`

### Error: "Unknown database 'daymate_db'"
- **Solution**: Create the database:
  ```sql
  CREATE DATABASE daymate_db;
  ```

### Error: "401 Unauthorized"
- **Solution**: 
  - Make sure you're including the `Authorization` header
  - Format: `Bearer <token>`
  - Token might be expired (24 hours) - login again

### Error: "403 Forbidden" or "Task not found"
- **Solution**: You're trying to access a resource that belongs to another user. Each user can only access their own tasks/notes.

---

## 📝 Testing Checklist

- [ ] MySQL server is running
- [ ] Database `daymate_db` exists
- [ ] Application starts successfully
- [ ] Register a new user
- [ ] Login and get JWT token
- [ ] Create a task
- [ ] Get all tasks
- [ ] Update a task
- [ ] Mark task as complete
- [ ] Delete a task
- [ ] Create a note
- [ ] Get all notes
- [ ] Update a note
- [ ] Delete a note
- [ ] Test error cases (invalid token, wrong credentials, etc.)

---

## 🎯 Quick Test Sequence

1. **Register**: `POST /api/users/register`
2. **Login**: `POST /api/users/login` → Copy token
3. **Create Task**: `POST /api/tasks` (with Authorization header)
4. **Get Tasks**: `GET /api/tasks` (with Authorization header)
5. **Create Note**: `POST /api/notes` (with Authorization header)
6. **Get Notes**: `GET /api/notes` (with Authorization header)

---

**Happy Testing! 🚀**


