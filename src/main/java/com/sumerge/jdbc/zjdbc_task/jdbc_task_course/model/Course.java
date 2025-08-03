package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model;

import javax.swing.plaf.SplitPaneUI;

public class Course {
    int id;
    String name;
    String description;
    int credit;
    int author_id;

    public Course(int i, String javaProgramming, String learnJavaFromScratch, int i1, int i2) {

    }

    public  Course(){}
    public int getId() {
        return id;
    }

    public void setId(int id) {
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
