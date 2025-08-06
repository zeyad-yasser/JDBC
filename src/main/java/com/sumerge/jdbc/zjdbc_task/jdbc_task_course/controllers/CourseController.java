package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;


import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

//    @Autowired
    public CourseController(CourseService courseService) {

        this.courseService = courseService;
    }

    @GetMapping("/discover")
    public List<Course> discoverCourses()
    {
        return courseService.getRecommendedCourses();
    }

    @GetMapping("/{id}")
    public Course viewCourse(@PathVariable int id){
        return courseService.viewCourse(id);
    }

    @PostMapping
    public void addCourse(@RequestBody Course course)
    {
        courseService.addCourse(course);
    }
    @PutMapping("/{id}")
    public void updateCourse(@PathVariable int id, @RequestBody Course course){
        course.setId(id);
        courseService.updateCourse(course);
    }
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id)
    {
        courseService.deleteCourse(id);
    }

    @GetMapping
    public Page<Course> getCourse(@RequestParam(defaultValue ="0")int page, @RequestParam(defaultValue = "10")int size) {
        return courseService.getCoursesPaginated(page, size);
    }

    @GetMapping("/by-author")
    public List<Course> getCoursesByAuthorEmail(@RequestParam String email){
        return courseService.getCourseByAuthorEmail(email);
    }
}
