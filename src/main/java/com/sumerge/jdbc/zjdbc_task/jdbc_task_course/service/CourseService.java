package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CourseService {
    List<Course> getRecommendedCourses();
    Course viewCourse(int id);                 // used in CommandLineRunner
    CourseDTO getCourseDTO(int id);            // used by controller
    CourseDTO addCourse(CourseDTO courseDTO);
    void updateCourse(int id, CourseDTO courseDTO);
    void deleteCourse(int id);
    Page<CourseDTO> getCoursesDTOPaginated(int page, int size);
    List<CourseDTO> getCoursesDTOByAuthorEmail(String email);
}
