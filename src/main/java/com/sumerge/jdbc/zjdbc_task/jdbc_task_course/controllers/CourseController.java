package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;


import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMapper courseMapper;

    @Autowired
    public CourseController(CourseService courseService, CourseMapper courseMapper) {
        this.courseService = courseService;
        this.courseMapper = courseMapper;
    }

    @GetMapping("/discover")
    public List<Course> discoverCourses()
    {
        return courseService.getRecommendedCourses();
    }

    @GetMapping("/{id}")
    public CourseDTO viewCourse(@PathVariable int id) {
        Course course = courseService.viewCourse(id);
        return courseMapper.toDTO(course);
    }

    @PostMapping
    public void addCourse(@RequestBody CourseDTO courseDTO)
    {
        Course  course = courseMapper.toEntity(courseDTO);
        courseService.addCourse(course);
    }

    @PutMapping("/{id}")
    public void updateCourse(@PathVariable int id, @RequestBody CourseDTO courseDTO){
        courseDTO.setId(id);
        Course course = courseMapper.toEntity(courseDTO);
        courseService.updateCourse(course);
    }
    @DeleteMapping("/{id}")
    public void deleteCourse(@PathVariable int id)
    {
        courseService.deleteCourse(id);
    }

    @GetMapping
    public Page<CourseDTO> getCourse(@RequestParam(defaultValue ="0")int page, @RequestParam(defaultValue = "10")int size) {
        return courseService.getCoursesPaginated(page, size).map(courseMapper::toDTO);
    }

    @GetMapping("/by-author")
    public List<CourseDTO> getCoursesByAuthorEmail(@RequestParam String email){
    List<Course> courses = courseService.getCourseByAuthorEmail(email);
    return courses.stream().map(courseMapper::toDTO).toList();
    }
}
