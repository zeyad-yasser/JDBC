package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.AuthorMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.AuthorDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/authors")
@Tag(name = "Authors", description = "CRUD operations for authors")
public class AuthorController {

    private final AuthorRepo authorRepo;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorController(AuthorRepo authorRepo, AuthorMapper authorMapper) {
        this.authorRepo = authorRepo;
        this.authorMapper = authorMapper;
    }

    @Operation(summary ="Get All Authors")
    @GetMapping
    public List<AuthorDTO> getAllAuthors() {
        return authorRepo.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AuthorDTO> getAuthorById(@PathVariable int id) {
        return authorRepo.findById(id)
                .map(authorMapper::toDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<AuthorDTO> createAuthor(@Valid @RequestBody AuthorDTO dto) {
        Author saved = authorRepo.save(authorMapper.toEntity(dto));
        return ResponseEntity.status(HttpStatus.CREATED).body(authorMapper.toDto(saved));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorDTO> updateAuthor(@PathVariable int id, @Valid @RequestBody AuthorDTO dto) {
        return authorRepo.findById(id)
                .map(existing -> {
                    authorMapper.updateAuthorFromDto(dto, existing);
                    Author updated = authorRepo.save(existing);
                    return ResponseEntity.ok(authorMapper.toDto(updated));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAuthor(@PathVariable int id) {
        if (!authorRepo.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        authorRepo.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
