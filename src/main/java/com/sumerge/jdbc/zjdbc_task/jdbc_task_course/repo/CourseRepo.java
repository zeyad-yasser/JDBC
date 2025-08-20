package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.repo;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.entity.Course;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepo extends JpaRepository<Course, Integer> {


    @Query(value = """
    SELECT c.* FROM course c
    JOIN rating r ON c.id = r.course_id
    GROUP BY c.id
    ORDER BY AVG(r.number) DESC
""", countQuery = """
    SELECT COUNT(DISTINCT c.id) FROM course c
    JOIN rating r ON c.id = r.course_id
""", nativeQuery = true)
    Page<Course> findTopRatedCourses(Pageable pageable);
    @Query(value = """
    SELECT c.* FROM course c 
    JOIN author a ON c.author_id = a.id 
    WHERE a.email = :email
""", nativeQuery = true)
    List<Course> findByAuthorEmail(@Param("email") String email);


}
