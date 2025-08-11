package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import jakarta.persistence.*;

@Entity
@Table(name = "course")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ Ensures DB handles ID generation
    private Integer id;

    @Column(nullable = false)
    private String name;

    private String description;

    private int credit;

    @Column(name = "author_id", nullable = false)
    private int author_id;

    public Course() {}

    public Course(String name, String description, int credit, int author_id) {
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.author_id = author_id;
    }

    // ✅ Getters and setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCredit() {
        return credit;
    }

    public void setCredit(int credit) {
        this.credit = credit;
    }

    public int getAuthor_id() {
        return author_id;
    }

    public void setAuthor_id(int author_id) {
        this.author_id = author_id;
    }

    @Override
    public String toString() {
        return "Course{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", credit=" + credit +
                ", author_id=" + author_id +
                '}';
    }
}
