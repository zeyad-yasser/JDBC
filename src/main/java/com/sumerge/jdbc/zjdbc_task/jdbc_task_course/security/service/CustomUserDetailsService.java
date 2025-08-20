package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.security.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.AppUser;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AppUserRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final AppUserRepository userRepo;

    public CustomUserDetailsService(AppUserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Should be encoded!
                .roles(user.getRole().replace("ROLE_", "")) // Spring expects role without prefix here
                .build();
    }
}

