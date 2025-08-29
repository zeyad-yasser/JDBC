package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.integration;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

// Entity imports
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Rating;

// DTO imports

// Repository imports
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@Rollback
class CourseIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private AuthorRepo authorRepo;

    @LocalServerPort
    private int port;

    private String baseUrl;
    private HttpHeaders headers;
    private Author testAuthor;
    private CourseRequestDTO testCourseRequestDTO;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/courses";

        // Set up headers with validation header
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-validation-report", "true");

        // Create and save test author - DON'T set ID manually
        testAuthor = new Author();
        // Remove this line: testAuthor.setAuthorId(1);
        testAuthor.setAuthorName("Integration Test Author");
        testAuthor.setAuthorEmail("integration@test.com");
        testAuthor.setAuthorBirthdate(LocalDate.of(1985, 5, 15));
        testAuthor = authorRepo.saveAndFlush(testAuthor);

        // Create test CourseDTO using the generated author ID
        testCourseRequestDTO = new CourseRequestDTO();
        testCourseRequestDTO.setName("Integration Test Course");
        testCourseRequestDTO.setDescription("A course for integration testing");
        testCourseRequestDTO.setCredit(3);
        testCourseRequestDTO.setAuthorId(testAuthor.getAuthorId()); // Use generated ID
    }

    @Test
    void addCourse_WhenValidCourse_ShouldReturnCreatedCourse() {
        // Given
        HttpEntity<CourseRequestDTO> request = new HttpEntity<>(testCourseRequestDTO, headers);

        // When
        ResponseEntity<CourseRequestDTO> response = restTemplate.postForEntity(
                baseUrl, request, CourseRequestDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Integration Test Course");
        assertThat(response.getBody().getDescription()).isEqualTo("A course for integration testing");
        assertThat(response.getBody().getCredit()).isEqualTo(3);
        assertThat(response.getBody().getAuthorId()).isEqualTo(testAuthor.getAuthorId());

        // Verify in database - course should exist
        Optional<Course> courseInDb = courseRepo.findById(1); // Assuming first course
        assertThat(courseInDb).isPresent();
        assertThat(courseInDb.get().getName()).isEqualTo("Integration Test Course");
        assertThat(courseInDb.get().getAuthor().getAuthorId()).isEqualTo(testAuthor.getAuthorId());
    }

    @Test
    void addCourse_WhenInvalidCourse_ShouldReturnBadRequest() {
        // Given - invalid course (empty name, invalid credit)
        CourseRequestDTO invalidCourse = new CourseRequestDTO();
        invalidCourse.setName(""); // Invalid - empty name
        invalidCourse.setCredit(0); // Invalid - less than 1
        invalidCourse.setAuthorId(testAuthor.getAuthorId());

        HttpEntity<CourseRequestDTO> request = new HttpEntity<>(invalidCourse, headers);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // Verify no course was created in database
        List<Course> coursesInDb = courseRepo.findAll();
        assertThat(coursesInDb).isEmpty();
    }

    @Test
    void addCourse_WhenMissingValidationHeader_ShouldReturnForbidden() {
        // Given - headers without validation header
        HttpHeaders headersWithoutValidation = new HttpHeaders();
        headersWithoutValidation.setContentType(MediaType.APPLICATION_JSON);
        // Don't set x-validation-report header

        HttpEntity<CourseRequestDTO> request = new HttpEntity<>(testCourseRequestDTO, headersWithoutValidation);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
    }

    @Test
    void getCourse_WhenCourseExists_ShouldReturnCourse() {
        // Given - create a course first
        Course course = new Course();
        course.setName("Test Course for Get");
        course.setDescription("Description for get test");
        course.setCredit(2);
        course.setAuthor(testAuthor);
        Course savedCourse = courseRepo.saveAndFlush(course);

        // When
        ResponseEntity<CourseRequestDTO> response = restTemplate.getForEntity(
                baseUrl + "/" + savedCourse.getId(), CourseRequestDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Test Course for Get");
        assertThat(response.getBody().getAuthorId()).isEqualTo(testAuthor.getAuthorId());
    }

    @Test
    void getCourse_WhenCourseNotExists_ShouldReturnNotFound() {
        // When - try to get non-existent course
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "/999999", String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    @Test
    void discoverCourses_ShouldReturnTopRatedCourses() {
        // Given - create courses with ratings
        Course course1 = createCourseWithRating("Course 1", 5);
        Course course2 = createCourseWithRating("Course 2", 3);

        // When
        ResponseEntity<Course[]> response = restTemplate.getForEntity(
                baseUrl + "/discover", Course[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        // Should return courses ordered by rating (highest first)
    }

    private Course createCourseWithRating(String name, int ratingValue) {
        Course course = new Course();
        course.setName(name);
        course.setDescription("Test course");
        course.setCredit(3);
        course.setAuthor(testAuthor);
        course = courseRepo.saveAndFlush(course);

        // Add rating
        Rating rating = new Rating(ratingValue, course);
        course.getRatings().add(rating);
        return courseRepo.saveAndFlush(course);
    }

    @AfterEach
    void tearDown() {
        // Clean up - delete in correct order due to foreign keys
        courseRepo.deleteAll();
        authorRepo.deleteAll();
    }
}