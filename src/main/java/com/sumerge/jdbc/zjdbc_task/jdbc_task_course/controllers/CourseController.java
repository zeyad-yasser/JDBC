package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;


import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

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


}
