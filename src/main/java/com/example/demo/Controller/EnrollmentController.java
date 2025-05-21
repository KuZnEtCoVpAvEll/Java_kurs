package com.example.demo.Controller;

import com.example.demo.Models.Course;
import com.example.demo.Models.Enrollment;
import com.example.demo.Models.Role;
import com.example.demo.Models.User;
import com.example.demo.Repository.CourseRepository;
import com.example.demo.Repository.UserRepository;
import com.example.demo.Service.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/enrollments")
@RequiredArgsConstructor
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserRepository userRepository;
    private final CourseRepository courseRepository;

    @GetMapping
    public ResponseEntity<?> getAllEnrollments(Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (currentUser.getRole() == Role.ADMIN) {
            return ResponseEntity.ok(enrollmentService.getAllEnrollments());
        } else {
            return ResponseEntity.ok(enrollmentService.getEnrollmentsByUser(currentUser));
        }
    }

    @PostMapping
    public ResponseEntity<?> createEnrollment(@RequestBody Enrollment enrollment, Principal principal) {
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (enrollment.getCourse() == null || enrollment.getCourse().getId() == null) {
            return ResponseEntity.badRequest().body("Course ID is required");
        }

        Course course = courseRepository.findById(enrollment.getCourse().getId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Enrollment newEnrollment = Enrollment.builder()
                .user(currentUser)
                .course(course)
                .build();

        Enrollment saved = enrollmentService.saveEnrollment(newEnrollment);
        return ResponseEntity.ok(saved);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getEnrollmentById(@PathVariable Long id, Principal principal) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!enrollment.getUser().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied");
        }

        return ResponseEntity.ok(enrollment);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteEnrollment(@PathVariable Long id, Principal principal) {
        Enrollment enrollment = enrollmentService.getEnrollmentById(id);
        User currentUser = userRepository.findByEmail(principal.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!enrollment.getUser().getId().equals(currentUser.getId())
                && currentUser.getRole() != Role.ADMIN) {
            return ResponseEntity.status(403).body("Access denied");
        }

        enrollmentService.deleteEnrollment(id);
        return ResponseEntity.ok("Enrollment deleted");
    }
}
