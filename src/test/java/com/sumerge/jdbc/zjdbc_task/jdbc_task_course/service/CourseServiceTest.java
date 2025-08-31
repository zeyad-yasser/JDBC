package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.AuthorNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseResponseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CourseService Unit Tests")
class CourseServiceTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private CourseMapper mapper;

    @Mock
    private AuthorRepo authorRepo;

    @InjectMocks
    private CourseService courseService;

    private Course course;
    private CourseRequestDTO courseRequestDTO;
    private CourseResponseDTO courseResponseDTO;
    private Author author;

    @BeforeEach
    void setUp() {
        // Setup Author
        author = new Author();
        author.setAuthorId(1);
        author.setAuthorName("John Doe");
        author.setAuthorEmail("john.doe@example.com");
        author.setAuthorBirthdate(LocalDate.of(1980, 1, 1));

        // Setup Course
        course = new Course();
        course.setId(1);
        course.setName("Java Programming");
        course.setDescription("Learn Java from scratch");
        course.setCredit(3);
        course.setAuthors(Arrays.asList(author));

        // Setup DTOs
        courseRequestDTO = new CourseRequestDTO();
        courseRequestDTO.setName("Java Programming");
        courseRequestDTO.setDescription("Learn Java from scratch");
        courseRequestDTO.setCredit(3);
        courseRequestDTO.setAuthorIds(Arrays.asList(1));

        courseResponseDTO = new CourseResponseDTO();
        courseResponseDTO.setId(1);
        courseResponseDTO.setName("Java Programming");
        courseResponseDTO.setDescription("Learn Java from scratch");
        courseResponseDTO.setCredit(3);
        courseResponseDTO.setAuthorIds(Arrays.asList(1));
    }

    @Test
    @DisplayName("Should return all recommended courses")
    void getRecommendedCourses_ShouldReturnAllCourses() {
        // Given
        List<Course> courses = Arrays.asList(course);
        List<CourseResponseDTO> expectedDTOs = Arrays.asList(courseResponseDTO);

        when(courseRepo.findAll()).thenReturn(courses);
        when(mapper.toDTOList(courses)).thenReturn(expectedDTOs);

        // When
        List<CourseResponseDTO> result = courseService.getRecommendedCourses();

        // Then
        assertThat(result).hasSize(1);
        assertThat(result).isEqualTo(expectedDTOs);
        verify(courseRepo).findAll();
        verify(mapper).toDTOList(courses);
    }

    @Test
    @DisplayName("Should return course by ID successfully")
    void viewCourse_ShouldReturnCourse_WhenCourseExists() {
        // Given
        int courseId = 1;
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));

        // When
        Course result = courseService.viewCourse(courseId);

        // Then
        assertThat(result).isEqualTo(course);
        verify(courseRepo).findById(courseId);
    }

    @Test
    @DisplayName("Should throw CourseNotFoundException when course does not exist")
    void viewCourse_ShouldThrowException_WhenCourseDoesNotExist() {
        // Given
        int courseId = 999;
        when(courseRepo.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> courseService.viewCourse(courseId))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessage("Course with ID: " + courseId + " not found");

        verify(courseRepo).findById(courseId);
    }

    @Test
    @DisplayName("Should return course DTO by ID successfully")
    void getCourseDTO_ShouldReturnCourseDTO_WhenCourseExists() {
        // Given
        int courseId = 1;
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        when(mapper.toResponseDTO(course)).thenReturn(courseResponseDTO);

        // When
        CourseResponseDTO result = courseService.getCourseDTO(courseId);

        // Then
        assertThat(result).isEqualTo(courseResponseDTO);
        verify(courseRepo).findById(courseId);
        verify(mapper).toResponseDTO(course);
    }

    @Test
    @DisplayName("Should throw CourseNotFoundException when getting DTO for non-existent course")
    void getCourseDTO_ShouldThrowException_WhenCourseDoesNotExist() {
        // Given
        int courseId = 999;
        when(courseRepo.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> courseService.getCourseDTO(courseId))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessage("Course with ID: " + courseId + " not found");

        verify(courseRepo).findById(courseId);
        verifyNoInteractions(mapper);
    }

    @Test
    @DisplayName("Should add course successfully")
    void addCourse_ShouldCreateAndReturnCourse_WhenAuthorsExist() {
        // Given
        List<Author> authors = Arrays.asList(author);

        when(mapper.toEntityForCreate(courseRequestDTO)).thenReturn(course);
        when(authorRepo.findAllById(courseRequestDTO.getAuthorIds())).thenReturn(authors);
        when(courseRepo.save(course)).thenReturn(course);
        when(mapper.toResponseDTO(course)).thenReturn(courseResponseDTO);

        // When
        CourseResponseDTO result = courseService.addCourse(courseRequestDTO);

        // Then
        assertThat(result).isEqualTo(courseResponseDTO);
        verify(mapper).toEntityForCreate(courseRequestDTO);
        verify(authorRepo).findAllById(courseRequestDTO.getAuthorIds());
        verify(courseRepo).save(course);
        verify(mapper).toResponseDTO(course);
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when adding course with non-existent authors")
    void addCourse_ShouldThrowException_WhenAuthorsDoNotExist() {
        // Given
        when(mapper.toEntityForCreate(courseRequestDTO)).thenReturn(course);
        when(authorRepo.findAllById(courseRequestDTO.getAuthorIds())).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> courseService.addCourse(courseRequestDTO))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessage("Author with ID: " + courseRequestDTO.getAuthorIds() + " not found");

        verify(mapper).toEntityForCreate(courseRequestDTO);
        verify(authorRepo).findAllById(courseRequestDTO.getAuthorIds());
        verify(courseRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should update course successfully")
    void updateCourse_ShouldUpdateAndReturnCourse_WhenCourseAndAuthorsExist() {
        // Given
        int courseId = 1;
        List<Author> authors = Arrays.asList(author);

        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(mapper).updateCourseFromDTO(courseRequestDTO, course);
        when(authorRepo.findAllById(courseRequestDTO.getAuthorIds())).thenReturn(authors);
        when(courseRepo.save(course)).thenReturn(course);
        when(mapper.toResponseDTO(course)).thenReturn(courseResponseDTO);

        // When
        CourseResponseDTO result = courseService.updateCourse(courseId, courseRequestDTO);

        // Then
        assertThat(result).isEqualTo(courseResponseDTO);
        verify(courseRepo).findById(courseId);
        verify(mapper).updateCourseFromDTO(courseRequestDTO, course);
        verify(authorRepo).findAllById(courseRequestDTO.getAuthorIds());
        verify(courseRepo).save(course);
        verify(mapper).toResponseDTO(course);
    }

    @Test
    @DisplayName("Should throw CourseNotFoundException when updating non-existent course")
    void updateCourse_ShouldThrowException_WhenCourseDoesNotExist() {
        // Given
        int courseId = 999;
        when(courseRepo.findById(courseId)).thenReturn(Optional.empty());

        // When & Then
        assertThatThrownBy(() -> courseService.updateCourse(courseId, courseRequestDTO))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessage("Course with ID: " + courseId + " not found");

        verify(courseRepo).findById(courseId);
        verifyNoInteractions(mapper);
        verifyNoInteractions(authorRepo);
    }

    @Test
    @DisplayName("Should throw AuthorNotFoundException when updating course with non-existent authors")
    void updateCourse_ShouldThrowException_WhenAuthorsDoNotExist() {
        // Given
        int courseId = 1;
        when(courseRepo.findById(courseId)).thenReturn(Optional.of(course));
        doNothing().when(mapper).updateCourseFromDTO(courseRequestDTO, course);
        when(authorRepo.findAllById(courseRequestDTO.getAuthorIds())).thenReturn(List.of());

        // When & Then
        assertThatThrownBy(() -> courseService.updateCourse(courseId, courseRequestDTO))
                .isInstanceOf(AuthorNotFoundException.class)
                .hasMessage("Author with ID: " + courseRequestDTO.getAuthorIds() + " not found");

        verify(courseRepo).findById(courseId);
        verify(mapper).updateCourseFromDTO(courseRequestDTO, course);
        verify(authorRepo).findAllById(courseRequestDTO.getAuthorIds());
        verify(courseRepo, never()).save(any());
    }

    @Test
    @DisplayName("Should delete course successfully")
    void deleteCourse_ShouldDeleteCourse_WhenCourseExists() {
        // Given
        int courseId = 1;
        when(courseRepo.existsById(courseId)).thenReturn(true);

        // When
        courseService.deleteCourse(courseId);

        // Then
        verify(courseRepo).existsById(courseId);
        verify(courseRepo).deleteById(courseId);
    }

    @Test
    @DisplayName("Should throw CourseNotFoundException when deleting non-existent course")
    void deleteCourse_ShouldThrowException_WhenCourseDoesNotExist() {
        // Given
        int courseId = 999;
        when(courseRepo.existsById(courseId)).thenReturn(false);

        // When & Then
        assertThatThrownBy(() -> courseService.deleteCourse(courseId))
                .isInstanceOf(CourseNotFoundException.class)
                .hasMessage("Course with ID: " + courseId + " not found");

        verify(courseRepo).existsById(courseId);
        verify(courseRepo, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should return paginated courses successfully")
    void getCoursesDTOPaginated_ShouldReturnPagedCourses() {
        // Given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> coursePage = new PageImpl<>(Arrays.asList(course));
        Page<CourseResponseDTO> expectedPage = new PageImpl<>(Arrays.asList(courseResponseDTO));

        when(courseRepo.findAll(pageRequest)).thenReturn(coursePage);
        when(mapper.toResponseDTO(course)).thenReturn(courseResponseDTO);

        // When
        Page<CourseResponseDTO> result = courseService.getCoursesDTOPaginated(page, size);

        // Then
        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(courseResponseDTO);
        verify(courseRepo).findAll(pageRequest);
    }

    @Test
    @DisplayName("Should return courses by author email successfully")
    void getCoursesDTOByAuthorEmail_ShouldReturnCourses_WhenEmailExists() {
        // Given
        String email = "john.doe@example.com";
        List<Course> courses = Arrays.asList(course);

        when(courseRepo.findByAuthorEmail(email)).thenReturn(courses);

        // When
        List<CourseResponseDTO> result = courseService.getCoursesDTOByAuthorEmail(email);

        // Then
        assertThat(result).hasSize(1);
        assertThat(result.get(0).getName()).isEqualTo("Java Programming");
        assertThat(result.get(0).getAuthorIds()).containsExactly(1);
        verify(courseRepo).findByAuthorEmail(email);
    }

    @Test
    @DisplayName("Should return empty list when no courses found for author email")
    void getCoursesDTOByAuthorEmail_ShouldReturnEmptyList_WhenNoCoursesFound() {
        // Given
        String email = "nonexistent@example.com";
        when(courseRepo.findByAuthorEmail(email)).thenReturn(List.of());

        // When
        List<CourseResponseDTO> result = courseService.getCoursesDTOByAuthorEmail(email);

        // Then
        assertThat(result).isEmpty();
        verify(courseRepo).findByAuthorEmail(email);
    }

    @Test
    @DisplayName("Should handle multiple authors for a course")
    void addCourse_ShouldHandleMultipleAuthors() {
        // Given
        Author author2 = new Author();
        author2.setAuthorId(2);
        author2.setAuthorName("Jane Smith");
        author2.setAuthorEmail("jane.smith@example.com");

        List<Integer> authorIds = Arrays.asList(1, 2);
        List<Author> authors = Arrays.asList(author, author2);
        Course mockCourse = mock(Course.class); // Create a mock Course

        courseRequestDTO.setAuthorIds(authorIds);

        when(mapper.toEntityForCreate(courseRequestDTO)).thenReturn(course);
        when(authorRepo.findAllById(authorIds)).thenReturn(authors);
        when(courseRepo.save(course)).thenReturn(course);
        when(mapper.toResponseDTO(course)).thenReturn(courseResponseDTO);

        // When
        CourseResponseDTO result = courseService.addCourse(courseRequestDTO);

        // Then
        assertThat(result).isEqualTo(courseResponseDTO);
        //verify(course).setAuthors(authors);
        verify(mapper).toEntityForCreate(courseRequestDTO);
        verify(authorRepo).findAllById(authorIds);
        verify(courseRepo).save(course);
        verify(mapper).toResponseDTO(course);
    }

    @Test
    @DisplayName("Should handle null description gracefully")
    void addCourse_ShouldHandleNullDescription() {
        // Given
        courseRequestDTO.setDescription(null);
        List<Author> authors = Arrays.asList(author);

        when(mapper.toEntityForCreate(courseRequestDTO)).thenReturn(course);
        when(authorRepo.findAllById(courseRequestDTO.getAuthorIds())).thenReturn(authors);
        when(courseRepo.save(course)).thenReturn(course);
        when(mapper.toResponseDTO(course)).thenReturn(courseResponseDTO);

        // When
        CourseResponseDTO result = courseService.addCourse(courseRequestDTO);

        // Then
        assertThat(result).isNotNull();
        verify(courseRepo).save(course);
    }

    @Test
    @DisplayName("Should return empty page when no courses exist")
    void getCoursesDTOPaginated_ShouldReturnEmptyPage_WhenNoCoursesExist() {
        // Given
        int page = 0;
        int size = 10;
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<Course> emptyPage = new PageImpl<>(List.of());

        when(courseRepo.findAll(pageRequest)).thenReturn(emptyPage);

        // When
        Page<CourseResponseDTO> result = courseService.getCoursesDTOPaginated(page, size);

        // Then
        assertThat(result.getContent()).isEmpty();
        assertThat(result.getTotalElements()).isZero();
        verify(courseRepo).findAll(pageRequest);
    }
}