// Example: All three types of dependency injection side by side.
// In practice, ALWAYS use constructor injection (Style 1).

package com.example.app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

// ─── STYLE 1: Constructor Injection (PREFERRED) ──────────────────────────────
@Service
public class StudentService {

    // private final — the field is set ONCE in the constructor and never changes.
    // This guarantees thread-safety and makes the dependency required.
    private final StudentRepository studentRepository;

    // Spring sees ONE constructor → uses it for injection automatically.
    // @Autowired is OPTIONAL when there is only one constructor. EXAM: know this.
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
        // At this point, studentRepository is guaranteed non-null.
        // The dependency was validated by Spring before calling this constructor.
    }
    // USE THIS STYLE. Always. For every Spring bean.
}

// ─── STYLE 2: Field Injection (AVOID — shown for recognition) ────────────────
// @Service
// public class StudentServiceBad {
//     @Autowired                        // Spring injects this field directly via reflection
//     private StudentRepository repo;   // NOT final — cannot guarantee immutability
//     // Problems:
//     //   1. Hidden dependency — not visible in constructor signature
//     //   2. Cannot write: new StudentServiceBad(mockRepo) in tests
//     //   3. Must use @Autowired explicitly (no shortcut)
//     //   4. Violates encapsulation (Spring modifies a private field)
// }

// ─── STYLE 3: Setter Injection (rarely needed) ───────────────────────────────
// @Service
// public class StudentServiceSetter {
//     private StudentRepository repo;
//
//     @Autowired  // required here — Spring knows to call this setter
//     public void setRepo(StudentRepository repo) {
//         this.repo = repo;
//     }
//     // Use when: the dependency is truly OPTIONAL (repo might not exist)
//     // Rare in practice. Constructor injection handles optional deps via @Nullable too.
// }
