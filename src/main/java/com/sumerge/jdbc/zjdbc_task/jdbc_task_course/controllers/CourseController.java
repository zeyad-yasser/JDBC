package com.sumerge.jdbc.zjdbc_task.jdbc_task_course.controllers;

import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseRequestDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.model.CourseResponseDTO;
import com.sumerge.jdbc.zjdbc_task.jdbc_task_course.service.CourseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/courses")
@Tag(name = "Courses", description = "Course management endpoints")
public class CourseController {

    private final CourseService courseService;

    @Autowired
    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    @Operation(summary = "Discover recommended courses", description = "Returns a list of recommended courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved recommended courses"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    // ResponseDTO
    @GetMapping("/discover")
    public ResponseEntity<List<CourseResponseDTO>> discoverCourses() {
        List<CourseResponseDTO> recommendedCourses = courseService.getRecommendedCourses();
        return ResponseEntity.ok(recommendedCourses);
    }

    @Operation(summary = "Get course by ID", description = "Returns course details for a specific ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved course"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
   //ResponseDTO
    @GetMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> viewCourse(@PathVariable int id) {
        CourseResponseDTO courseResponseDTO = courseService.getCourseDTO(id);
        return ResponseEntity.ok(courseResponseDTO);
    }

    @Operation(summary = "Add a new course", description = "Creates a new course with the provided details")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Course successfully created"),
            @ApiResponse(responseCode = "400", description = "Invalid course data provided")
    })
    /*@PostMapping
    public ResponseEntity<Void> addCourse(@Valid @RequestBody CourseDTO courseDTO) {
        courseService.addCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }*/

    @PostMapping
    public ResponseEntity<CourseResponseDTO> addCourse(@Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        CourseResponseDTO saved = courseService.addCourse(courseRequestDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @Operation(summary = "Update an existing course", description = "Updates a course with the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Course successfully updated"),
            @ApiResponse(responseCode = "400", description = "Invalid course data provided"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @PutMapping("/{id}")
    public ResponseEntity<CourseResponseDTO> updateCourse(@PathVariable int id, @Valid @RequestBody CourseRequestDTO courseRequestDTO) {
        courseService.updateCourse(id, courseRequestDTO);
        return ResponseEntity.ok().body(courseService.getCourseDTO(id));
    }

    @Operation(summary = "Delete a course", description = "Deletes the course with the given ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Course successfully deleted"),
            @ApiResponse(responseCode = "404", description = "Course not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable int id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Get paginated courses", description = "Returns a paginated list of courses")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    @GetMapping
    public ResponseEntity<Page<CourseRequestDTO>> getCourses(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        Page<CourseRequestDTO> courseDTOs = courseService.getCoursesDTOPaginated(page, size);
        return ResponseEntity.ok(courseDTOs);
    }

    @Operation(summary = "Get courses by author email", description = "Returns courses by a specific author email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully retrieved courses")
    })
    @GetMapping("/author")
    public ResponseEntity<List<CourseRequestDTO>> getCoursesByAuthorEmail(@RequestParam @Email String email) {
        List<CourseRequestDTO> courseRequestDTOS = courseService.getCoursesDTOByAuthorEmail(email);
        return ResponseEntity.ok(courseRequestDTOS);
    }


}
