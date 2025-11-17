# DayMate Backend API Documentation

## 1. Overview
- **Base URL:** `http://localhost:8080`
- **Content-Type:** `application/json`
- **Authentication:** JWT Bearer tokens (`Authorization: Bearer <token>`)
- **Public Endpoints:** `POST /api/users/register`, `POST /api/users/login`
- **Protected Endpoints:** All others (requires valid JWT)

> Obtain a JWT by calling the login endpoint. Include the token in the `Authorization` header for every protected request.

---

## 2. Authentication APIs

### 2.1 Register User
| Method | Path | Auth |
| --- | --- | --- |
| POST | `/api/users/register` | Public |

**Request Body**
```
{
  "name": "John Doe",
  "email": "john@example.com",
  "password": "Password123!"
}
```

**Response 200 OK**
```
{
  "id": "71c52b40-8970-4dfe-861b-9a26693ed3d1",
  "name": "John Doe",
  "email": "john@example.com"
}
```

**Validation & Errors**
- `400 Bad Request`: missing or invalid fields
- `409 Conflict`: email already exists

---

### 2.2 Login
| Method | Path | Auth |
| --- | --- | --- |
| POST | `/api/users/login` | Public |

**Request Body**
```
{
  "email": "john@example.com",
  "password": "Password123!"
}
```

**Response 200 OK**
```
{
  "token": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...",
  "user": {
    "id": "71c52b40-8970-4dfe-861b-9a26693ed3d1",
    "name": "John Doe",
    "email": "john@example.com"
  }
}
```

**Errors**
- `401 Unauthorized`: invalid email/password
- `400 Bad Request`: missing email or password

---

### 2.3 Get User by ID
| Method | Path | Auth |
| --- | --- | --- |
| GET | `/api/users/{id}` | Protected |

**Response 200 OK**
```
{
  "id": "71c52b40-8970-4dfe-861b-9a26693ed3d1",
  "name": "John Doe",
  "email": "john@example.com",
  "password": "$2a$10$...",
  "createdAt": "2025-11-12T18:00:00",
  "updatedAt": "2025-11-12T18:00:00"
}
```

**Errors**
- `404 Not Found`: user does not exist
- `401 Unauthorized`: missing/invalid token

---

## 3. Task APIs

### 3.1 Create Task
| Method | Path | Auth |
| --- | --- | --- |
| POST | `/api/tasks` | Protected |

**Request Headers**
- `Authorization: Bearer <token>`
- `Content-Type: application/json`

**Request Body**
```
{
  "title": "Complete project documentation",
  "description": "Write detailed documentation for DayMate",
  "dueDate": "2025-11-15T10:00:00",
  "urgency": "HIGH"
}
```

**Response 200 OK**
```
{
  "id": 1,
  "title": "Complete project documentation",
  "description": "Write detailed documentation for DayMate",
  "dueDate": "2025-11-15T10:00:00",
  "urgency": "HIGH",
  "completed": false,
  "reminderSent": false,
  "createdAt": "2025-11-12T18:30:00",
  "updatedAt": "2025-11-12T18:30:00"
}
```

**Validation**
- `title`: required, 1-255 characters
- `description`: optional, max 1000 characters
- `dueDate`: required (ISO date-time)
- `urgency`: optional (`LOW`, `MEDIUM`, `HIGH`)

**Errors**
- `400 Bad Request`: validation issues
- `401 Unauthorized`: missing/invalid token

---

### 3.2 Get Tasks
| Method | Path | Auth |
| --- | --- | --- |
| GET | `/api/tasks` | Protected |

**Response 200 OK**
```
[
  {
    "id": 1,
    "title": "Complete project documentation",
    "description": "Write detailed documentation for DayMate",
    "dueDate": "2025-11-15T10:00:00",
    "urgency": "HIGH",
    "completed": false,
    "reminderSent": false,
    "createdAt": "2025-11-12T18:30:00",
    "updatedAt": "2025-11-12T18:30:00"
  }
]
```

**Notes**
- Returns tasks owned by the authenticated user only

---

### 3.3 Update Task
| Method | Path | Auth |
| --- | --- | --- |
| PUT | `/api/tasks/{id}` | Protected |

**Request Body**: same schema as create

**Response 200 OK**: updated task JSON

**Errors**
- `404 Not Found`: task does not exist or not owned by user
- `400 Bad Request`: validation failure

---

