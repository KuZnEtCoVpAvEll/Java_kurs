package com.example.demo.Test;

import com.example.demo.Models.Course;
import com.example.demo.Repository.CourseRepository;
import com.example.demo.Service.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private final CourseRepository courseRepository = Mockito.mock(CourseRepository.class);
    private final CourseService courseService = new CourseService(courseRepository);

    @Test
    void testGetAllCourses() {
        Course c1 = new Course();
        Course c2 = new Course();

        when(courseRepository.findAll()).thenReturn(Arrays.asList(c1, c2));

        List<Course> courses = courseService.getAllCourses();

        assertEquals(2, courses.size());
        verify(courseRepository, times(1)).findAll();
    }

    @Test
    void testSaveCourse() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Java");
        course.setDescription("Java basics");

        when(courseRepository.save(course)).thenReturn(course);

        Course saved = courseService.saveCourse(course);

        assertNotNull(saved);
        assertEquals("Java", saved.getTitle());
        verify(courseRepository, times(1)).save(course);
    }

    @Test
    void testGetCourseById_Found() {
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Python");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        Course result = courseService.getCourseById(1L);

        assertNotNull(result);
        assertEquals("Python", result.getTitle());
        verify(courseRepository, times(1)).findById(1L);
    }

    @Test
    void testGetCourseById_NotFound() {
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        Course result = courseService.getCourseById(999L);

        assertNull(result);
        verify(courseRepository, times(1)).findById(999L);
    }

    @Test
    void testUpdateCourse_WhenFound() {
        Course existing = new Course();
        existing.setId(1L);
        existing.setTitle("Old Title");
        existing.setDescription("Old Description");

        Course updated = new Course();
        updated.setTitle("New Title");
        updated.setDescription("New Description");

        when(courseRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(courseRepository.save(existing)).thenReturn(existing);

        Course result = courseService.updateCourse(1L, updated);

        assertNotNull(result);
        assertEquals("New Title", result.getTitle());
        assertEquals("New Description", result.getDescription());
        verify(courseRepository).findById(1L);
        verify(courseRepository).save(existing);
    }

    @Test
    void testUpdateCourse_WhenNotFound() {
        Course updated = new Course();
        updated.setTitle("Won't update");
        updated.setDescription("No course");

        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        Course result = courseService.updateCourse(999L, updated);

        assertNull(result);
        verify(courseRepository, never()).save(any());
    }

    @Test
    void testDeleteCourse() {
        doNothing().when(courseRepository).deleteById(1L);

        courseService.deleteCourse(1L);

        verify(courseRepository, times(1)).deleteById(1L);
    }
}
