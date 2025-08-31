-- Insert authors
INSERT INTO author (author_name, author_email, author_birthdate) VALUES
                                                                     ('Zeyad Yasser', 'zeyad.yasser@example.com', '1980-01-01'),
                                                                     ('Samy Youssef', 'samy.youssef@example.com', '1990-05-15'),
                                                                     ('Mohamed Tarek', 'mohamed.tarek@example.com', '1985-09-20'),
                                                                     ('Samy Amry', 'samy.amry@example.com', '1990-05-15');

-- Insert courses
INSERT INTO course (name, description, credit) VALUES
                                                   ('Java Basics', 'Introductory course for Java programming.', 3),
                                                   ('Spring Boot Deep Dive', 'Advanced course on Spring Boot framework.', 4),
                                                   ('Data Science with Python', 'Learn data analysis and visualization.', 3),
                                                   ('AI Fundamentals', 'Introduction to Artificial Intelligence.', 2);

-- Insert ratings
INSERT INTO rating (number, course_id) VALUES
                                           (5, 1),
                                           (4, 1),
                                           (5, 2),
                                           (3, 3);

-- Insert assessments
INSERT INTO assessment (content, course_id) VALUES
                                                ('Final Exam - Java Basics', 1),
                                                ('Midterm Quiz - Spring Boot', 2),
                                                ('Capstone Project - Data Science', 3),
                                                ('AI Assignment: Decision Trees', 4);

-- Insert join table relationships (link courses to authors)
INSERT INTO course_author (course_id, author_id) VALUES
                                                     (1, 1), -- Java Basics by Zeyad
                                                     (2, 1), -- Spring Boot Deep Dive by Zeyad
                                                     (3, 2), -- Data Science with Python by Samy Youssef
                                                     (4, 3); -- AI Fundamentals by Mohamed Tarek
