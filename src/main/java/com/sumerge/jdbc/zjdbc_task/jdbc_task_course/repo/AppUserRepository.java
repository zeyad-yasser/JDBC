package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {
    Optional<AppUser> findByUsername(String username);
}

