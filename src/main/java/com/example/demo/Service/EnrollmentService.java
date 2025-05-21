package com.example.demo.Service;

import com.example.demo.Models.Enrollment;
import com.example.demo.Models.User;
import com.example.demo.Repository.EnrollmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private final EnrollmentRepository enrollmentRepository;

    public List<Enrollment> getAllEnrollments() {
        return enrollmentRepository.findAll();
    }

    public List<Enrollment> getEnrollmentsByUser(User user) {
        return enrollmentRepository.findByUser(user);
    }

    public Enrollment saveEnrollment(Enrollment enrollment) {
        return enrollmentRepository.save(enrollment);
    }

    public Enrollment getEnrollmentById(Long id) {
        return enrollmentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Enrollment not found with id: " + id)
        );
    }

    public Enrollment updateEnrollment(Long id, Enrollment updatedEnrollment) {
        Enrollment existing = enrollmentRepository.findById(id).orElseThrow(() ->
                new RuntimeException("Enrollment not found with id: " + id)
        );

        existing.setCourse(updatedEnrollment.getCourse());
        existing.setUser(updatedEnrollment.getUser());

        return enrollmentRepository.save(existing);
    }

    public void deleteEnrollment(Long id) {
        enrollmentRepository.deleteById(id);
    }
}
