package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class UserRegistrationDTO {
    @NotBlank
    private String username;

    @NotBlank
    private String password;

    @NotBlank
    private String role; // e.g., "ROLE_USER", "ROLE_ADMIN"

}
