-- ==============================================
-- ORIGINAL DATA (COMPLETELY UNCHANGED)
-- ==============================================

-- Author table (EXACTLY THE SAME)
INSERT   INTO author (name, email, birthdate) VALUES
                                                ('Zeyad Yasser', 'zeyad.yasser@example.com', '1980-01-01'),
                                                ('Samy Youssef', 'samy.youssef@example.com', '1990-05-15'),
                                                ('Mohamed Tarek', 'mohamed.tarek@example.com', '1985-09-20'),
                                                ('Samy Amry', 'samy.amry@example.com', '1990-05-15');

-- Course table (EXACTLY THE SAME - keeping author_id)
INSERT INTO course (name, description, credit, author_id) VALUES
                                                              ('Java Basics', 'Introductory course for Java programming.', 3, 1),
                                                              ('Spring Boot Deep Dive', 'Advanced course on Spring Boot framework.', 4, 1),
                                                              ('Data Science with Python', 'Learn data analysis and visualization.', 3, 2),
                                                              ('AI Fundamentals', 'Introduction to Artificial Intelligence.', 2, 3);

-- Rating table (EXACTLY THE SAME)
INSERT   INTO rating (number, course_id) VALUES
                                           (5, 1),
                                           (4, 1),
                                           (5, 2),
                                           (3, 3);

-- Assessment table (EXACTLY THE SAME)
INSERT INTO assessment (content, course_id) VALUES
                                                ('Final Exam - Java Basics', 1),
                                                ('Midterm Quiz - Spring Boot', 2),
                                                ('Capstone Project - Data Science', 3),
                                                ('AI Assignment: Decision Trees', 4);

-- ==============================================
-- ONLY NEW ADDITION: POPULATE JOIN TABLE
-- ==============================================

-- Automatically populate join table from existing course.author_id
-- This will run after the course data is inserted
INSERT INTO course_author (course_id, author_id)
SELECT id, author_id FROM course;