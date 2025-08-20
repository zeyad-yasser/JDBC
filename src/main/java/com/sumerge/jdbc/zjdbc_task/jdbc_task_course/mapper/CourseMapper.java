package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import org.mapstruct.*;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    CourseMapper INSTANCE = Mappers.getMapper(CourseMapper.class);

    CourseDTO toDTO(Course course);

    Course toEntity(CourseDTO dto);

    @Mapping(target = "id", ignore = true)
    Course toEntityForCreate(CourseDTO dto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void updateCourseFromDTO(CourseDTO dto, @MappingTarget Course course);

}
