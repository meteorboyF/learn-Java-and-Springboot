package com.example.courseapp.seeder;

import com.example.courseapp.model.Course;
import com.example.courseapp.repository.CourseRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataSeeder implements CommandLineRunner {

    private final CourseRepository courseRepository;

    public DataSeeder(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        // Duplicate guard — skip if data already exists.
        // Essential for H2 file-based and MySQL to prevent duplicate inserts on restart.
        if (courseRepository.count() > 0) {
            System.out.println("[DataSeeder] Courses already seeded — skipping.");
            return;
        }

        // EXAM CRITICAL: One course MUST have code='CSE2218' as required by the question.
        courseRepository.save(new Course("CSE2218", "Advanced OOP Lab",     3, "Dr. Rahman"));
        courseRepository.save(new Course("CSE1101", "Intro to Programming", 3, "Dr. Islam"));
        courseRepository.save(new Course("MTH2201", "Discrete Mathematics", 3, "Dr. Ahmed"));

        System.out.println("[DataSeeder] Seeded " + courseRepository.count() + " courses.");
    }
}
