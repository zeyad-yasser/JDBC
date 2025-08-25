package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
@Data
@ToString
@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // DB auto-generates ID
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;
    private int credit;
    /*
    @Column(name = "authorId", nullable = false)
    private int authorId;*/

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Assessment> assessments = new ArrayList<>();

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private Author author;

    public Course() {
    }

    public Course(String name, String description, int credit, Author author) {
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.author = author;
    }

}