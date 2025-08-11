package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
//import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CourseController.class)
public class CourseControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseServiceImpl courseServiceImpl;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testViewCourse() throws Exception {
        CourseDTO courseDTO = new CourseDTO(1, "java", "Desc", 3, 1);
        when(courseServiceImpl.getCourseDTO(1)).thenReturn(courseDTO);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("java"))
                .andExpect(jsonPath("$.description").value("Desc"));
    }

    @Test
    void testViewCourseNotFound() throws Exception {
        when(courseServiceImpl.getCourseDTO(999))
                .thenThrow(new CourseNotFoundException("Course with ID: 999 not found"));

        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound());
    }

    @Test
    void testDiscoverCourses() throws Exception {
        List<Course> courses = Arrays.asList(
                new Course(1, "Java", "Java Course", 3, 1),
                new Course(2, "Python", "Python Course", 4, 2)
        );
        when(courseServiceImpl.getRecommendedCourses()).thenReturn(courses);

        mockMvc.perform(get("/courses/discover"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Java"))
                .andExpect(jsonPath("$[1].name").value("Python"));
    }

    // ... other tests (add/update/delete/getPage/by-author) similar to your existing ones,
    // but ensure they mock the exact service methods the controller calls.
}
