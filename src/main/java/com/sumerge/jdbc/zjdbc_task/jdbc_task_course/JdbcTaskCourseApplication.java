package com.sumerge.jdbc.zjdbc_task.jdbc_task_course;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;


@SpringBootApplication
public class JdbcTaskCourseApplication {
	public static void main(String[] args) {
		SpringApplication.run(JdbcTaskCourseApplication.class, args);
	}

	@Bean
	public CommandLineRunner demo(CourseService courseService) {
		return args -> {
			Course course = new Course(7, "Java Programming", "Learn Java from scratch", 3, 1);
			courseService.addCourse(course);

			course.setName("Advanced Java");
			courseService.updateCourse(course);

			Course retrieved = courseService.viewCourse(1);
			System.out.println(retrieved);

			Course retrieved_II = courseService.viewCourse(7);
			System.out.println(retrieved_II);

			courseService.deleteCourse(1);
		};
	}
}
