package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Author;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.AuthorNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
//import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.LegacyCourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseResponseDTO;
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

    public List<CourseResponseDTO> getRecommendedCourses()
    {
        /*
        Page<Course> page = courseRepo.findTopRatedCourses(PageRequest.of(0, 10));
        return page.getContent().stream()
                .map(mapper::toDTO)
                .toList();*/

        List<Course> courses = courseRepo.findAll();
        return mapper.toDTOList(courses);  // <<< This Method From The Mapper
    /*
          This is working well using "toDTOList" from Mapper
          Returning List<Course> Without Using .toList(),
          Check If this what "Hussain" meant Or Not.
        &
           - Which one is better Using it from the Mapper
           or Say .toList() in the Service
           List<Course> courses = courseRepo.findAll();
           return mapper.toDTOList(courses);
     */
    }

// This used only in Unit test: Replace it later " Ignore this, Not IN Production"
    public Course viewCourse(int id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
    }
// Here So, What is the difference between this and making one DTO, ....
//          Contains ID and Ignore it at Creation / Requests
    public CourseResponseDTO getCourseDTO(int id) {
        return courseRepo.findById(id)
                .map(mapper::toResponseDTO).orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
    }

    @Transactional
    public CourseResponseDTO addCourse(CourseRequestDTO courseRequestDTO) {

        Course course = mapper.toEntityForCreate(courseRequestDTO);
        Author author = authorRepo.findById(courseRequestDTO.getAuthorId())
                .orElseThrow(()-> new AuthorNotFoundException("Author with ID: " + courseRequestDTO.getAuthorId() + " not found"));
        course.setAuthor(author);
        Course saved = courseRepo.save(course);
        return mapper.toResponseDTO(saved);
    }

    @Transactional
    public CourseResponseDTO updateCourse(int id, CourseRequestDTO courseRequestDTO) {

        //look up @mappingtarget
        Course existing = courseRepo.findById(id).orElseThrow(() -> new CourseNotFoundException("Course with ID: " + id + " not found"));
        mapper.updateCourseFromDTO(courseRequestDTO, existing);

        Author author = authorRepo.findById(courseRequestDTO.getAuthorId()).orElseThrow(()->new AuthorNotFoundException("Author with ID: " + courseRequestDTO.getAuthorId() + " not found"));
        existing.setAuthor(author);

       courseRepo.save(existing);
       return  mapper.toResponseDTO(existing);

    }

    @Transactional
    public void deleteCourse(int id) {
        if (!courseRepo.existsById(id)) {
            throw new CourseNotFoundException("Course with ID: " + id + " not found");
        }
        courseRepo.deleteById(id);
    }

    public Page<CourseRequestDTO> getCoursesDTOPaginated(int page, int size) {
        Page<Course> p = courseRepo.findAll(PageRequest.of(page, size));
        return p.map(mapper::toRequestDTO);
    }

    public List<CourseRequestDTO> getCoursesDTOByAuthorEmail(String email) {
        return courseRepo.findByAuthorEmail(email);
    }
}
