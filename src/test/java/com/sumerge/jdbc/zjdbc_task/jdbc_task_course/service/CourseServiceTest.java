package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class CourseServiceTest {

    @Autowired
    private CourseService courseService;

    @Autowired
    private CourseRepo repo;

    @Test
    void testAddCourse()
    {
        Course course = new Course(10,"Test","Test Desc", 3,1);
        courseService.addCourse(course);
        Course result = courseService.viewCourse(10);
        assertEquals("Test", result.getName());
    }
    @Test
    void testViewCourseNotFound()
    {
        assertThrows(CourseNotFoundException.class, ()-> courseService.viewCourse(100));
    }

}
