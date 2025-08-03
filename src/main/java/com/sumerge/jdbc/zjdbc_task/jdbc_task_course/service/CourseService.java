package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;
@Service
public class CourseService {

    private final JdbcTemplate template;

    @Autowired
    public CourseService(JdbcTemplate template) {
        this.template = template;
    }

    public void addCourse(Course course) {
        String sql = "INSERT INTO Course (id, name, description, credit, author_id) VALUES (?, ?, ?, ?, ?)";
        template.update(sql, course.getId(), course.getName(), course.getDescription(), course.getCredit(), course.getAuthor_id());
    }

    public void updateCourse(Course course) {
        String sql = "UPDATE Course SET name = ?, description = ?, credit = ?, author_id = ? WHERE id = ?";
        template.update(sql, course.getName(), course.getDescription(), course.getCredit(), course.getAuthor_id(), course.getId());
    }

    public Course viewCourse(int courseId) {
        String sql = "SELECT * FROM Course WHERE id = ?";
        RowMapper<Course> mapper = (rs, rowNum) -> {
            Course hold = new Course();
            hold.setId(rs.getInt(1));
            hold.setName(rs.getString(2));
            hold.setDescription(rs.getString(3));
            hold.setCredit(rs.getInt(4));
            hold.setAuthor_id(rs.getInt(5));
            return hold;
        };
        try {
            return template.queryForObject(sql, mapper, courseId);
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    public void deleteCourse(int courseId) {
        String deleteRatings = "DELETE FROM Rating WHERE course_id = ?";
        template.update(deleteRatings, courseId);

        String deleteAssessments = "DELETE FROM Assessment WHERE course_id = ?";
        template.update(deleteAssessments, courseId);

        String deleteCourse = "DELETE FROM Course WHERE id = ?";
        template.update(deleteCourse, courseId);
    }

}
