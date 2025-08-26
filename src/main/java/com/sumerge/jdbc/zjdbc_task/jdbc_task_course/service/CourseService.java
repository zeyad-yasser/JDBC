package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.AuthorNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
//import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.LegacyCourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.AuthorRepo;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CourseService {

    private final CourseRepo courseRepo;
    private final CourseMapper mapper;
    private final AuthorRepo authorRepo;


    @Autowired
    public CourseService(CourseRepo courseRepo, CourseMapper mapper, AuthorRepo authorRepo) {
        this.courseRepo = courseRepo;
        this.mapper = mapper;
        this.authorRepo = authorRepo;
    }

    public List<CourseDTO> getRecommendedCourses() {
        Page<Course> page = courseRepo.findTopRatedCourses(PageRequest.of(0, 10));
        return page.getContent().stream()
                .map(mapper::toDTO)
                .toList();
    }

    public Course viewCourse(int id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
    }

    public CourseDTO getCourseDTO(int id) {
        return courseRepo.findById(id)
                .map(mapper::toDTO)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
    }

    @Transactional
    public CourseDTO addCourse(CourseDTO courseDTO) {

        Course course = mapper.toEntityForCreate(courseDTO);
        Author author = authorRepo.findById(courseDTO.getAuthorId())
                .orElseThrow(()-> new AuthorNotFoundException("Author with ID: " + courseDTO.getAuthorId() + " not found"));
        course.setAuthor(author);
        Course saved = courseRepo.save(course);
        return mapper.toDTO(saved);
    }

    @Transactional
    public void updateCourse(int id, CourseDTO courseDTO) {

        //look up @mappingtarget
        Course existing = courseRepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
        mapper.updateCourseFromDTO(courseDTO, existing);

        Author author = authorRepo.findById(courseDTO.getAuthorId()).orElseThrow(()->new AuthorNotFoundException("Author with ID: " + courseDTO.getAuthorId() + " not found"));
        existing.setAuthor(author);

       courseRepo.save(existing);

    }

    @Transactional
    public void deleteCourse(int id) {
        if (!courseRepo.existsById(id)) {
            throw new CourseNotFoundException("Course with ID: " + id + " not found");
        }
        courseRepo.deleteById(id);
    }

    public Page<CourseDTO> getCoursesDTOPaginated(int page, int size) {
        Page<Course> p = courseRepo.findAll(PageRequest.of(page, size));
        return p.map(mapper::toDTO);
    }

    public List<CourseDTO> getCoursesDTOByAuthorEmail(String email) {
        return courseRepo.findByAuthorEmail(email);
    }
}
