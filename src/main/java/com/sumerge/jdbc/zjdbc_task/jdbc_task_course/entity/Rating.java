package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

@Entity
@Data
@ToString
@Table(name = "rating")
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int number;

    @ManyToOne
    @JoinColumn(name = "courseId", nullable = false)
    private Course course;

    public Rating() {}

    public Rating(int number, Course course) {
        this.number = number;
        this.course = course;
    }
}