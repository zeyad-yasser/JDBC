/*
package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseResponseDTO;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface CourseMapper {


    @Mapping(target = "authorIds",
            expression = "java(course.getAuthors().stream().map(Author::getAuthorId).toList())")
    CourseRequestDTO toRequestDTO(Course course);

    @Mapping(target = "authors", ignore = true)
    //Normal Mapping Creates NEW Object
    Course toEntity(CourseRequestDTO dto);

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "authors", ignore = true)
    Course toEntityForCreate(CourseRequestDTO dto);

    List<CourseResponseDTO> toDTOList (List<Course> courses);


    @Mapping(target = "authorIds",
            expression = "java(course.getAuthors().stream().map(Author::getAuthorId).toList())")
    CourseResponseDTO toResponseDTO(Course course);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    @Mapping(target = "authors", ignore = true)
    void updateCourseFromDTO(CourseRequestDTO dto, @MappingTarget Course course);
    //  Helper to extract IDs
    default List<Integer> mapAuthorsToIds(List<Author> authors) {
        return authors == null ? null : authors.stream()
                .map(Author::getAuthorId)
                .toList();
    }
}
----------------------------------------------------------------------------------------------
*/

package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring")
public interface CourseMapper {

    // Convert Course → RequestDTO
    @Mapping(target = "authorIds", expression = "java(mapAuthorsToIds(course.getAuthors()))")
    CourseRequestDTO toRequestDTO(Course course);

    // Convert RequestDTO → Course (for update)
    Course toEntity(CourseRequestDTO dto);

    // Convert RequestDTO → Course (for create)
    @Mapping(target = "id", ignore = true)
    Course toEntityForCreate(CourseRequestDTO dto);

    // Convert list of Courses → list of ResponseDTOs
    List<CourseResponseDTO> toDTOList(List<Course> courses);

    // Convert Course → ResponseDTO
    @Mapping(target = "authorIds", expression = "java(mapAuthorsToIds(course.getAuthors()))")
    CourseResponseDTO toResponseDTO(Course course);

    // Update existing Course entity from DTO
    void updateCourseFromDTO(CourseRequestDTO dto, @MappingTarget Course course);

    //  Helper method to extract Author IDs
    default List<Integer> mapAuthorsToIds(List<Author> authors) {
        return authors == null ? null :
                authors.stream()
                        .map(Author::getAuthorId)
                        .collect(Collectors.toList());
    }
}

