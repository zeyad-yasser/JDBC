package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Assessment;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Rating;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.AuthorNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;
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

    @Mock
    private CourseMapper mapper;

    @Mock
    private AuthorRepo authorRepo;

    @InjectMocks
    private CourseServiceImpl courseService;

    private Course testCourse;
    private CourseDTO testCourseDTO;
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

        // Create test CourseDTO
        testCourseDTO = new CourseDTO();
        //testCourseDTO.setId(1);
        testCourseDTO.setName("Java Basics");
        testCourseDTO.setDescription("Introductory course for Java programming.");
        testCourseDTO.setCredit(3);
        testCourseDTO.setAuthorId(1);
    }

    //  viewCourse Tests
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

    //  addCourse Tests
    @Test
    void addCourse_WhenValidCourseDTO_ShouldReturnSavedCourseDTO() {
        // Given
        Course mappedCourse = new Course();
        mappedCourse.setName("Java Basics");
        mappedCourse.setDescription("Introductory course for Java programming.");
        mappedCourse.setCredit(3);

        Course savedCourse = new Course();
        savedCourse.setId(1);
        savedCourse.setName("Java Basics");
        savedCourse.setDescription("Introductory course for Java programming.");
        savedCourse.setCredit(3);
        savedCourse.setAuthor(testAuthor);

        when(mapper.toEntityForCreate(testCourseDTO)).thenReturn(mappedCourse);
        when(authorRepo.findById(1)).thenReturn(Optional.of(testAuthor));
        when(courseRepo.save(mappedCourse)).thenReturn(savedCourse);
        when(mapper.toDTO(savedCourse)).thenReturn(testCourseDTO);

        // When
        CourseDTO result = courseService.addCourse(testCourseDTO);

        // Then
        assertNotNull(result);
        assertEquals("Java Basics", result.getName());
        assertEquals("Introductory course for Java programming.", result.getDescription());
        assertEquals(3, result.getCredit());
        assertEquals(1, result.getAuthorId());

        verify(mapper, times(1)).toEntityForCreate(testCourseDTO);
        verify(authorRepo, times(1)).findById(1);
        verify(courseRepo, times(1)).save(mappedCourse);
        verify(mapper, times(1)).toDTO(savedCourse);
    }

    @Test
    void addCourse_WhenAuthorNotFound_ShouldThrowAuthorNotFoundException() {
        // Given
        Course mappedCourse = new Course();
        when(mapper.toEntityForCreate(testCourseDTO)).thenReturn(mappedCourse);
        when(authorRepo.findById(1)).thenReturn(Optional.empty());

        // When & Then
        AuthorNotFoundException exception = assertThrows(
                AuthorNotFoundException.class,
                () -> courseService.addCourse(testCourseDTO)
        );

        assertEquals("Author with ID: 1 not found", exception.getMessage());
        verify(mapper, times(1)).toEntityForCreate(testCourseDTO);
        verify(authorRepo, times(1)).findById(1);
        verify(courseRepo, never()).save(any());
        verify(mapper, never()).toDTO(any());
    }

    //  updateCourse Tests

    @Test
    void updateCourse_WhenValidCourseAndAuthorExist_ShouldUpdateSuccessfully() {
        // Given
        int courseId = 1;
        Course existingCourse = new Course();
        existingCourse.setId(courseId);
        existingCourse.setName("Old Name");

        when(courseRepo.findById(courseId)).thenReturn(Optional.of(existingCourse));
        when(authorRepo.findById(1)).thenReturn(Optional.of(testAuthor));
        when(courseRepo.save(existingCourse)).thenReturn(existingCourse);

        // When
        courseService.updateCourse(courseId, testCourseDTO);

        // Then
        verify(courseRepo, times(1)).findById(courseId);
        verify(mapper, times(1)).updateCourseFromDTO(testCourseDTO, existingCourse);
        verify(authorRepo, times(1)).findById(1);
        verify(courseRepo, times(1)).save(existingCourse);
        assertEquals(testAuthor, existingCourse.getAuthor());
    }

    @Test
    void updateCourse_WhenCourseNotFound_ShouldThrowCourseNotFoundException() {
        // Given
        int nonExistentCourseId = 999;
        when(courseRepo.findById(nonExistentCourseId)).thenReturn(Optional.empty());

        // When & Then
        CourseNotFoundException exception = assertThrows(
                CourseNotFoundException.class,
                () -> courseService.updateCourse(nonExistentCourseId, testCourseDTO)
        );

        assertEquals("Course with ID: " + nonExistentCourseId + " not found", exception.getMessage());
        verify(courseRepo, times(1)).findById(nonExistentCourseId);
        verify(mapper, never()).updateCourseFromDTO(any(), any());
        verify(authorRepo, never()).findById(any());
        verify(courseRepo, never()).save(any());
    }

    //  deleteCourse Tests
    @Test
    void deleteCourse_WhenCourseExists_ShouldDeleteSuccessfully() {
        // Given
        int courseId = 1;
        when(courseRepo.existsById(courseId)).thenReturn(true);

        // When
        courseService.deleteCourse(courseId);

        // Then
        verify(courseRepo, times(1)).existsById(courseId);
        verify(courseRepo, times(1)).deleteById(courseId);
    }

    @Test
    void deleteCourse_WhenCourseDoesNotExist_ShouldThrowCourseNotFoundException() {
        // Given
        int nonExistentCourseId = 999;
        when(courseRepo.existsById(nonExistentCourseId)).thenReturn(false);

        // When & Then
        CourseNotFoundException exception = assertThrows(
                CourseNotFoundException.class,
                () -> courseService.deleteCourse(nonExistentCourseId)
        );

        assertEquals("Course with ID: " + nonExistentCourseId + " not found", exception.getMessage());
        verify(courseRepo, times(1)).existsById(nonExistentCourseId);
        verify(courseRepo, never()).deleteById(any());
    }
}