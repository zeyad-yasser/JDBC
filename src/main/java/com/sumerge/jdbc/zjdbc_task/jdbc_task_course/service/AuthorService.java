package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.AuthorNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.AuthorMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.AuthorDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthorService {

    private final AuthorRepo authorRepo;
    private final AuthorMapper authorMapper;

    @Autowired
    public AuthorService(AuthorRepo authorRepo, AuthorMapper authorMapper) {
        this.authorRepo = authorRepo;
        this.authorMapper = authorMapper;
    }

    public List<AuthorDTO> getAllAuthors() {
        return authorRepo.findAll().stream()
                .map(authorMapper::toDto)
                .collect(Collectors.toList());
    }

    public AuthorDTO getAuthorById(int id) {
        return authorRepo.findById(id)
                .map(authorMapper::toDto)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
    }

    public AuthorDTO createAuthor(AuthorDTO dto) {
        Author saved = authorRepo.save(authorMapper.toEntity(dto));
        return authorMapper.toDto(saved);
    }

    public AuthorDTO updateAuthor(int id, AuthorDTO dto) {
        Author existing = authorRepo.findById(id)
                .orElseThrow(() -> new AuthorNotFoundException("Author not found with id: " + id));
        authorMapper.updateAuthorFromDto(dto, existing);
        Author updated = authorRepo.save(existing);
        return authorMapper.toDto(updated);
    }

    public void deleteAuthor(int id) {
        if (!authorRepo.existsById(id)) {
            throw new AuthorNotFoundException("Author not found with id: " + id);
        }
        authorRepo.deleteById(id);
    }
}

