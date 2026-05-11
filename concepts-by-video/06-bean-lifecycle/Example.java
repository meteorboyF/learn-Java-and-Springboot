package com.example.app;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.stereotype.Service;

@Service
public class LifecycleDemo {
    private final StudentRepository repo;

    // Phase 1+2: Constructor — called by Spring during instantiation + injection
    public LifecycleDemo(StudentRepository repo) {
        this.repo = repo;
        System.out.println("1. Constructor called. repo = " + repo);
    }

    // Phase 3: @PostConstruct — runs after ALL dependencies are injected
    @PostConstruct
    public void afterInjection() {
        System.out.println("2. @PostConstruct — bean fully initialized.");
        // Safe to use 'repo' here — it's guaranteed non-null.
    }

    // Phase 5: @PreDestroy — runs when the Spring context is shutting down
    @PreDestroy
    public void beforeDestroy() {
        System.out.println("3. @PreDestroy — bean about to be destroyed.");
        // Release resources, close connections.
    }
}
