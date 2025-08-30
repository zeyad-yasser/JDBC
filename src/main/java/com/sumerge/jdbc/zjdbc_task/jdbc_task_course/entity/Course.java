package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.sql.Array;
import java.util.ArrayList;
import java.util.List;
@Data
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

    @OneToOne(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private Assessment assessment;

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "course_author",
            joinColumns = @JoinColumn(name="course_id"),
            inverseJoinColumns = @JoinColumn(name="author_id")
            )
    private List<Author> authors = new ArrayList<>();
    public void addAuthor(Author author) {
        if (authors == null) {
            authors = new ArrayList<>();
        }
        if (!authors.contains(author)) {
            authors.add(author);
            // Make sure the other side of the relationship is updated too
            if (author.getCourses() != null && !author.getCourses().contains(this)) {
                author.getCourses().add(this);
            }
        }
    }
    public void removeAuthor(Author author) {
        if (authors != null) {
            authors.remove(author);
            // Make sure the other side of the relationship is updated too
            if (author.getCourses() != null) {
                author.getCourses().remove(this);
            }
        }
    }

    public Course() {this.authors = new ArrayList<>(); }
    public Course(String name, String description, int credit, Author author) {
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.authors = new ArrayList<>();
        if(author != null) {
            this.authors.add(author);
        }

    }
    public Course(String name, String description, int credit, List<Author> authors) {
        this.name = name;
        this.description = description;
        this.credit = credit;
        this.authors = new ArrayList<>();
        if (authors != null) {
            for (Author author : authors) {
                addAuthor(author);
            }
        }
    }
}