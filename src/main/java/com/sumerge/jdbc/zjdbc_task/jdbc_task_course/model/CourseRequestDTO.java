package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import java.util.Objects;

@Data
public class CourseRequestDTO {

   // private Integer id;

    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    private String name;

    private String description;

    @Min(value = 1, message = "Credit must be at least 1")
    private int credit;

    private int authorId;

    public CourseRequestDTO(/*Integer id,*/ String name, String description, int credit, int authorId) {
    //    this.id = id;
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.authorId = authorId;
    }
    public CourseRequestDTO() {
        // Default constructor for testing and serialization
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CourseRequestDTO courseRequestDTO = (CourseRequestDTO) o;
        return credit == courseRequestDTO.credit && authorId == courseRequestDTO.authorId &&  Objects.equals(name, courseRequestDTO.name) && Objects.equals(description, courseRequestDTO.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(/*id,*/ name, description, credit, authorId);
    }
}
