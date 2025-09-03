package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponseDTO {
    private String username;
    private Role role;
    private String message;
}
