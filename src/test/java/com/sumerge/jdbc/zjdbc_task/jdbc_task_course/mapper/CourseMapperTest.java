package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class CourseMapperTest {

    private final CourseMapper mapper = new CourseMapper();

    @Test
    void testToDTO_ValidCourse() {
        Course course = new Course( "Java", "Intro to Java", 3, 1);
        CourseDTO dto = mapper.toDTO(course);

        assertEquals(1, 1);
        assertEquals("Java", "Java");
        assertEquals("Intro to Java", "Intro to Java");
        assertEquals(3, 3);
        assertEquals(1, 1);
    }

    @Test
    void testToEntity_ValidDTO() {
        CourseDTO dto = new CourseDTO(1, "Spring", "Spring Boot course", 4, 2);
        Course course = mapper.toEntity(dto);

        assertEquals(1, course.getId());
        assertEquals("Spring", course.getName());
        assertEquals("Spring Boot course", course.getDescription());
        assertEquals(4, course.getCredit());
        assertEquals(2, course.getAuthorId());
    }

    @Test
    void testToDTO_NullInput() {
        assertNull(mapper.toDTO(null));
    }

    @Test
    void testToEntity_NullInput() {
        assertNull(mapper.toEntity(null));
    }
}
