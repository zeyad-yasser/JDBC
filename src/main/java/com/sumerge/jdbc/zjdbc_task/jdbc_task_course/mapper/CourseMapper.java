package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    
    @Mapping(target = "authorId", source = "author.authorId")
    CourseRequestDTO toRequestDTO(Course course);

    @Mapping(target = "author", ignore = true)
    Course toEntity(CourseRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "author", ignore = true)
    Course toEntityForCreate(CourseRequestDTO dto);

    List<CourseResponseDTO> toDTOList (List<Course> courses);


    @Mapping(target = "authorId", source = "author.authorId")
    CourseResponseDTO toResponseDTO(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "author", ignore = true)
    void updateCourseFromDTO(CourseRequestDTO dto, @MappingTarget Course course);

}
