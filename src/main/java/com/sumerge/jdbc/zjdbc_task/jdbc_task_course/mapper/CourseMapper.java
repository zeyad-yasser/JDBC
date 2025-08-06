package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseDTO toDTO(Course course);

    Course toEntity(CourseDTO courseDTO);
}
