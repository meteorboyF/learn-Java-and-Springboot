// Example: @PathVariable vs @RequestParam — side-by-side comparison.

package com.example.app.controller;

import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/{studentId}/students") // {studentId} here is also a path variable
public class PathVariableExample {

    // ─── @PathVariable — extracting from the URL path ─────────────────────────

    // URL: GET /api/STU001/students/42
    // {studentId} → studentId parameter
    // {id}        → id parameter
    // EXAM: The placeholder name in {} must match the parameter name exactly,
    //       OR you must use @PathVariable("placeholder-name") String differentParamName.
    @GetMapping("/{id}")
    public String getById(
            @PathVariable String studentId,  // from @RequestMapping's {studentId}
            @PathVariable Long id            // from @GetMapping's {id}
    ) {
        return "Student " + studentId + " → record id=" + id;
    }

    // ─── @PathVariable with mismatched name ───────────────────────────────────
    // URL: GET /api/STU001/students/detail/99
    // When the URL placeholder name differs from the Java parameter name,
    // you must specify the placeholder name in the annotation.
    @GetMapping("/detail/{recordId}")
    public String getDetail(
            @PathVariable("recordId") Long id  // "recordId" matches the URL, "id" is the Java name
    ) {
        return "Detail for record " + id;
    }

    // ─── @RequestParam — extracting from query string ─────────────────────────

    // URL: GET /api/STU001/students/search?major=CS
    // ?major=CS is the query string — NOT part of the URL path.
    // required=true by default, so client MUST send ?major=...
    @GetMapping("/search")
    public String searchByMajor(
            @RequestParam String major  // extracted from ?major=CS
    ) {
        return "Students majoring in " + major;
    }

    // ─── @RequestParam optional with default ──────────────────────────────────
    // URL options:
    //   GET /api/STU001/students/filter              → uses default major="CS"
    //   GET /api/STU001/students/filter?major=Math   → uses "Math"
    //   GET /api/STU001/students/filter?major=Math&minGpa=3.5
    @GetMapping("/filter")
    public String filter(
            @RequestParam(required = false, defaultValue = "CS") String major, // optional
            @RequestParam(required = false, defaultValue = "0.0") Double minGpa // optional
    ) {
        return "major=" + major + ", minGpa=" + minGpa;
    }

    // ─── Combined: @PathVariable + @RequestParam ──────────────────────────────
    // URL: GET /api/STU001/students/byStudentId?sid=STU20240001
    // {studentId} → path variable from class-level @RequestMapping
    // ?sid=...    → request param for looking up by student number (not database id)
    @GetMapping("/byStudentId")
    public String getByStudentNumber(
            @PathVariable String studentId,       // which student's API endpoint
            @RequestParam("sid") String sid       // "sid" matches ?sid= in the URL
    ) {
        return "Looking up student number " + sid + " under API owner " + studentId;
    }
}
