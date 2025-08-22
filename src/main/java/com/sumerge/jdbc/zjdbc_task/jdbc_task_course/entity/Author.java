package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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

    @OneToMany(mappedBy = "author", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Course> courses = new ArrayList<>();

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getAuthorEmail() {
        return authorEmail;
    }

    public void setAuthorEmail(String authorEmail) {
        this.authorEmail = authorEmail;
    }

    public LocalDate getAuthorBirthdate() {
        return authorBirthdate;
    }

    public void setAuthorBirthdate(LocalDate authorBirthdate) {
        this.authorBirthdate = authorBirthdate;
    }

    public List<Course> getCourses() {
        return courses;
    }

    public void setCourses(List<Course> courses) {
        this.courses = courses;
    }

    @Override
    public String toString() {
        return "Author{" +
                "authorId=" + authorId +
                ", authorName='" + authorName + '\'' +
                ", authorEmail='" + authorEmail + '\'' +
                ", authorBirthdate=" + authorBirthdate +
                ", courses=" + courses +
                '}';
    }

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
