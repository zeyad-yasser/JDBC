package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Objects;
@Data
@ToString
public class AuthorDTO {

    @NotBlank(message = "Author name is required")
    private String authorName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Author email is required")
    private String authorEmail;

    @NotNull(message = "Birthdate is required")
    @Past(message = "Birthdate must be in the past")
    private LocalDate authorBirthdate;


    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        AuthorDTO authorDTO = (AuthorDTO) o;
        return Objects.equals(authorName, authorDTO.authorName) && Objects.equals(authorEmail, authorDTO.authorEmail) && Objects.equals(authorBirthdate, authorDTO.authorBirthdate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorName, authorEmail, authorBirthdate);
    }
}
