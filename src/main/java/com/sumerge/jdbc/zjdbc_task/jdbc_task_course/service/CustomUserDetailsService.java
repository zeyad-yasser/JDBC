package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Role;
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

    public CustomUserDetailsService(AppUserRepository userRepo)
    {
        this.userRepo = userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        AppUser user = userRepo.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // Here, you’re converting your own AppUser into Spring Security’s UserDetails object.
        // I used "User.builder()" or I Could Make "AppUser Extends UserDetails."
        // I Don't know which one is better.
        return User.builder()
                .username(user.getUsername())
                .password(user.getPassword()) // Should be encoded!
                .roles(user.getRole().name()) // Spring expects role without prefix here
                .build();
    }
    private String extractRoleName(Role role) {
        return role.name().replace("ROLE_", ""); // Convert enum name to role name
    }
}


/*
*<< Authentication Flow with this service >>

 happens when someone logs in:

 1. User hits /login with username + password.

 3. Spring Security calls CustomUserDetailsService.loadUserByUsername(username).

 4. Your service fetches AppUser from the database.

 5. Converts it into UserDetails (Spring’s own format).

 5. Spring Security compares the entered raw password with the encoded password from the DB using your configured PasswordEncoder.

 6. If valid → authentication success, user is logged in with their roles.

 7. If invalid → Spring throws BadCredentialsException.
*/

