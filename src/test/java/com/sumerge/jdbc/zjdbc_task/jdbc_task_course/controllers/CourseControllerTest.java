package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.boot.test.mock.mockito.MockBean;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@WebMvcTest(CourseController.class)
public class CourseControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CourseServiceImpl courseService;

    @Test
    void testViewCourseSuccess() throws Exception {
        CourseDTO dto = new CourseDTO(1, "Java", "Desc", 3, 1);
        when(courseService.getCourseDTO(1)).thenReturn(dto);

        mockMvc.perform(get("/courses/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Java"));
    }

    @Test
    void testViewCourseNotFound() throws Exception {
        when(courseService.getCourseDTO(999))
                .thenThrow(new CourseNotFoundException("Course with ID: 999 not found"));

        mockMvc.perform(get("/courses/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.error").value("Course not found"));
    }

    @Test
    void testAddCourseValidationError() throws Exception {
        CourseDTO invalid = new CourseDTO(null, "", "desc", 0, 1);

        mockMvc.perform(post("/courses")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name").value("Course name is required"))
                .andExpect(jsonPath("$.credit").value("Credit must be at least 1"));
    }

}
