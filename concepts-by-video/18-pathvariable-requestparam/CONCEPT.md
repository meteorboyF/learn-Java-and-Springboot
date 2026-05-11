# @PathVariable and @RequestParam

## What Are They?
Both annotations extract data from the URL of an HTTP request, but from different parts:

| Annotation | Extracts from | Example URL | Example value |
|---|---|---|---|
| `@PathVariable` | The URL path segment | `/students/42` | `42` |
| `@RequestParam` | The query string (`?key=value`) | `/students?major=CS` | `"CS"` |

## @PathVariable — for identifying a specific resource
Use when the value is part of the URL path itself, typically an ID.

```
GET /api/STU001/students/42
                          ↑ this 42 is a path variable
```

The URL template `/{id}` defines a placeholder. `@PathVariable Long id` extracts the value from that placeholder and puts it in the `id` parameter.

## @RequestParam — for filtering, sorting, searching
Use when the value is optional or is a query/filter criterion, not the resource identity.

```
GET /students?major=CS&minGpa=3.0
              ↑ these are query parameters (request params)
```

## When to Use Which

| Scenario | Use |
|---|---|
| Fetch student with id=5 | `@PathVariable` → `/students/5` |
| Search students by major | `@RequestParam` → `/students?major=CS` |
| Update student with id=5 | `@PathVariable` → `/students/5` (PUT) |
| Find student by student number | `@RequestParam` → `/students/byStudentId?sid=STU001` |

## EXAM: Optional @RequestParam
`@RequestParam(required=false, defaultValue="CS")` — makes the param optional.
If the client doesn't provide it, `defaultValue` is used instead.
