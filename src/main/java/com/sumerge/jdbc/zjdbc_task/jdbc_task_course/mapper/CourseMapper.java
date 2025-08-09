package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    @Mapping(source= "author_id", target= "authorId")
    CourseDTO toDTO(Course course);

    @Mapping(source= "authorId", target= "author_id")
    Course toEntity(CourseDTO courseDTO);
}
