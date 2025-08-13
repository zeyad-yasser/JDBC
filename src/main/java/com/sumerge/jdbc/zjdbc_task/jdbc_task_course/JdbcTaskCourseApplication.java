package com.sumerge.jdbc.zjdbc_task.jdbc_task_course;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;


@SpringBootApplication(scanBasePackages = "com.sumerge.jdbc.zjdbc_task.jdbc_task_course")

public class JdbcTaskCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(JdbcTaskCourseApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CourseServiceImpl courseService) {
        return args -> {
            CourseDTO course1 = new CourseDTO(null, "Java Programming", "Learn Java from scratch", 3, 1);
            CourseDTO course2 = new CourseDTO(null, "Cpp Programming", "Learn Cpp from scratch", 3, 2);
            CourseDTO course3 = new CourseDTO(null, "Python Programming", "Learn Python from scratch", 3, 1);
            CourseDTO course4 = new CourseDTO(null, "RUST Programming", "Learn RUST from scratch", 3, 3);

            courseService.addCourse(course1);
            courseService.addCourse(course2);
            courseService.addCourse(course3);
            courseService.addCourse(course4);

            // Optional: log inserted courses
            // courseService.getCoursesDTOByAuthorEmail("author@example.com").forEach(c -> System.out.println(c.getName()));
        };
    }
}
