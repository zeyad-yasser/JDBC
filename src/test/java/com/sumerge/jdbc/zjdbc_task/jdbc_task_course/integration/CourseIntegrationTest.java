package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.integration;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.*;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

// Entity imports
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Rating;

// DTO imports
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.advice.ErrorResponse;

// Repository imports
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestPropertySource(locations = "classpath:application-test.properties")
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
    private CourseDTO testCourseDTO;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/courses";

        // Set up headers with validation header
        headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("x-validation-report", "true");

        // Create and save test author
        testAuthor = new Author();
        testAuthor.setAuthorName("Integration Test Author");
        testAuthor.setAuthorEmail("integration@test.com");
        testAuthor.setAuthorBirthdate(LocalDate.of(1985, 5, 15));
        testAuthor = authorRepo.saveAndFlush(testAuthor);

        // Create test CourseDTO
        testCourseDTO = new CourseDTO();
        testCourseDTO.setName("Integration Test Course");
        testCourseDTO.setDescription("A course for integration testing");
        testCourseDTO.setCredit(3);
        testCourseDTO.setAuthorId(testAuthor.getAuthorId());
    }

    @Test
    void addCourse_WhenValidCourse_ShouldReturnCreatedCourse() {
        // Given
        HttpEntity<CourseDTO> request = new HttpEntity<>(testCourseDTO, headers);

        // When
        ResponseEntity<CourseDTO> response = restTemplate.postForEntity(
                baseUrl, request, CourseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo("Integration Test Course");
        assertThat(response.getBody().getDescription()).isEqualTo("A course for integration testing");
        assertThat(response.getBody().getCredit()).isEqualTo(3);
        assertThat(response.getBody().getAuthorId()).isEqualTo(testAuthor.getAuthorId());

        // Verify in database
        List<Course> coursesInDb = courseRepo.findAll();
        assertThat(coursesInDb).hasSize(1);
        Course savedCourse = coursesInDb.get(0);
        assertThat(savedCourse.getName()).isEqualTo("Integration Test Course");
        assertThat(savedCourse.getAuthor().getAuthorId()).isEqualTo(testAuthor.getAuthorId());
    }

    @Test
    void addCourse_WhenInvalidCourse_ShouldReturnBadRequest() {
        // Given - Invalid course with empty name
        CourseDTO invalidCourse = new CourseDTO();
        invalidCourse.setName(""); // Invalid: empty name
        invalidCourse.setCredit(0); // Invalid: credit less than 1
        invalidCourse.setAuthorId(testAuthor.getAuthorId());

        HttpEntity<CourseDTO> request = new HttpEntity<>(invalidCourse, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Validation Failed");
        assertThat(response.getBody().getValidationErrors()).isNotNull();
        assertThat(response.getBody().getValidationErrors()).containsKey("name");
        assertThat(response.getBody().getValidationErrors()).containsKey("credit");

        // Verify no course was saved
        assertThat(courseRepo.count()).isEqualTo(0);
    }

    @Test
    void addCourse_WhenAuthorNotFound_ShouldReturnNotFound() {
        // Given
        testCourseDTO.setAuthorId(9999); // Non-existent author
        HttpEntity<CourseDTO> request = new HttpEntity<>(testCourseDTO, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.postForEntity(
                baseUrl, request, ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Author Not Found");
        assertThat(response.getBody().getMessage()).contains("Author with ID: 9999 not found");

        // Verify no course was saved
        assertThat(courseRepo.count()).isEqualTo(0);
    }

    @Test
    void getCourseById_WhenCourseExists_ShouldReturnCourse() {
        // Given - Create and save a course
        Course savedCourse = createAndSaveCourse();

        // When
        ResponseEntity<CourseDTO> response = restTemplate.getForEntity(
                baseUrl + "/" + savedCourse.getId(), CourseDTO.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getName()).isEqualTo(savedCourse.getName());
        assertThat(response.getBody().getAuthorId()).isEqualTo(testAuthor.getAuthorId());
    }

    @Test
    void getCourseById_WhenCourseNotFound_ShouldReturnNotFound() {
        // When
        ResponseEntity<ErrorResponse> response = restTemplate.getForEntity(
                baseUrl + "/9999", ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Course Not Found");
        assertThat(response.getBody().getMessage()).contains("Course with ID: 9999 not found");
    }

    @Test
    void updateCourse_WhenValidUpdate_ShouldUpdateSuccessfully() {
        // Given
        Course savedCourse = createAndSaveCourse();

        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setName("Updated Course Name");
        updateDTO.setDescription("Updated description");
        updateDTO.setCredit(4);
        updateDTO.setAuthorId(testAuthor.getAuthorId());

        HttpEntity<CourseDTO> request = new HttpEntity<>(updateDTO, headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + savedCourse.getId(),
                HttpMethod.PUT,
                request,
                Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify update in database
        Course updatedCourse = courseRepo.findById(savedCourse.getId()).orElseThrow();
        assertThat(updatedCourse.getName()).isEqualTo("Updated Course Name");
        assertThat(updatedCourse.getDescription()).isEqualTo("Updated description");
        assertThat(updatedCourse.getCredit()).isEqualTo(4);
    }

    @Test
    void updateCourse_WhenCourseNotFound_ShouldReturnNotFound() {
        // Given
        HttpEntity<CourseDTO> request = new HttpEntity<>(testCourseDTO, headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                baseUrl + "/9999",
                HttpMethod.PUT,
                request,
                ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Course Not Found");
    }

    @Test
    void deleteCourse_WhenCourseExists_ShouldDeleteSuccessfully() {
        // Given
        Course savedCourse = createAndSaveCourse();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // When
        ResponseEntity<Void> response = restTemplate.exchange(
                baseUrl + "/" + savedCourse.getId(),
                HttpMethod.DELETE,
                request,
                Void.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(courseRepo.existsById(savedCourse.getId())).isFalse();
    }

    @Test
    void deleteCourse_WhenCourseNotFound_ShouldReturnNotFound() {
        // Given
        HttpEntity<Void> request = new HttpEntity<>(headers);

        // When
        ResponseEntity<ErrorResponse> response = restTemplate.exchange(
                baseUrl + "/9999",
                HttpMethod.DELETE,
                request,
                ErrorResponse.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getError()).isEqualTo("Course Not Found");
    }

    @Test
    void getCoursesPaginated_ShouldReturnPaginatedResults() {
        // Given - Create multiple courses
        createAndSaveCourse("Course 1");
        createAndSaveCourse("Course 2");
        createAndSaveCourse("Course 3");

        // When
        ResponseEntity<String> response = restTemplate.getForEntity(
                baseUrl + "?page=0&size=2", String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).contains("Course 1");
        assertThat(response.getBody()).contains("Course 2");
        // Should contain pagination metadata
        assertThat(response.getBody()).contains("totalElements");
        assertThat(response.getBody()).contains("totalPages");
    }

    @Test
    void getCoursesByAuthorEmail_ShouldReturnAuthorCourses() {
        // Given
        createAndSaveCourse("Author's Course 1");
        createAndSaveCourse("Author's Course 2");

        // Create another author and course to ensure filtering works
        Author otherAuthor = new Author();
        otherAuthor.setAuthorName("Other Author");
        otherAuthor.setAuthorEmail("other@test.com");
        otherAuthor.setAuthorBirthdate(LocalDate.of(1990, 1, 1));
        otherAuthor = authorRepo.saveAndFlush(otherAuthor);

        Course otherCourse = new Course("Other Course", "Description", 2, otherAuthor);
        courseRepo.saveAndFlush(otherCourse);

        // When
        ResponseEntity<CourseDTO[]> response = restTemplate.getForEntity(
                baseUrl + "/author?email=" + testAuthor.getAuthorEmail(),
                CourseDTO[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody()).hasSize(2);

        List<String> courseNames = Arrays.stream(response.getBody())
                .map(CourseDTO::getName)
                .collect(Collectors.toList());
        assertThat(courseNames).contains("Author's Course 1", "Author's Course 2");
        assertThat(courseNames).doesNotContain("Other Course");
    }

    @Test
    void discoverCourses_ShouldReturnTopRatedCourses() {
        // Given - Create courses with ratings
        Course course1 = createAndSaveCourse("High Rated Course");
        Course course2 = createAndSaveCourse("Low Rated Course");

        // Add ratings
        addRatingToCourse(course1, 5);
        addRatingToCourse(course1, 4);
        addRatingToCourse(course2, 2);

        // When
        ResponseEntity<Course[]> response = restTemplate.getForEntity(
                baseUrl + "/discover", Course[].class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().length).isGreaterThan(0);

        // First course should be the higher rated one
        Course firstCourse = response.getBody()[0];
        assertThat(firstCourse.getName()).isEqualTo("High Rated Course");
    }

    @Test
    void requestWithoutValidationHeader_ShouldReturnForbidden() {
        // Given - Headers without validation header
        HttpHeaders headersWithoutValidation = new HttpHeaders();
        headersWithoutValidation.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<CourseDTO> request = new HttpEntity<>(testCourseDTO, headersWithoutValidation);

        // When
        ResponseEntity<String> response = restTemplate.postForEntity(
                baseUrl, request, String.class);

        // Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
        assertThat(response.getBody()).contains("Missing or Invalid x-validation-report header");
    }

    @Test
    void fullCourseLifecycle_ShouldWorkCorrectly() {
        // 1. Create course
        HttpEntity<CourseDTO> createRequest = new HttpEntity<>(testCourseDTO, headers);
        ResponseEntity<CourseDTO> createResponse = restTemplate.postForEntity(
                baseUrl, createRequest, CourseDTO.class);

        assertThat(createResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        CourseDTO createdCourse = createResponse.getBody();
        assertThat(createdCourse).isNotNull();

        // Get the course ID from database since DTO doesn't include it
        Course courseInDb = courseRepo.findAll().get(0);
        int courseId = courseInDb.getId();

        // 2. Read course
        ResponseEntity<CourseDTO> readResponse = restTemplate.getForEntity(
                baseUrl + "/" + courseId, CourseDTO.class);

        assertThat(readResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(readResponse.getBody().getName()).isEqualTo(testCourseDTO.getName());

        // 3. Update course
        CourseDTO updateDTO = new CourseDTO();
        updateDTO.setName("Updated Lifecycle Course");
        updateDTO.setDescription("Updated for lifecycle test");
        updateDTO.setCredit(4);
        updateDTO.setAuthorId(testAuthor.getAuthorId());

        HttpEntity<CourseDTO> updateRequest = new HttpEntity<>(updateDTO, headers);
        ResponseEntity<Void> updateResponse = restTemplate.exchange(
                baseUrl + "/" + courseId, HttpMethod.PUT, updateRequest, Void.class);

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);

        // Verify update
        Course updatedCourse = courseRepo.findById(courseId).orElseThrow();
        assertThat(updatedCourse.getName()).isEqualTo("Updated Lifecycle Course");

        // 4. Delete course
        HttpEntity<Void> deleteRequest = new HttpEntity<>(headers);
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                baseUrl + "/" + courseId, HttpMethod.DELETE, deleteRequest, Void.class);

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
        assertThat(courseRepo.existsById(courseId)).isFalse();
    }

    // Helper methods
    private Course createAndSaveCourse() {
        return createAndSaveCourse("Test Course");
    }

    private Course createAndSaveCourse(String name) {
        Course course = new Course(name, "Test description", 3, testAuthor);
        return courseRepo.saveAndFlush(course);
    }

    private void addRatingToCourse(Course course, int rating) {
        Rating ratingEntity = new Rating(rating, course);
        course.getRatings().add(ratingEntity);
        courseRepo.saveAndFlush(course);
    }
}