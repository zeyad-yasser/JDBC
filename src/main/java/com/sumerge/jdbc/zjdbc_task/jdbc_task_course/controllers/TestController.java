package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
@Tag(name = "Test", description = "Test endpoints")
public class TestController {

    @Operation(summary = "Test endpoint", description = "Simple test endpoint to verify API docs")
    @GetMapping
    public String test() {
        return "API is working!";
    }
}
