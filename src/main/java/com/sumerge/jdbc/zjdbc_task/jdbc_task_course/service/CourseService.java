package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseService {

    @Autowired
    CourseRepo repo;

    //private final JdbcTemplate template;
    private final CourseMapper courseMapper;
    @Autowired
    public CourseService(CourseMapper courseMapper) {
        this.courseMapper= courseMapper;
    }

    public void addCourse(Course course) {
        repo.save(course);
    }

    public void updateCourse(Course course) {
        repo.save(course);
    }
    public List<Course> viewAllCourses()
    {
        return repo.findAll();
    }
    public Course viewCourse(int courseId) {

       return repo.findById(courseId).orElse(new Course());
    }

    public void deleteCourse(int courseId) {

        repo.deleteById(courseId);
    }
    public List<Course> getRecommendedCourses() {
               return repo.findTopRatedCourses(PageRequest.of(0,5)).getContent();
    }

    public Page<Course> getCoursesPaginated(int page, int size){
        return repo.findAll(PageRequest.of(page,size));
    }

    public List<Course>getCourseByAuthorEmail(String email){
        return repo.findByAuthorEmail(email);
    }
}
