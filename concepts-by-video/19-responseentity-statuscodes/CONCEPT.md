# ResponseEntity and HTTP Status Codes

## What is it?
`ResponseEntity<T>` is Spring's wrapper for an HTTP response. It lets you control all three parts of the response: status code, headers, and body. Without it, you only control the body (Spring sets status to 200 by default).

## Why Does It Exist?
REST APIs need to communicate different outcomes through HTTP status codes. A client shouldn't have to parse the response body to know if a request succeeded or failed — the status code tells them instantly. `ResponseEntity` gives you full control over that status code.

## When to Use It
In EVERY controller method. Always return `ResponseEntity<YourType>` instead of `YourType` directly.

## HTTP Status Code Quick Reference — EXAM: memorise these
| Code | Name | When to Use |
|---|---|---|
| 200 | OK | GET / PUT success |
| 201 | Created | POST success (new resource created) |
| 204 | No Content | DELETE success (nothing to return) |
| 400 | Bad Request | Client sent invalid data |
| 404 | Not Found | Resource doesn't exist |
| 409 | Conflict | Duplicate resource (unique constraint) |
| 500 | Internal Server Error | Unhandled server-side error |

## The Most Important Patterns

```java
// 200 OK — use for GET and PUT
ResponseEntity.ok(body)
ResponseEntity.ok().build()  // 200, no body (rare)

// 201 Created — use for POST (EXAM: NOT 200!)
ResponseEntity.created(uri).body(saved)          // 201 + Location header + body
ResponseEntity.status(HttpStatus.CREATED).body(saved)  // 201 + body, no Location

// 204 No Content — use for DELETE (EXAM: NOT 200!)
ResponseEntity.noContent().build()

// 404 Not Found
ResponseEntity.notFound().build()               // 404, no body
ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorBody)  // 404 + body

// 400 Bad Request
ResponseEntity.badRequest().body(errorMessage)
```
