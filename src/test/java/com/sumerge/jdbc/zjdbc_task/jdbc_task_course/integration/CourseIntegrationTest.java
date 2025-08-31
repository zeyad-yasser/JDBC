package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.integration;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.AuthorNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseResponseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
@DisplayName("CourseService Integration Tests")
class CourseIntegrationTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepo courseRepo;

    @Autowired
    private AuthorRepo authorRepo;

    private Author testAuthor;
    private Author testAuthor2;
    private CourseRequestDTO courseRequestDTO;

    @BeforeEach
    void setUp() {
        // Clean database
        courseRepo.deleteAll();
        authorRepo.deleteAll();

        // Create test authors
        testAuthor = new Author();
        testAuthor.setAuthorName("Test Author");
        testAuthor.setAuthorEmail("test@author.com");
        testAuthor.setAuthorBirthdate(LocalDate.of(1980, 1, 1));
        testAuthor = authorRepo.save(testAuthor);

        testAuthor2 = new Author();
        testAuthor2.setAuthorName("Second Author");
        testAuthor2.setAuthorEmail("second@author.com");
        testAuthor2.setAuthorBirthdate(LocalDate.of(1985, 5, 15));
        testAuthor2 = authorRepo.save(testAuthor2);

        // Setup course request DTO
        courseRequestDTO = new CourseRequestDTO();
        courseRequestDTO.setName("Integration Test Course");
        courseRequestDTO.setDescription("A course for integration testing");
        courseRequestDTO.setCredit(3);
        courseRequestDTO.setAuthorIds(Arrays.asList(testAuthor.getAuthorId()));
    }

    @Test
    @DisplayName("Should create course and persist to database")
    void addCourse_ShouldPersistCourseToDatabase() {
        // When
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);

        // Then
        assertThat(createdCourse.getName()).isEqualTo("Integration Test Course");
        assertThat(createdCourse.getDescription()).isEqualTo("A course for integration testing");
        assertThat(createdCourse.getCredit()).isEqualTo(3);
        assertThat(createdCourse.getAuthorIds()).containsExactly(testAuthor.getAuthorId());

        // Verify persistence in database
        List<Course> coursesInDb = courseRepo.findAll();
        assertThat(coursesInDb).hasSize(1);

        Course courseInDb = coursesInDb.get(0);
        assertThat(courseInDb.getName()).isEqualTo("Integration Test Course");
        assertThat(courseInDb.getAuthors()).hasSize(1);
        assertThat(courseInDb.getAuthors().get(0).getAuthorEmail()).isEqualTo("test@author.com");
    }

    @Test
    @DisplayName("Should create course with multiple authors")
    void addCourse_ShouldCreateCourseWithMultipleAuthors() {
        // Given
        courseRequestDTO.setAuthorIds(Arrays.asList(testAuthor.getAuthorId(), testAuthor2.getAuthorId()));

        // When
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);

        // Then
        assertThat(createdCourse.getAuthorIds()).hasSize(2);
        assertThat(createdCourse.getAuthorIds()).containsExactlyInAnyOrder(
                testAuthor.getAuthorId(), testAuthor2.getAuthorId());

        // Verify in database
        Course courseInDb = courseRepo.findById(createdCourse.getId()).get();
        assertThat(courseInDb.getAuthors()).hasSize(2);
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when creating course with non-existent author")
    void addCourse_ShouldThrowException_WhenAuthorDoesNotExist() {
        // Given
        courseRequestDTO.setAuthorIds(Arrays.asList(999)); // Non-existent author ID

        // When & Then
        assertThatThrownBy(() -> courseService.addCourse(courseRequestDTO))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessage("Author with ID: [999] not found");

        // Verify no course was created
        assertThat(courseRepo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should retrieve course by ID successfully")
    void getCourseDTO_ShouldReturnCourse_WhenCourseExists() {
        // Given
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);

        // When
        CourseResponseDTO retrievedCourse = courseService.getCourseDTO(createdCourse.getId());

        // Then
        assertThat(retrievedCourse.getId()).isEqualTo(createdCourse.getId());
        assertThat(retrievedCourse.getName()).isEqualTo("Integration Test Course");
        assertThat(retrievedCourse.getAuthorIds()).containsExactly(testAuthor.getAuthorId());
    }

    @Test
    @DisplayName("Should update course successfully")
    void updateCourse_ShouldUpdateAndPersistChanges() {
        // Given
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);

        CourseRequestDTO updateDTO = new CourseRequestDTO();
        updateDTO.setName("Updated Course Name");
        updateDTO.setDescription("Updated description");
        updateDTO.setCredit(4);
        updateDTO.setAuthorIds(Arrays.asList(testAuthor2.getAuthorId())); // Change author

        // When
        CourseResponseDTO updatedCourse = courseService.updateCourse(createdCourse.getId(), updateDTO);

        // Then
        assertThat(updatedCourse.getName()).isEqualTo("Updated Course Name");
        assertThat(updatedCourse.getDescription()).isEqualTo("Updated description");
        assertThat(updatedCourse.getCredit()).isEqualTo(4);
        assertThat(updatedCourse.getAuthorIds()).containsExactly(testAuthor2.getAuthorId());

        // Verify persistence
        Course courseInDb = courseRepo.findById(createdCourse.getId()).get();
        assertThat(courseInDb.getName()).isEqualTo("Updated Course Name");
        assertThat(courseInDb.getAuthors()).hasSize(1);
        assertThat(courseInDb.getAuthors().get(0).getAuthorEmail()).isEqualTo("second@author.com");
    }

    @Test
    @DisplayName("Should throw CourseNotFoundException when updating non-existent course")
    void updateCourse_ShouldThrowException_WhenCourseDoesNotExist() {
        // Given
        int nonExistentId = 999;

        // When & Then
        assertThatThrownBy(() -> courseService.updateCourse(nonExistentId, courseRequestDTO))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessage("Course with ID: " + nonExistentId + " not found");
    }

    @Test
    @DisplayName("Should delete course from database")
    void deleteCourse_ShouldRemoveFromDatabase() {
        // Given
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);
        int courseId = createdCourse.getId();

        // Verify course exists
        assertThat(courseRepo.existsById(courseId)).isTrue();

        // When
        courseService.deleteCourse(courseId);

        // Then
        assertThat(courseRepo.existsById(courseId)).isFalse();
        assertThat(courseRepo.findAll()).isEmpty();
    }

    @Test
    @DisplayName("Should return paginated courses")
    void getCoursesDTOPaginated_ShouldReturnCorrectPage() {
        // Given - Create multiple courses
        courseService.addCourse(courseRequestDTO);

        CourseRequestDTO course2DTO = new CourseRequestDTO();
        course2DTO.setName("Second Course");
        course2DTO.setDescription("Second course description");
        course2DTO.setCredit(2);
        course2DTO.setAuthorIds(Arrays.asList(testAuthor2.getAuthorId()));
        courseService.addCourse(course2DTO);

        // When
        Page<CourseResponseDTO> page = courseService.getCoursesDTOPaginated(0, 1);

        // Then
        assertThat(page.getContent()).hasSize(1);
        assertThat(page.getTotalElements()).isEqualTo(2);
        assertThat(page.getTotalPages()).isEqualTo(2);
        assertThat(page.isFirst()).isTrue();
    }

    @Test
    @DisplayName("Should find courses by author email")
    void getCoursesDTOByAuthorEmail_ShouldReturnCoursesForAuthor() {
        // Given
        courseService.addCourse(courseRequestDTO);

        // Create another course with different author
        CourseRequestDTO course2DTO = new CourseRequestDTO();
        course2DTO.setName("Second Course");
        course2DTO.setDescription("Second course description");
        course2DTO.setCredit(2);
        course2DTO.setAuthorIds(Arrays.asList(testAuthor2.getAuthorId()));
        courseService.addCourse(course2DTO);

        // When
        List<CourseResponseDTO> coursesForAuthor1 = courseService.getCoursesDTOByAuthorEmail("test@author.com");
        List<CourseResponseDTO> coursesForAuthor2 = courseService.getCoursesDTOByAuthorEmail("second@author.com");

        // Then
        assertThat(coursesForAuthor1).hasSize(1);
        assertThat(coursesForAuthor1.get(0).getName()).isEqualTo("Integration Test Course");

        assertThat(coursesForAuthor2).hasSize(1);
        assertThat(coursesForAuthor2.get(0).getName()).isEqualTo("Second Course");
    }

    @Test
    @DisplayName("Should return empty list for non-existent author email")
    void getCoursesDTOByAuthorEmail_ShouldReturnEmptyList_WhenAuthorHasNoCourses() {
        // Given
        courseService.addCourse(courseRequestDTO);

        // When
        List<CourseResponseDTO> courses = courseService.getCoursesDTOByAuthorEmail("nonexistent@author.com");

        // Then
        assertThat(courses).isEmpty();
    }

    @Test
    @DisplayName("Should get all recommended courses")
    void getRecommendedCourses_ShouldReturnAllCourses() {
        // Given
        courseService.addCourse(courseRequestDTO);

        CourseRequestDTO course2DTO = new CourseRequestDTO();
        course2DTO.setName("Second Course");
        course2DTO.setDescription("Another course");
        course2DTO.setCredit(4);
        course2DTO.setAuthorIds(Arrays.asList(testAuthor2.getAuthorId()));
        courseService.addCourse(course2DTO);

        // When
        List<CourseResponseDTO> recommendedCourses = courseService.getRecommendedCourses();

        // Then
        assertThat(recommendedCourses).hasSize(2);
        assertThat(recommendedCourses)
                .extracting(CourseResponseDTO::getName)
                .containsExactlyInAnyOrder("Integration Test Course", "Second Course");
    }

    @Test
    @DisplayName("Should handle course-author relationship correctly")
    void courseAuthorRelationship_ShouldBePersistedCorrectly() {
        // Given - Create course with multiple authors
        courseRequestDTO.setAuthorIds(Arrays.asList(testAuthor.getAuthorId(), testAuthor2.getAuthorId()));

        // When
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);

        // Then
        Course courseInDb = courseRepo.findById(createdCourse.getId()).get();
        assertThat(courseInDb.getAuthors()).hasSize(2);

        // Verify bidirectional relationship
        Author author1InDb = authorRepo.findById(testAuthor.getAuthorId()).get();
        Author author2InDb = authorRepo.findById(testAuthor2.getAuthorId()).get();

        assertThat(author1InDb.getCourses()).contains(courseInDb);
        assertThat(author2InDb.getCourses()).contains(courseInDb);
    }

    @Test
    @DisplayName("Should handle transaction rollback on update failure")
    void updateCourse_ShouldRollbackTransaction_OnAuthorNotFound() {
        // Given
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);
        String originalName = createdCourse.getName();

        CourseRequestDTO updateDTO = new CourseRequestDTO();
        updateDTO.setName("Should Not Be Saved");
        updateDTO.setDescription("This should rollback");
        updateDTO.setCredit(5);
        updateDTO.setAuthorIds(Arrays.asList(999)); // Non-existent author

        // When & Then
        assertThatThrownBy(() -> courseService.updateCourse(createdCourse.getId(), updateDTO))
                .isInstanceOf(AuthorNotFoundException.class);

        // Verify course was not modified (transaction rolled back)
        Course courseInDb = courseRepo.findById(createdCourse.getId()).get();
        assertThat(courseInDb.getName()).isEqualTo(originalName);
        assertThat(courseInDb.getName()).isNotEqualTo("Should Not Be Saved");
    }

    @Test
    @DisplayName("Should handle empty database gracefully")
    void getRecommendedCourses_ShouldReturnEmptyList_WhenNoCourses() {
        // Given - database is already clean from setUp

        // When
        List<CourseResponseDTO> courses = courseService.getRecommendedCourses();

        // Then
        assertThat(courses).isEmpty();
    }

    @Test
    @DisplayName("Should verify course deletion removes relationships")
    void deleteCourse_ShouldRemoveCourseAuthorRelationships() {
        // Given
        CourseResponseDTO createdCourse = courseService.addCourse(courseRequestDTO);
        int courseId = createdCourse.getId();

        // Verify relationships exist
        Author authorInDb = authorRepo.findById(testAuthor.getAuthorId()).get();
        assertThat(authorInDb.getCourses()).isNotEmpty();

        // When
        courseService.deleteCourse(courseId);

        // Then
        assertThat(courseRepo.existsById(courseId)).isFalse();

        // Verify author still exists but relationship is removed
        Author authorAfterDeletion = authorRepo.findById(testAuthor.getAuthorId()).get();
        assertThat(authorAfterDeletion.getCourses()).isEmpty();
    }

    @Test
    @DisplayName("Should handle concurrent course creation")
    void addCourse_ShouldHandleConcurrentCreation() {
        // Given - Create two courses with same author simultaneously
        CourseRequestDTO course1DTO = new CourseRequestDTO();
        course1DTO.setName("Concurrent Course 1");
        course1DTO.setDescription("First concurrent course");
        course1DTO.setCredit(2);
        course1DTO.setAuthorIds(Arrays.asList(testAuthor.getAuthorId()));

        CourseRequestDTO course2DTO = new CourseRequestDTO();
        course2DTO.setName("Concurrent Course 2");
        course2DTO.setDescription("Second concurrent course");
        course2DTO.setCredit(3);
        course2DTO.setAuthorIds(Arrays.asList(testAuthor.getAuthorId()));

        // When
        CourseResponseDTO course1 = courseService.addCourse(course1DTO);
        CourseResponseDTO course2 = courseService.addCourse(course2DTO);

        // Then
        assertThat(course1.getName()).isEqualTo("Concurrent Course 1");
        assertThat(course2.getName()).isEqualTo("Concurrent Course 2");

        // Verify both courses exist in database
        assertThat(courseRepo.findAll()).hasSize(2);

        // Verify author has both courses
        Author authorInDb = authorRepo.findById(testAuthor.getAuthorId()).get();
        assertThat(authorInDb.getCourses()).hasSize(2);
    }

    @Test
    @DisplayName("Should throw CourseNotFoundException when deleting non-existent course")
    void deleteCourse_ShouldThrowException_WhenCourseDoesNotExist() {
        // Given
        int nonExistentId = 999;

        // When & Then
        assertThatThrownBy(() -> courseService.deleteCourse(nonExistentId))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessage("Course with ID: " + nonExistentId + " not found");
    }

    @Test
    @DisplayName("Should handle complex author-course relationships")
     void coursexAuthorCourseRelationships_ShouldBeHandledCorrectly() {
        // Given - Create courses with overlapping authors
        CourseRequestDTO javaCoursesDTO = new CourseRequestDTO();
        javaCoursesDTO.setName("Java Advanced");
        javaCoursesDTO.setDescription("Advanced Java concepts");
        javaCoursesDTO.setCredit(4);
        javaCoursesDTO.setAuthorIds(Arrays.asList(testAuthor.getAuthorId(), testAuthor2.getAuthorId()));

        CourseRequestDTO springCourseDTO = new CourseRequestDTO();
        springCourseDTO.setName("Spring Boot");
        springCourseDTO.setDescription("Spring Boot framework");
        springCourseDTO.setCredit(3);
        springCourseDTO.setAuthorIds(Arrays.asList(testAuthor.getAuthorId()));

        // When
        CourseResponseDTO javaCourse = courseService.addCourse(javaCoursesDTO);
        CourseResponseDTO springCourse = courseService.addCourse(springCourseDTO);

        // Then
        List<CourseResponseDTO> author1Courses = courseService.getCoursesDTOByAuthorEmail("test@author.com");
        List<CourseResponseDTO> author2Courses = courseService.getCoursesDTOByAuthorEmail("second@author.com");

        assertThat(author1Courses).hasSize(2); // testAuthor teaches both courses
        assertThat(author2Courses).hasSize(1); // testAuthor2 teaches only Java Advanced

        assertThat(author1Courses)
                .extracting(CourseResponseDTO::getName)
                .containsExactlyInAnyOrder("Java Advanced", "Spring Boot");

        assertThat(author2Courses)
                .extracting(CourseResponseDTO::getName)
                .containsExactly("Java Advanced");
    }
}