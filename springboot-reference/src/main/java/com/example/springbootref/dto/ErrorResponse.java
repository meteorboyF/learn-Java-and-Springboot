// ============================================================
// ErrorResponse.java — DTO for error responses
// When something goes wrong, we return structured JSON (not a plain string).
// This class defines the shape of every error response from this API.
// ============================================================
package com.example.springbootref.dto;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

// WHY a separate ErrorResponse class?
//   Without it, we'd return a plain String: ResponseEntity.status(404).body("not found")
//   With it, the client receives structured JSON:
//   {
//     "status": 404,
//     "message": "Student not found with id: 5",
//     "timestamp": "2024-05-11 10:22:01"
//   }
//   JUnit tests often check specific JSON fields — a plain String would fail those tests.
//   EXAM: Know why we return a DTO instead of a plain String.
public class ErrorResponse {

    private int    status;     // HTTP status code as an integer (e.g., 404, 400, 500)
    private String message;   // human-readable error description
    private String timestamp; // when the error occurred (helps debugging)

    // Constructor — sets all three fields at once.
    // The timestamp is auto-generated here so callers don't need to supply it.
    public ErrorResponse(int status, String message) {
        this.status    = status;
        this.message   = message;

        // LocalDateTime.now() = current date and time (no timezone).
        // DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss") = format:
        //   yyyy = 4-digit year, MM = 2-digit month, dd = 2-digit day
        //   HH = 24-hour hour, mm = minutes, ss = seconds
        //   Example output: "2024-05-11 10:22:01"
        this.timestamp = LocalDateTime.now()
                .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    // Getters ONLY — no setters needed (error responses are immutable once created).
    // EXAM: Jackson requires GETTERS to serialize fields to JSON.
    //       If you remove a getter, that field disappears from the JSON output!
    public int    getStatus()    { return status; }
    public String getMessage()   { return message; }
    public String getTimestamp() { return timestamp; }
}
