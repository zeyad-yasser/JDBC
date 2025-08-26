package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.ToString;

@Data
@ToString
public class AppUserDTO
{
    @NotNull
    private Role role;
}

