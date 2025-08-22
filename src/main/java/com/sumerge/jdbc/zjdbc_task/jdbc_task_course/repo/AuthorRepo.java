package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AuthorRepo extends JpaRepository<Author, Integer> {
    Optional<Author> findByAuthorEmail(String email);}
