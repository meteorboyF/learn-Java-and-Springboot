# Explanation — Problem 08: REST Controller

## HTTP Method → Correct Status Code (EXAM: memorise this table)

| HTTP Method | Operation | Success Code | Body? |
|---|---|---|---|
| `GET` | Read | `200 OK` | ✅ Yes (the resource) |
| `POST` | Create | `201 Created` | ✅ Yes (the created resource) |
| `PUT` | Update | `200 OK` | ✅ Yes (the updated resource) |
| `PATCH` | Partial update | `200 OK` | ✅ Yes |
| `DELETE` | Delete | `204 No Content` | ❌ No body |

## ResponseEntity Cheat Sheet (EXAM: know all of these)

```java
ResponseEntity.ok(body)                              // 200 + body
ResponseEntity.created(uri).body(obj)                // 201 + Location header + body
ResponseEntity.status(HttpStatus.CREATED).body(obj)  // 201 + body (no Location)
ResponseEntity.noContent().build()                   // 204, no body
ResponseEntity.notFound().build()                    // 404, no body
ResponseEntity.badRequest().body("error message")    // 400 + body
ResponseEntity.status(HttpStatus.NOT_FOUND).body(x)  // 404 + body
ResponseEntity.status(500).body("error")             // 500 + body
```

## `@PathVariable` vs `@RequestParam` — The Key Difference

| | `@PathVariable` | `@RequestParam` |
|---|---|---|
| URL example | `/students/5` | `/students?major=CS` |
| Usage | Identify a specific resource | Filter, sort, paginate |
| Required? | Yes (part of the URL structure) | Often optional |
| Annotation | `@PathVariable Long id` | `@RequestParam("major") String major` |

## The `{studentId}` URL Prefix — Explained

```java
@RequestMapping("/api/{studentId}/students")
```

The `{studentId}` here is the **URL prefix** — your university student number used to make each student's API URL unique in the lab. For example:

- Your student ID is `STU20240001`
- Your API base URL is `/api/STU20240001/students`
- Your classmate's API base URL is `/api/STU20240002/students`

This `{studentId}` template variable in the class-level `@RequestMapping` is captured by:
```java
@PostMapping
public ResponseEntity<Student> create(@PathVariable String studentId, ...)
```

You only need to declare `@PathVariable String studentId` in methods where you USE it. In `getAllStudents()` and `getStudentById()`, we don't use the URL prefix, so we don't declare it — Spring still matches the URL correctly.

## The Location Header — Why It Matters

When you create a resource, the HTTP specification says the response should include a `Location` header pointing to where the new resource lives:

```http
HTTP/1.1 201 Created
Location: /api/STU20240001/students/7
Content-Type: application/json

{"id":7,"name":"Alice","studentId":"STU001","gpa":3.85,"major":"CS"}
```

The JUnit test may check: `response.headers().get("Location")`. If you return 200 instead of 201, or omit the Location header, those tests fail.

## Why `@RestController` Not `@Controller`?

```java
@Controller
public class StudentController {
    @GetMapping("/students")
    public List<Student> getAll() {
        return service.getAll();  // Spring tries to find view "Alice,Bob" — WRONG!
    }
}

@RestController
public class StudentController {
    @GetMapping("/students")
    public List<Student> getAll() {
        return service.getAll();  // Jackson serializes to JSON — CORRECT!
    }
}
```

`@RestController` = `@Controller` + `@ResponseBody`. The `@ResponseBody` part tells Spring: "Write the return value directly to the HTTP response body as JSON (via Jackson), not as a view name."
