package com.example.demo.Test;

import com.example.demo.Models.Course;
import com.example.demo.Models.Enrollment;
import com.example.demo.Models.User;
import com.example.demo.Repository.EnrollmentRepository;
import com.example.demo.Service.EnrollmentService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EnrollmentServiceTest {

    private final EnrollmentRepository enrollmentRepository = Mockito.mock(EnrollmentRepository.class);
    private final EnrollmentService enrollmentService = new EnrollmentService(enrollmentRepository);

    @Test
    void testGetAllEnrollments() {
        Enrollment e1 = new Enrollment();
        Enrollment e2 = new Enrollment();

        when(enrollmentRepository.findAll()).thenReturn(Arrays.asList(e1, e2));

        List<Enrollment> list = enrollmentService.getAllEnrollments();

        assertEquals(2, list.size());
        verify(enrollmentRepository, times(1)).findAll();
    }

    @Test
    void testSaveEnrollment() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);

        when(enrollmentRepository.save(enrollment)).thenReturn(enrollment);

        Enrollment saved = enrollmentService.saveEnrollment(enrollment);

        assertNotNull(saved);
        assertEquals(1L, saved.getId());
        verify(enrollmentRepository, times(1)).save(enrollment);
    }

    @Test
    void testGetEnrollmentById_Found() {
        Enrollment enrollment = new Enrollment();
        enrollment.setId(1L);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(enrollment));

        Enrollment found = enrollmentService.getEnrollmentById(1L);

        assertNotNull(found);
        assertEquals(1L, found.getId());
        verify(enrollmentRepository, times(1)).findById(1L);
    }

    @Test
    void testGetEnrollmentById_NotFound() {
        when(enrollmentRepository.findById(99L)).thenReturn(Optional.empty());

        Enrollment found = enrollmentService.getEnrollmentById(99L);

        assertNull(found);
        verify(enrollmentRepository, times(1)).findById(99L);
    }

    @Test
    void testUpdateEnrollment_WhenFound() {
        Enrollment existing = new Enrollment();
        existing.setId(1L);
        existing.setUser(new User());
        existing.setCourse(new Course());

        User newUser = new User();
        newUser.setName("Updated User");

        Course newCourse = new Course();
        newCourse.setTitle("Updated Course");

        Enrollment updatedData = new Enrollment();
        updatedData.setUser(newUser);
        updatedData.setCourse(newCourse);

        when(enrollmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(enrollmentRepository.save(existing)).thenReturn(existing);

        Enrollment updated = enrollmentService.updateEnrollment(1L, updatedData);

        assertNotNull(updated);
        assertEquals("Updated User", updated.getUser().getName());
        assertEquals("Updated Course", updated.getCourse().getTitle());
        verify(enrollmentRepository).findById(1L);
        verify(enrollmentRepository).save(existing);
    }

    @Test
    void testUpdateEnrollment_WhenNotFound() {
        Enrollment updatedData = new Enrollment();
        updatedData.setUser(new User());
        updatedData.setCourse(new Course());

        when(enrollmentRepository.findById(999L)).thenReturn(Optional.empty());

        Enrollment result = enrollmentService.updateEnrollment(999L, updatedData);

        assertNull(result);
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void testDeleteEnrollment() {
        doNothing().when(enrollmentRepository).deleteById(1L);

        enrollmentService.deleteEnrollment(1L);

        verify(enrollmentRepository, times(1)).deleteById(1L);
    }
}
