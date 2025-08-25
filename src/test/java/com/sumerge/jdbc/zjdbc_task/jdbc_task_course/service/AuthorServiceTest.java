package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.AuthorNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.AuthorMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.AuthorDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import java.time.LocalDate;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthorServiceTest {

    @Mock
    private AuthorRepo authorRepo;

    @Mock
    private AuthorMapper authorMapper;

    @InjectMocks
    private AuthorService authorService;

    private Author testAuthor;
    private AuthorDTO testAuthorDTO;

    @BeforeEach
    void setUp() {
        // Create test Author entity
        testAuthor = new Author();
        testAuthor.setAuthorId(1);
        testAuthor.setAuthorName("Zeyad Yasser");
        testAuthor.setAuthorEmail("zeyad.yasser@example.com");
        testAuthor.setAuthorBirthdate(LocalDate.of(1980, 1, 1));

        // Create test AuthorDTO
        testAuthorDTO = new AuthorDTO();
        testAuthorDTO.setAuthorName("Zeyad Yasser");
        testAuthorDTO.setAuthorEmail("zeyad.yasser@example.com");
        testAuthorDTO.setAuthorBirthdate(LocalDate.of(1980, 1, 1));
    }

    //  getAuthorById Tests
    @Test
    void getAuthorById_WhenAuthorExists_ShouldReturnAuthorDTO() {
        // Given
        int authorId = 1;
        when(authorRepo.findById(authorId)).thenReturn(Optional.of(testAuthor));
        when(authorMapper.toDto(testAuthor)).thenReturn(testAuthorDTO);

        // When
        AuthorDTO result = authorService.getAuthorById(authorId);

        // Then
        assertNotNull(result);
        assertEquals("Zeyad Yasser", result.getAuthorName());
        assertEquals("zeyad.yasser@example.com", result.getAuthorEmail());
        assertEquals(LocalDate.of(1980, 1, 1), result.getAuthorBirthdate());

        verify(authorRepo, times(1)).findById(authorId);
        verify(authorMapper, times(1)).toDto(testAuthor);
    }

    @Test
    void getAuthorById_WhenAuthorDoesNotExist_ShouldThrowAuthorNotFoundException() {
        // Given
        int nonExistentAuthorId = 999;
        when(authorRepo.findById(nonExistentAuthorId)).thenReturn(Optional.empty());

        // When & Then
        AuthorNotFoundException exception = assertThrows(
                AuthorNotFoundException.class,
                () -> authorService.getAuthorById(nonExistentAuthorId)
        );

        assertEquals("Author not found with id: " + nonExistentAuthorId, exception.getMessage());
        verify(authorRepo, times(1)).findById(nonExistentAuthorId);
        verify(authorMapper, never()).toDto(any());
    }

    // createAuthor Tests
    @Test
    void createAuthor_WhenValidAuthorDTO_ShouldReturnSavedAuthorDTO() {
        // Given
        Author mappedAuthor = new Author();
        mappedAuthor.setAuthorName("Zeyad Yasser");
        mappedAuthor.setAuthorEmail("zeyad.yasser@example.com");
        mappedAuthor.setAuthorBirthdate(LocalDate.of(1980, 1, 1));

        Author savedAuthor = new Author();
        savedAuthor.setAuthorId(1);
        savedAuthor.setAuthorName("Zeyad Yasser");
        savedAuthor.setAuthorEmail("zeyad.yasser@example.com");
        savedAuthor.setAuthorBirthdate(LocalDate.of(1980, 1, 1));

        when(authorMapper.toEntity(testAuthorDTO)).thenReturn(mappedAuthor);
        when(authorRepo.save(mappedAuthor)).thenReturn(savedAuthor);
        when(authorMapper.toDto(savedAuthor)).thenReturn(testAuthorDTO);

        // When
        AuthorDTO result = authorService.createAuthor(testAuthorDTO);

        // Then
        assertNotNull(result);
        assertEquals("Zeyad Yasser", result.getAuthorName());
        assertEquals("zeyad.yasser@example.com", result.getAuthorEmail());
        assertEquals(LocalDate.of(1980, 1, 1), result.getAuthorBirthdate());

        verify(authorMapper, times(1)).toEntity(testAuthorDTO);
        verify(authorRepo, times(1)).save(mappedAuthor);
        verify(authorMapper, times(1)).toDto(savedAuthor);
    }

    @Test
    void createAuthor_WhenRepositoryFails_ShouldPropagateException() {
        // Given
        Author mappedAuthor = new Author();
        when(authorMapper.toEntity(testAuthorDTO)).thenReturn(mappedAuthor);
        when(authorRepo.save(mappedAuthor)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> authorService.createAuthor(testAuthorDTO)
        );

        assertEquals("Database error", exception.getMessage());
        verify(authorMapper, times(1)).toEntity(testAuthorDTO);
        verify(authorRepo, times(1)).save(mappedAuthor);
        verify(authorMapper, never()).toDto(any());
    }

    // updateAuthor Tests
    @Test
    void updateAuthor_WhenAuthorExists_ShouldReturnUpdatedAuthorDTO() {
        // Given
        int authorId = 1;
        Author existingAuthor = new Author();
        existingAuthor.setAuthorId(authorId);
        existingAuthor.setAuthorName("Old Name");
        existingAuthor.setAuthorEmail("old.email@example.com");

        Author updatedAuthor = new Author();
        updatedAuthor.setAuthorId(authorId);
        updatedAuthor.setAuthorName("Zeyad Yasser");
        updatedAuthor.setAuthorEmail("zeyad.yasser@example.com");
        updatedAuthor.setAuthorBirthdate(LocalDate.of(1980, 1, 1));

        when(authorRepo.findById(authorId)).thenReturn(Optional.of(existingAuthor));
        when(authorRepo.save(existingAuthor)).thenReturn(updatedAuthor);
        when(authorMapper.toDto(updatedAuthor)).thenReturn(testAuthorDTO);

        // When
        AuthorDTO result = authorService.updateAuthor(authorId, testAuthorDTO);

        // Then
        assertNotNull(result);
        assertEquals("Zeyad Yasser", result.getAuthorName());
        assertEquals("zeyad.yasser@example.com", result.getAuthorEmail());
        assertEquals(LocalDate.of(1980, 1, 1), result.getAuthorBirthdate());

        verify(authorRepo, times(1)).findById(authorId);
        verify(authorMapper, times(1)).updateAuthorFromDto(testAuthorDTO, existingAuthor);
        verify(authorRepo, times(1)).save(existingAuthor);
        verify(authorMapper, times(1)).toDto(updatedAuthor);
    }

    @Test
    void updateAuthor_WhenAuthorDoesNotExist_ShouldThrowAuthorNotFoundException() {
        // Given
        int nonExistentAuthorId = 999;
        when(authorRepo.findById(nonExistentAuthorId)).thenReturn(Optional.empty());

        // When & Then
        AuthorNotFoundException exception = assertThrows(
                AuthorNotFoundException.class,
                () -> authorService.updateAuthor(nonExistentAuthorId, testAuthorDTO)
        );

        assertEquals("Author not found with id: " + nonExistentAuthorId, exception.getMessage());
        verify(authorRepo, times(1)).findById(nonExistentAuthorId);
        verify(authorMapper, never()).updateAuthorFromDto(any(), any());
        verify(authorRepo, never()).save(any());
        verify(authorMapper, never()).toDto(any());
    }

    //  deleteAuthor Tests
    @Test
    void deleteAuthor_WhenAuthorExists_ShouldDeleteSuccessfully() {
        // Given
        int authorId = 1;
        when(authorRepo.existsById(authorId)).thenReturn(true);

        // When
        authorService.deleteAuthor(authorId);

        // Then
        verify(authorRepo, times(1)).existsById(authorId);
        verify(authorRepo, times(1)).deleteById(authorId);
    }

    @Test
    void deleteAuthor_WhenAuthorDoesNotExist_ShouldThrowAuthorNotFoundException() {
        // Given
        int nonExistentAuthorId = 999;
        when(authorRepo.existsById(nonExistentAuthorId)).thenReturn(false);

        // When & Then
        AuthorNotFoundException exception = assertThrows(
                AuthorNotFoundException.class,
                () -> authorService.deleteAuthor(nonExistentAuthorId)
        );

        assertEquals("Author not found with id: " + nonExistentAuthorId, exception.getMessage());
        verify(authorRepo, times(1)).existsById(nonExistentAuthorId);
        verify(authorRepo, never()).deleteById(any());
    }
}