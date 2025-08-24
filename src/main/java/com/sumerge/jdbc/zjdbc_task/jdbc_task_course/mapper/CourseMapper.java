package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    
    @Mapping(target = "authorId", source = "author.authorId")
    CourseDTO toDTO(Course course);

    @Mapping(target = "author", ignore = true)
    Course toEntity(CourseDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Course toEntityForCreate(CourseDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", ignore = true)
    void updateCourseFromDTO(CourseDTO dto, @MappingTarget Course course);

}
