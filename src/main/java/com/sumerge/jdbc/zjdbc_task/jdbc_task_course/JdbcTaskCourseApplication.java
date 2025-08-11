package com.sumerge.jdbc.zjdbc_task.jdbc_task_course;


import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseServiceImpl;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class JdbcTaskCourseApplication {
	public static void main(String[] args) {
		SpringApplication.run(JdbcTaskCourseApplication.class, args);
	}

	// Removed GroupedOpenApi bean to simplify configuration

	@Bean
	public CommandLineRunner demo(CourseServiceImpl courseService, CourseRepo courseRepo) {
		return args -> {

            /*Author author = new Author(1, "Zeyad Yasser", "zeyad.yasser@example.com", LocalDate.of(1980, 1, 1));
            authorRepo.save(author);
*/
            CourseDTO course1 = new CourseDTO(1, "Java Programming", "Learn Java from scratch", 3, 1);
            CourseDTO course2 = new CourseDTO(2, "Cpp Programming", "Learn Cpp from scratch", 3, 2);
            CourseDTO course3 = new CourseDTO(3, "Python Programming", "Learn Python from scratch", 3,1 );
            CourseDTO course4 = new CourseDTO(4, "RUST Programming", "Learn RUST from scratch", 3, 3);
			courseService.addCourse(course1);
            courseService.addCourse(course2);
            courseService.addCourse(course3);
            courseService.addCourse(course4);


			Course retrieved = courseService.viewCourse(1);
			System.out.println(retrieved);

			//Course retrieved_II = courseService.viewCourse(7);
			//System.out.println(retrieved_II);

			//courseService.deleteCourse(1);
		};
	}
}
