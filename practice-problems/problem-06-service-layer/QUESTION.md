# Problem 06 — Full Service Layer with Exception Throwing

**Source:** CSE 2218 Advanced OOP Lab — Section 3, Question 6

## The Question

Implement `StudentService` with the following methods:

a) `getAllStudents()` — returns all students  
b) `getStudentById(Long id)` — throws `StudentNotFoundException` if not found  
c) `getStudentByStudentId(String studentId)` — throws `StudentNotFoundException` if not found  
d) `createStudent(Student student)` — saves and returns the student  
e) `updateStudent(Long id, Student updatedData)` — updates name/gpa/major, throws if not found  
f) `deleteStudent(Long id)` — throws if not found, then deletes  

Also write the `StudentNotFoundException` class.

## What You Need to Know Before Solving

- The service is annotated with `@Service`
- Use constructor injection with `private final StudentRepository repo`
- `findById()` returns `Optional<T>` — use `.orElseThrow()`, NEVER `.get()`
- `repo.save()` does INSERT if id is null, UPDATE if id already exists
- `existsById()` is more efficient than `findById()` when you only need to check existence

## Exam Tips

- `StudentNotFoundException extends RuntimeException` — NOT `Exception`
- `.orElseThrow()` throws YOUR exception with YOUR message → GlobalExceptionHandler returns 404
- `.get()` throws `NoSuchElementException` → NOT caught by GlobalExceptionHandler → returns 500
- In `updateStudent`: update ONLY name/gpa/major — NEVER update id or studentId
- `deleteStudent`: check with `existsById()` (efficient) BEFORE calling `deleteById()`
