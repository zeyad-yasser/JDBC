package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@DataJpaTest
@Import(CourseServiceImpl.class)
public class CourseServiceTest {

    @Autowired
    private CourseServiceImpl courseService;

    @Autowired
    private CourseRepo repo;

    @BeforeEach
    void setUp() {
        // Clear database before each test
        repo.deleteAll();
    }

    @Test
    void testAddCourse()
    {
        // Let the database handle ID generation
        Course course = new Course();
        course.setName("Test");
        course.setDescription("Test Desc");
        course.setCredit(3);
        course.setAuthor_id(1);

        CourseDTO courseDTO = new CourseDTO(123,"TestCourseName", "Test Course Desc", 3, 1);
        courseService.addCourse(courseDTO);

        // Get the generated ID from the saved course
        int savedId = course.getId();
        Course result = courseService.viewCourse(savedId);
        assertEquals("Test", result.getName());
    }

    @Test
    void testViewCourseNotFound()
    {
        assertThrows(CourseNotFoundException.class, ()-> courseService.viewCourse(100));
    }
}
