package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CourseService courseService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testViewCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO(1, "java", "Desc", 3, 1);
        when(courseService.getCourseDTO(1)).thenReturn(courseDTO);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("java"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    @Test
    void testViewCourseNotFound() throws Exception {
        when(courseService.getCourseDTO(999))
                .thenThrow(new CourseNotFoundException("Course with ID: 999 not found"));

        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDiscoverCourses() throws Exception {
        Course course1 = new Course("Java", "Java Course", 3, 1);
        course1.setId(1);

        Course course2 = new Course("Python", "Python Course", 4, 2);
        course2.setId(2);

        List<Course> courses = List.of(course1, course2);
        when(courseService.getRecommendedCourses()).thenReturn(courses);

        mockMvc.perform(get("/courses/discover"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[1].name").value("Python"));
    }
}
