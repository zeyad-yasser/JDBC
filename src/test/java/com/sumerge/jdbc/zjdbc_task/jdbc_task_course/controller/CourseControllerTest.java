package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controller;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers.CourseController;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CourseService courseService;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CourseService courseService() {
            return Mockito.mock(CourseService.class);
        }
    }

    @Test
    void testViewCourse() throws Exception {
        Course course = new Course(1, "java", "Desc", 3, 1);
        Mockito.when(courseService.viewCourse(1)).thenReturn(course);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("java"));
    }

    @Test
    void testViewCourseNotFound() throws Exception {
        Mockito.when(courseService.viewCourse(999))
                .thenThrow(new CourseNotFoundException("Course not found"));

        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Course not found"));
    }
}
