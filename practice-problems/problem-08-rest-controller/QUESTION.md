# Problem 08 — Full CRUD REST Controller with Student ID URL Prefix

**Source:** CSE 2218 Advanced OOP Lab — Section 4, Question 8

## The Question

Implement `StudentController` for the `Student` entity. Requirements:

a) Base URL must be: `/api/{studentId}/students` (where `{studentId}` is the URL prefix from YOUR student number)  
b) `GET /api/{studentId}/students` → 200 OK + all students  
c) `GET /api/{studentId}/students/{id}` → 200 OK + one student, or 404 if not found  
d) `POST /api/{studentId}/students` → 201 Created + saved student  
e) `PUT /api/{studentId}/students/{id}` → 200 OK + updated student, or 404  
f) `DELETE /api/{studentId}/students/{id}` → 204 No Content, or 404  
g) POST must return a custom **Location header** pointing to the new resource

## What You Need to Know Before Solving

- `@RestController` = `@Controller` + `@ResponseBody` (auto-JSON responses)
- `@RequestMapping` on the class sets the BASE URL for all methods
- `@GetMapping`, `@PostMapping`, `@PutMapping`, `@DeleteMapping` map HTTP methods
- `@PathVariable` extracts values from the URL path
- `@RequestBody` deserializes the JSON body to a Java object
- `ResponseEntity` gives you control over status codes and headers

## Exam Tips

- POST returns **201** (not 200) — `ResponseEntity.created(uri)` sets 201 automatically
- DELETE returns **204** with no body — `ResponseEntity.noContent().build()`
- `{studentId}` in the class-level URL is the URL PREFIX — not the student's DB id
- The Location header URI format: `/api/{studentId}/students/{newId}`
- `@PathVariable Long id` (from `/{id}`) and `@PathVariable String studentId` (from class URL) are DIFFERENT variables
