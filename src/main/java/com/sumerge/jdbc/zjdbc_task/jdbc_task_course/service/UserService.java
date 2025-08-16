package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;


import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.AppUser;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.UserRegistrationDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AppUserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final AppUserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public UserService(AppUserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    public void registerUser(UserRegistrationDTO dto) {
        if (userRepo.findByUsername(dto.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        AppUser user = new AppUser();
        user.setUsername(dto.getUsername());
        user.setPassword(passwordEncoder.encode(dto.getPassword()));
        user.setRole(dto.getRole());

        userRepo.save(user);
    }
}


