# Syntax Reference — ResponseEntity and Status Codes

## All ResponseEntity Factory Methods
```java
// 200 OK:
ResponseEntity.ok(body)
ResponseEntity.ok().build()                          // 200, empty body

// 201 Created:
ResponseEntity.created(URI.create("/path")).body(obj) // 201 + Location + body
ResponseEntity.created(uri).build()                   // 201 + Location, no body
ResponseEntity.status(HttpStatus.CREATED).body(obj)   // 201 + body, no Location
ResponseEntity.status(201).body(obj)                  // same, using integer

// 204 No Content:
ResponseEntity.noContent().build()                    // 204, empty body ONLY

// 400 Bad Request:
ResponseEntity.badRequest().body("error message")
ResponseEntity.badRequest().build()

// 404 Not Found:
ResponseEntity.notFound().build()                    // 404, no body
ResponseEntity.status(HttpStatus.NOT_FOUND).body(x) // 404 + body

// 500:
ResponseEntity.internalServerError().body("error")
ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(x)
ResponseEntity.status(500).body(x)
```

## HttpStatus Enum — Common Values
```java
HttpStatus.OK                     // 200
HttpStatus.CREATED                // 201
HttpStatus.NO_CONTENT             // 204
HttpStatus.BAD_REQUEST            // 400
HttpStatus.UNAUTHORIZED           // 401
HttpStatus.FORBIDDEN              // 403
HttpStatus.NOT_FOUND              // 404
HttpStatus.CONFLICT               // 409
HttpStatus.INTERNAL_SERVER_ERROR  // 500
```

## Return Type Patterns
```java
ResponseEntity<Student>        // body is a Student object
ResponseEntity<List<Student>>  // body is a list
ResponseEntity<String>         // body is a plain string
ResponseEntity<Void>           // NO body (use for 204 DELETE responses)
ResponseEntity<ErrorResponse>  // body is a custom error DTO
```

## Builder Pattern
```java
// BodyBuilder pattern — chain calls:
return ResponseEntity
    .status(HttpStatus.CREATED)  // set status
    .header("X-Custom", "value") // add header
    .contentType(MediaType.APPLICATION_JSON) // set content type
    .body(savedObject);          // set body (terminates builder)
```
