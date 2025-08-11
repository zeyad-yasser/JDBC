package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import org.springframework.stereotype.Component;

@Component
public class CourseMapper {

    public CourseDTO toDTO(Course course) {
        if (course == null) return null;
        return new CourseDTO(
                course.getId(),
                course.getName(),
                course.getDescription(),
                course.getCredit(),
                course.getAuthor_id()
        );
    }

    public Course toEntity(CourseDTO courseDTO) {
        if (courseDTO == null) return null;
        Course course = new Course();
        if (courseDTO.getId() != null) {
            course.setId(courseDTO.getId());
        }
        course.setName(courseDTO.getName());
        course.setDescription(courseDTO.getDescription());
        course.setCredit(courseDTO.getCredit());
        course.setAuthor_id(courseDTO.getAuthorId());
        return course;
    }
}
