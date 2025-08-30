package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity;
import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@Data
@Entity
@Table(name = "author")
public class Author {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   @Column(name = "author_id", nullable = false)
    private int authorId;

   @Column(name= "author_name", nullable = false)
    private String authorName;

    @Column(name= "author_email", nullable = false, unique = true)
    private String authorEmail;

    @Column(name= "author_birthdate", nullable = false)
    private LocalDate authorBirthdate;

    @ManyToMany(mappedBy = "authors" )
    private List<Course> courses = new ArrayList<>();

    public Author(int authorId, String authorName, String authorEmail, LocalDate authorBirthdate, List<Course> courses) {
        this.authorId = authorId;
        this.authorName = authorName;
        this.authorEmail = authorEmail;
        this.authorBirthdate = authorBirthdate;
        this.courses = courses;
    }
    public Author() {}


   @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Author author = (Author) o;
        return authorId == author.authorId && Objects.equals(authorName, author.authorName) && Objects.equals(authorEmail, author.authorEmail) && Objects.equals(authorBirthdate, author.authorBirthdate) && Objects.equals(courses, author.courses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authorId, authorName, authorEmail, authorBirthdate, courses);
    }
}
