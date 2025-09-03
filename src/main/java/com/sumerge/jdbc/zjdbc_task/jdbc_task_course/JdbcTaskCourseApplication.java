package com.sumerge.jdbc.zjdbc_task.jdbc_task_course;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;


//@SpringBootApplication()

@SpringBootApplication
@EnableJpaRepositories(basePackages = "com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo")
@EntityScan(basePackages = "com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity")
@Profile("!test")
public class JdbcTaskCourseApplication {
    public static void main(String[] args) {
        SpringApplication.run(JdbcTaskCourseApplication.class, args);
    }

    @Bean
    public CommandLineRunner demo(CourseService courseService) {
        return args -> {

          //CourseDTO course1 = new CourseDTO(null, "Java Programming", "Learn Java from scratch", 3, 1);
          //CourseDTO course2 = new CourseDTO(null, "Cpp Programming", "Learn Cpp from scratch", 3, 2);
          //CourseDTO course3 = new CourseDTO(null, "Python Programming", "Learn Python from scratch", 3, 1);
          //CourseDTO course4 = new CourseDTO(null, "RUST Programming", "Learn RUST from scratch", 3, 3);

          //courseService.addCourse(course1);
          //courseService.addCourse(course2);
          //courseService.addCourse(course3);
          //courseService.addCourse(course4);

          // Optional: log inserted courses

          /* courseService.getCoursesDTOByAuthorEmail("author@example.com").
                   forEach(c -> System.out.println(c.getName()));

           */
            /*
          String raw = "admin123";
          String encoded = new BCryptPasswordEncoder().encode(raw);
          System.out.println(encoded);
            */
        };
    }
}

