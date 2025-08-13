package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.security.service;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {
    @Override
    public UserDetails loadUserByUsername (String username) throws UsernameNotFoundException
    {
        if ("admin".equals(username))
        {
            return User.builder()
                    .username("admin")
                    .password("{noop}password")
                    .roles("ADMIN")
                    .build();
        }
        
        if ("user".equals(username))
        {
            return User.builder()
                    .username("user")
                    .password("{noop}1234")
                    .roles("USER")
                    .build();
        }
        throw new UsernameNotFoundException("User not found");
    }
}
