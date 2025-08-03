package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Service;

import java.sql.ResultSet;
import java.sql.SQLException;

@Service
public class CourseService {
    private JdbcTemplate template;

    public void addCourse(Course course) {
        String sql = "Insert into Course (id, name, description, credit, author_id) values (?,?,?,?,?)";
        template.update(sql, course.getId(), course.getName(), course.getDescription(), course.getCredit(), course.getAuthor_id());
    }
    public void updateCourse(Course course){
        String sql = "Update Course SET name = ?, description = ?, credit = ?, author_id =? Where id = ?";
        template.update(sql);
    }
    public Course viewCourse(int courseId){
        String sql = "Select * from Course Where id = ?";
        RowMapper<Course> mapper = new RowMapper<Course>() {
            @Override
            public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
                Course hold = new Course();
                hold.setId(rs.getInt("id"));
                hold.setName(rs.getString("name"));
                hold.setDescription(rs.getString("description"));
                hold.setCredit(rs.getInt("credit"));
                hold.setAuthor_id(rs.getInt("author_id"));
                return hold;
            }
        };
        return template.queryForObject(sql,mapper,courseId);
    }

    public void deleteCourse(int courseId )
    {
     String sql = "Delete from Course Where id =?" ;
     template.update(sql,courseId);
    }
}
