package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Assessment;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Rating;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {
    @Mock
    private CourseRepo courseRepo;

    @InjectMocks
    private CourseServiceImpl courseService; // Use the concrete implementation class

    private Course testCourse;
    private Author testAuthor;
    private List<Assessment> testAssessments;
    private List<Rating> testRatings;

    @BeforeEach
    void setUp() {
        // Create test author
        testAuthor = new Author();
        testAuthor.setAuthorId(1);
        testAuthor.setAuthorName("Zeyad Yasser");
        testAuthor.setAuthorEmail("zeyad.yasser@example.com");
        testAuthor.setAuthorBirthdate(LocalDate.of(1980, 1, 1));

        // Create test assessments
        testAssessments = new ArrayList<>();
        Assessment assessment = new Assessment();
        assessment.setId(1L);
        assessment.setContent("Final Exam - Java Basics");
        testAssessments.add(assessment);

        // Create test ratings
        testRatings = new ArrayList<>();
        Rating rating1 = new Rating();
        rating1.setId(1L);
        rating1.setNumber(5);
        testRatings.add(rating1);

        Rating rating2 = new Rating();
        rating2.setId(2L);
        rating2.setNumber(4);
        testRatings.add(rating2);

        // Create test course
        testCourse = new Course();
        testCourse.setId(1);
        testCourse.setName("Java Basics");
        testCourse.setDescription("Introductory course for Java programming.");
        testCourse.setCredit(3);
        testCourse.setAuthor(testAuthor);
        testCourse.setAssessments(testAssessments);
        testCourse.setRatings(testRatings);
    }

    @Test
    void viewCourse_WhenCourseExists_ShouldReturnCourse() {
        // Given
        int courseId = 1;
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(testCourse));

        // When
        Course result = courseService.viewCourse(courseId);

        // Then
        assertNotNull(result);
        assertEquals(courseId, result.getId());
        assertEquals("Java Basics", result.getName());
        assertEquals("Introductory course for Java programming.", result.getDescription());
        assertEquals(3, result.getCredit());

        // Verify author details
        assertNotNull(result.getAuthor());
        assertEquals("Zeyad Yasser", result.getAuthor().getAuthorName());
        assertEquals("zeyad.yasser@example.com", result.getAuthor().getAuthorEmail());

        // Verify assessments
        assertNotNull(result.getAssessments());
        assertEquals(1, result.getAssessments().size());
        assertEquals("Final Exam - Java Basics", result.getAssessments().get(0).getContent());

        // Verify ratings
        assertNotNull(result.getRatings());
        assertEquals(2, result.getRatings().size());
        assertEquals(5, result.getRatings().get(0).getNumber());
        assertEquals(4, result.getRatings().get(1).getNumber());

        // Verify repository method was called exactly once
        verify(courseRepo, times(1)).findById(courseId);
    }

    @Test
    void viewCourse_WhenCourseDoesNotExist_ShouldThrowCourseNotFoundException() {
        // Given
        int nonExistentCourseId = 999;
        when(courseRepo.findById(nonExistentCourseId)).thenReturn(Optional.empty());

        // When & Then
        CourseNotFoundException exception = assertThrows(
                CourseNotFoundException.class,
                () -> courseService.viewCourse(nonExistentCourseId)
        );

        assertEquals("Course with ID: " + nonExistentCourseId + " not found", exception.getMessage());
        verify(courseRepo, times(1)).findById(nonExistentCourseId);
    }


}