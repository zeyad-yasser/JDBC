package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CourseServiceImpl implements CourseService {

    private final CourseRepo courseRepo;
    private final CourseMapper mapper;

    @Autowired
    public CourseServiceImpl(CourseRepo courseRepo, CourseMapper mapper) {
        this.courseRepo = courseRepo;
        this.mapper = mapper;
    }

    @Override
    public List<Course> getRecommendedCourses() {
        Page<Course> page = courseRepo.findTopRatedCourses(PageRequest.of(0, 10));
        return page.getContent();
    }

    @Override
    public Course viewCourse(int id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
    }

    @Override
    public CourseDTO getCourseDTO(int id) {
        return courseRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
    }

    @Override
    @Transactional
    public void addCourse(CourseDTO courseDTO) {
        Course course = mapper.toEntityForCreate(courseDTO);
        courseRepo.save(course);
    }

    @Override
    @Transactional
    public void updateCourse(int id, CourseDTO courseDTO) {
        Course existing = courseRepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
        existing.setName(courseDTO.getName());
        existing.setDescription(courseDTO.getDescription());
        existing.setCredit(courseDTO.getCredit());
        existing.setAuthor_id(courseDTO.getAuthorId());
        courseRepo.save(existing);
    }

    @Override
    @Transactional
    public void deleteCourse(int id) {
        if (!courseRepo.existsById(id)) {
            throw new CourseNotFoundException("Course with ID: " + id + " not found");
        }
        courseRepo.deleteById(id);
    }

    @Override
    public Page<CourseDTO> getCoursesDTOPaginated(int page, int size) {
        Page<Course> p = courseRepo.findAll(PageRequest.of(page, size));
        return p.map(mapper::toDTO);
    }

    @Override
    public List<CourseDTO> getCoursesDTOByAuthorEmail(String email) {
        return courseRepo.findByAuthorEmail(email).stream()
                .map(mapper::toDTO)
                .toList();
    }
}
