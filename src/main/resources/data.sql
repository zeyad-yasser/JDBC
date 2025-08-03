INSERT INTO Author (id, name, email, birthdate) VALUES
      (1, 'Ali Mansour', 'ali.mansour@example.com', '1980-01-01'),
      (2, 'Sara Youssef', 'sara.youssef@example.com', '1990-05-15'),
      (3, 'Mohamed Tarek', 'mohamed.tarek@example.com', '1985-09-20');

INSERT INTO Course (id, name, description, credit, author_id) VALUES
      (1, 'Java Basics', 'Introductory course for Java programming.', 3, 1),
      (2, 'Spring Boot Deep Dive', 'Advanced course on Spring Boot framework.', 4, 1),
      (3, 'Data Science with Python', 'Learn data analysis and visualization.', 3, 2),
      (4, 'AI Fundamentals', 'Introduction to Artificial Intelligence.', 2, 3);

INSERT INTO Rating (id, number, course_id) VALUES
       (1, 5, 1),
       (2, 4, 1),
       (3, 5, 2),
       (4, 3, 3),
       (5, 4, 4);

INSERT INTO Assessment (id, content, course_id) VALUES
      (1, 'Final Exam - Java Basics', 1),
      (2, 'Midterm Quiz - Spring Boot', 2),
      (3, 'Capstone Project - Data Science', 3),
      (4, 'AI Assignment: Decision Trees', 4);
