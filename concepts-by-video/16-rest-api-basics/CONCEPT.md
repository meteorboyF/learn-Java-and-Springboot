# REST API Basics

## What is REST?
REST (Representational State Transfer) is an architectural style for APIs. A REST API exposes resources (students, courses, products) as URLs and uses standard HTTP methods (GET, POST, PUT, DELETE) to operate on them.

## The 5 Key REST Principles
1. **Stateless:** Each request contains all information needed. The server stores no session state.
2. **Resource-based URLs:** URLs identify THINGS (nouns), not actions: `/students` not `/getStudents`
3. **HTTP methods as operations:** GET=read, POST=create, PUT=update, DELETE=delete
4. **Standard responses:** HTTP status codes communicate the outcome
5. **JSON bodies:** Data is exchanged as JSON (JavaScript Object Notation)

## URL Design Rules
```
/api/{studentId}/students         ← collection of students
/api/{studentId}/students/5       ← specific student with id=5
/api/{studentId}/courses          ← collection of courses
/api/{studentId}/courses/CSE2218  ← specific course

WRONG: /api/getAllStudents       ← verb in URL (not RESTful)
WRONG: /api/deleteStudent?id=5   ← using query param for action (not RESTful)
```

## CRUD → HTTP Method Mapping
```
Create → POST   /students         → 201 Created
Read   → GET    /students         → 200 OK (list)
Read   → GET    /students/{id}    → 200 OK (one)
Update → PUT    /students/{id}    → 200 OK (updated)
Delete → DELETE /students/{id}    → 204 No Content
```

## JSON — What Your API Sends and Receives
```json
// Request body (client → server): POST /students
{"name": "Alice", "studentId": "STU001", "gpa": 3.85, "major": "CS"}

// Response body (server → client): GET /students/1
{"id": 1, "name": "Alice", "studentId": "STU001", "gpa": 3.85, "major": "CS"}
```

## EXAM: Spring handles JSON automatically
Jackson (bundled with `spring-boot-starter-web`) converts Java objects ↔ JSON automatically:
- `@RequestBody` → JSON → Java object (deserialization)
- `@ResponseBody` / `@RestController` → Java object → JSON (serialization)
