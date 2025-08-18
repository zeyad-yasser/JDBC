package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.JdbcTaskCourseApplication;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import org.junit.jupiter.api.*;
import org.springframework.http.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.TestMethodOrder;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;

//import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;


@SpringBootTest(classes = JdbcTaskCourseApplication.class)
@AutoConfigureMockMvc
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class CourseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private static CourseDTO sampleCourse;

    @BeforeAll
    static void setup() {
        sampleCourse = new CourseDTO();
        sampleCourse.setName("Integration Test Course");
        sampleCourse.setDescription("Test Description");
        sampleCourse.setCredit(3);
        sampleCourse.setAuthorId(1); // Adjust based on your DB seeding
    }
    @Test
    void contextLoads() {
        Assertions.assertNotNull(mockMvc, "MockMvc should be injected");
    }

    //  Create Course
    @Test
    @Order(1)
    void shouldCreateCourse() throws Exception {
        mockMvc.perform(post("/courses")
                        .header("x-validation-report", "true")
                        .with(httpBasic("user2", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Integration Test Course"));
    }

    //   Get All Courses
    @Test
    @Order(2)
    void shouldGetAllCourses() throws Exception {
        mockMvc.perform(get("/courses"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray());
    }

    //  Update Course
    @Test
    @Order(3)
    void shouldUpdateCourse() throws Exception {
        sampleCourse.setName("Updated Course Name");

        mockMvc.perform(put("/courses/1")
                        .header("x-validation-report", "true")
                        .with(httpBasic("user2", "user123"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(sampleCourse)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Course Name"));
    }

    // Delete Course
    @Test
    @Order(4)
    void shouldDeleteCourse() throws Exception {
        mockMvc.perform(delete("/courses/1")
                        .header("x-validation-report", "true")
                        .with(httpBasic("user2", "user123")))
                .andExpect(status().isOk());
    }
}
