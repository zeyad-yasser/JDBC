package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.security.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ValidationHeaderFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException
    {
        String header = request.getHeader("x-validation-report");
        String path = request.getRequestURI();
        // how to bypass GET requests in a OncePerRequestFilter
        if (path.startsWith("/courses/") && request.getMethod().equals("GET"))
        {
            filterChain.doFilter(request, response);
            return;
        }
        if(!"true".equalsIgnoreCase(header))
        {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Missing or Invalid x-validation-report header");
            return;
        }
        filterChain.doFilter(request, response);
    }
}
