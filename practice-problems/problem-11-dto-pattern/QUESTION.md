# Problem 11 — DTO Pattern: Separating Request, Response and Entity

**Source:** CSE 2218 Advanced OOP Lab — Section 5, Question 11

## The Question

Refactor the `Student` entity to use proper DTOs:

a) `StudentRequest` DTO — fields: `name`, `studentId`, `gpa`, `major` (**no `id` field**)  
b) `StudentResponse` DTO — fields: `id`, `name`, `studentId`, `major` (**no `gpa` — pretend it's sensitive**)  
c) Rewrite the POST and GET endpoints to accept/return DTOs instead of the entity directly  
d) Write the conversion logic: entity → response DTO, request DTO → entity

## What You Need to Know Before Solving

- **Request DTO**: What the client sends → no `id` field (DB assigns it), controls input
- **Response DTO**: What the server returns → hide sensitive fields by omitting getters
- **Conversion**: Service converts request DTO → Entity (to save), Entity → Response DTO (to return)
- Jackson serializes fields that have **getters** — no getter = field hidden from JSON response

## Exam Tips

- No `id` in `StudentRequest` = client cannot influence the DB primary key (security)
- No `gpa` getter in `StudentResponse` = gpa is hidden from API responses (privacy)
- Conversion methods are usually `private` helpers in the `Service` class
- Request DTOs need no-arg constructor + setters (for Jackson deserialization)
- Response DTOs only need getters (Jackson serializes them; no deserialization needed)
- The `@Entity` class itself should NEVER be returned directly from controllers in production
