// Example: Correct POST endpoint returning 201 Created with a Location header.

package com.example.app.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;

@RestController
@RequestMapping("/api/{studentId}/students")
public class Post201Example {

    // ─── WRONG way (Bug 1) ────────────────────────────────────────────────────
    // This compiles and runs, but returns 200 instead of 201.
    // EXAM: This exact mistake is tested in Problem 12.
    /*
    @PostMapping
    public ResponseEntity<String> createWrong(@RequestBody String body) {
        String saved = "NewStudent";
        return ResponseEntity.ok(saved); // ← 200 OK — WRONG for POST!
    }
    */

    // ─── CORRECT way — using ResponseEntity.created() ─────────────────────────
    // Full URL: POST /api/STU001/students
    // Client sends JSON body → @RequestBody puts it in 'request'
    // We return 201 Created + a Location header showing where the new resource lives.
    @PostMapping
    public ResponseEntity<String> createCorrect(
            @PathVariable String studentId,   // from class @RequestMapping: /api/{studentId}/students
            @RequestBody String request       // JSON body from the client
    ) {
        // Step 1: Pretend we saved and got back id=42.
        // In a real app: Student saved = service.create(request);
        Long newId = 42L;
        String saved = "NewStudent#" + newId;

        // Step 2: Build the Location URI — where can the client find the new resource?
        // Convention: /api/{studentId}/students/{newId}
        URI location = URI.create(
                "/api/" + studentId + "/students/" + newId
        );
        // The URI string above becomes the "Location" response header.

        // Step 3: Return 201 Created + Location header + the saved object as body.
        // ResponseEntity.created(location) → sets status=201 and Location header.
        // .body(saved)                     → adds the JSON body.
        return ResponseEntity
                .created(location)   // 201 + Location: /api/STU001/students/42
                .body(saved);        // body: "NewStudent#42"
    }

    // ─── Minimal 201 without Location (also correct, but less informative) ─────
    // If you don't want to bother with the Location URI, this is still correct.
    @PostMapping("/simple")
    public ResponseEntity<String> createSimple(@RequestBody String request) {
        String saved = "NewStudent";
        // HttpStatus.CREATED = 201
        return ResponseEntity
                .status(org.springframework.http.HttpStatus.CREATED)
                .body(saved);
    }
}
