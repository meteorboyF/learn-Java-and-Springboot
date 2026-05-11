// Example: Complete service layer with all CRUD operations.
package com.example.app.service;

import com.example.app.exception.StudentNotFoundException;
import com.example.app.model.Student;
import com.example.app.repository.StudentRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) { this.repo = repo; }

    // READ ALL: SELECT * FROM students
    public List<Student> getAll() { return repo.findAll(); }

    // READ ONE by DB pk: SELECT * WHERE id=? → throw 404 if not found
    public Student getById(Long id) {
        return repo.findById(id)
                .orElseThrow(() -> new StudentNotFoundException(id));
    }

    // READ ONE by business key: SELECT * WHERE student_id=?
    public Student getByStudentId(String studentId) {
        return repo.findByStudentId(studentId)
                .orElseThrow(() -> new StudentNotFoundException(
                        "Student not found: " + studentId));
    }

    // CREATE: INSERT
    public Student create(Student student) { return repo.save(student); }

    // UPDATE: find → modify safe fields → save (triggers UPDATE)
    public Student update(Long id, Student newData) {
        Student existing = getById(id);               // reuse → throws if not found
        existing.setName(newData.getName());           // only modifiable fields
        existing.setGpa(newData.getGpa());
        existing.setMajor(newData.getMajor());
        // NOT: existing.setId(), existing.setStudentId() — those are immutable
        return repo.save(existing);                   // UPDATE (has existing id)
    }

    // DELETE: check → delete
    public void delete(Long id) {
        if (!repo.existsById(id)) throw new StudentNotFoundException(id);
        repo.deleteById(id);
    }

    // VALIDATION EXAMPLE (throw IllegalArgumentException → 400 Bad Request)
    public Student createWithValidation(Student student) {
        if (student.getName() == null || student.getName().isBlank()) {
            throw new IllegalArgumentException("Name cannot be blank");
        }
        if (student.getGpa() != null && (student.getGpa() < 0 || student.getGpa() > 4.0)) {
            throw new IllegalArgumentException("GPA must be between 0.0 and 4.0");
        }
        return repo.save(student);
    }
}