### 3.4 Mark Task Complete
| Method | Path | Auth |
| --- | --- | --- |
| PATCH | `/api/tasks/{id}/complete` | Protected |

**Response 200 OK**
```
{
  "id": 1,
  "title": "Complete project documentation",
  "completed": true,
  ...
}
```

---

### 3.5 Delete Task
| Method | Path | Auth |
| --- | --- | --- |
| DELETE | `/api/tasks/{id}` | Protected |

**Response**: `204 No Content`

**Errors**: `404 Not Found`, `401 Unauthorized`

---

## 4. Notes APIs

### 4.1 Create Note
| Method | Path | Auth |
| --- | --- | --- |
| POST | `/api/notes` | Protected |

**Request Body**
```
{
  "title": "Sprint planning notes",
  "content": "Discussed upcoming goals",
  "tags": "meeting,planning",
  "linkedTaskId": 1
}
```

**Response 200 OK**
```
{
  "id": 5,
  "title": "Sprint planning notes",
  "content": "Discussed upcoming goals",
  "tags": "meeting,planning",
  "createdAt": "2025-11-12T19:00:00",
  "updatedAt": "2025-11-12T19:00:00",
  "linkedTask": {
    "id": 1,
    "title": "Complete project documentation"
  }
}
```

**Validation**
- `title`: required, 1-255 characters
- `content`: optional, <= 10,000 characters
- `tags`: optional, <= 500 characters
- `linkedTaskId`: optional, must belong to the same user

---

### 4.2 Get Notes
| Method | Path | Auth |
| --- | --- | --- |
| GET | `/api/notes` | Protected |

**Response 200 OK**
- Array of notes owned by the user

---

### 4.3 Update Note
| Method | Path | Auth |
| --- | --- | --- |
| PUT | `/api/notes/{id}` | Protected |

**Request Body**: same schema as create

**Response 200 OK**: updated note JSON

**Errors**
- `404 Not Found`: note missing or not owned by user
- `400 Bad Request`: validation errors

---

### 4.4 Delete Note
| Method | Path | Auth |
| --- | --- | --- |
| DELETE | `/api/notes/{id}` | Protected |

**Response**: `204 No Content`

---

## 5. Error Responses
| Status | Meaning | Example |
| --- | --- | --- |
| 400 Bad Request | Validation failure | `{ "title": "Task title is required" }` |
| 401 Unauthorized | Missing/invalid JWT | `{ "status": 401, "error": "Unauthorized" }` |
| 404 Not Found | Resource missing or not owned | `{ "status": 404, "message": "Task not found" }` |
| 409 Conflict | Duplicate resource | `{ "status": 409, "message": "Email already exists!" }` |
| 500 Internal Server Error | Unexpected failure | `{ "status": 500, "error": "Internal Server Error" }` |

Validation errors also include `timestamp`, `status`, and `error` fields.

---

## 6. Data Models

### RegisterRequest
```
{
  "name": "string",
  "email": "string",
  "password": "string"
}
```

### LoginRequest
```
{
  "email": "string",
  "password": "string"
}
```

### TaskRequest
```
{
  "title": "string",
  "description": "string",
  "dueDate": "YYYY-MM-DDTHH:mm:ss",
  "urgency": "LOW | MEDIUM | HIGH"
}
```

### NotesRequest
```
{
  "title": "string",
  "content": "string",
  "tags": "string",
  "linkedTaskId": 1
}
```

### Responses
- `UserResponse`: `{ "id": "string", "name": "string", "email": "string" }`
- `AuthResponse`: `{ "token": "string", "user": UserResponse }`
- `Task`: entity fields (`id`, `title`, `description`, `dueDate`, `urgency`, `completed`, `reminderSent`, `createdAt`, `updatedAt`)
- `Notes`: entity fields (`id`, `title`, `content`, `tags`, `createdAt`, `updatedAt`, `linkedTask`)

### Urgency Enum
- `LOW`
- `MEDIUM`
- `HIGH`

---

## 7. Usage Tips
1. Register ➜ Login ➜ Use JWT for all protected endpoints.
2. Always send `Content-Type: application/json` and `Authorization` headers.
3. Tasks/notes are user-scoped—users cannot access others' data.
4. Due dates use ISO-8601 format (`YYYY-MM-DDTHH:mm:ss`).

This document is located at `API_DOCUMENTATION.md` for backend and frontend reference.
