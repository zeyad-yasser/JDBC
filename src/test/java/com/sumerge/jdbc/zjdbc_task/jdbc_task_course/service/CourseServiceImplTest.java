package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.exception.CourseNotFoundException;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.mapper.CourseMapper;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo.CourseRepo;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CourseServiceImplTest {

    @Mock
    private CourseRepo courseRepo;

    @Mock
    private CourseMapper courseMapper;

    @InjectMocks
    private CourseServiceImpl courseService;

    @Test
    void testGetCourseDTO_Success() {
        Course course = new Course( "Java", "Desc", 3, 1);
        CourseDTO dto = new CourseDTO(1, "Java", "Desc", 3, 1);

        when(courseRepo.findById(1)).thenReturn(Optional.of(course));
        when(courseMapper.toDTO(course)).thenReturn(dto);

        CourseDTO result = courseService.getCourseDTO(1);
        assertEquals("Java", result.getName());
        assertEquals(3, result.getCredit());
    }

    @Test
    void testGetCourseDTO_NotFound() {
        when(courseRepo.findById(999)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseDTO(999));
    }

    @Test
    void testAddCourse_Success() {
        CourseDTO dto = new CourseDTO(null, "Spring", "Spring Boot", 4, 2);
        Course entity = new Course( "Spring", "Spring Boot", 4, 2);
        Course saved = new Course( "Spring", "Spring Boot", 4, 2);
        CourseDTO resultDTO = new CourseDTO(10, "Spring", "Spring Boot", 4, 2);

        when(courseMapper.toEntityForCreate(dto)).thenReturn(entity);
        when(courseRepo.save(entity)).thenReturn(saved);
        when(courseMapper.toDTO(saved)).thenReturn(resultDTO);

        CourseDTO result = courseService.addCourse(dto);
        assertEquals(10, result.getId());
        assertEquals("Spring", result.getName());
    }
}
