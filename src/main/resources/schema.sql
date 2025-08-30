-- ==============================================
-- ORIGINAL SCHEMA (UNCHANGED)
-- ==============================================

-- Author table (NO CHANGES)
CREATE TABLE IF NOT EXISTS author (
                                      author_id INT PRIMARY KEY AUTO_INCREMENT,
                                      name VARCHAR(255) NOT NULL,
    email VARCHAR(255) NOT NULL,
    birthdate DATE NOT NULL
    );

-- Course table (NO CHANGES - keeping author_id for backward compatibility)
CREATE TABLE IF NOT EXISTS course (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    credit INT NOT NULL,
    author_id INT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES author(author_id)
    );

-- Rating table (NO CHANGES)
CREATE TABLE IF NOT EXISTS rating (
                                      id INT PRIMARY KEY AUTO_INCREMENT,
                                      number INT NOT NULL,
                                      course_id INT NOT NULL,
                                      FOREIGN KEY (course_id) REFERENCES course(id)
    );

-- Assessment table (NO CHANGES)
CREATE TABLE IF NOT EXISTS assessment (
                                          id INT PRIMARY KEY AUTO_INCREMENT,
                                          content TEXT NOT NULL,
                                          course_id INT NOT NULL,
                                          FOREIGN KEY (course_id) REFERENCES course(id)
    );

-- Users table (NO CHANGES)
CREATE TABLE IF NOT EXISTS users (
                                     id BIGINT PRIMARY KEY AUTO_INCREMENT,
                                     username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL
    );

-- ==============================================
-- ONLY NEW ADDITION: JOIN TABLE
-- ==============================================

-- NEW: Course-Author join table (ONLY ADDITION)
CREATE TABLE IF NOT EXISTS course_author (
                                             course_id INT NOT NULL,
                                             author_id INT NOT NULL,
                                             PRIMARY KEY (course_id, author_id),
    FOREIGN KEY (course_id) REFERENCES course(id) ON DELETE CASCADE,
    FOREIGN KEY (author_id) REFERENCES author(author_id) ON DELETE CASCADE
    );

-- ==============================================
-- POPULATE JOIN TABLE WITH EXISTING DATA
-- ==============================================

-- Auto-populate join table from existing course.author_id relationships
INSERT IGNORE INTO course_author (course_id, author_id)
SELECT id, author_id
FROM course
WHERE author_id IS NOT NULL;