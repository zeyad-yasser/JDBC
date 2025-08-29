package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.ToString;

import java.util.Objects;

@ToString
@Data
public class CourseResponseDTO {
    private Integer id;

    @NotBlank(message = "Course name is required")
    @Size(min = 2, max = 100, message = "Course name must be between 2 and 100 characters")
    private String name;

    private String description;

    @Min(value = 1, message = "Credit must be at least 1")
    private int credit;

    private int authorId;
    public CourseResponseDTO(Integer id, String name, String description, int credit, int authorId) {
        //    this.id = id;
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.authorId = authorId;
    }
    public CourseResponseDTO() {
        // Default constructor for testing and serialization
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        CourseResponseDTO that = (CourseResponseDTO) o;
        return credit == that.credit && authorId == that.authorId && Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(description, that.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, credit, authorId);
    }
}
