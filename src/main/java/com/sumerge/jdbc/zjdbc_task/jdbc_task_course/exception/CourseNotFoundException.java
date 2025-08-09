package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(String message) {
        super(message);
    }
}
