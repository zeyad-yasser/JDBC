-- ==============================================
-- H2-COMPATIBLE SCHEMA FOR TESTING
-- ==============================================

-- Author table (UPDATED COLUMN NAMES TO MATCH ENTITY)
CREATE TABLE IF NOT EXISTS author (
                                      author_id INT PRIMARY KEY AUTO_INCREMENT,
                                      author_name VARCHAR(255) NOT NULL,
    author_email VARCHAR(255) NOT NULL,
    author_birthdate DATE NOT NULL
    );

-- Course table (NO CHANGES - keeping author_id for backward compatibility)
CREATE TABLE course (
                        id BIGINT AUTO_INCREMENT PRIMARY KEY,
                        name VARCHAR(255) NOT NULL,
                        description VARCHAR(255),
                        credit INT
    -- ⚠️ no author_id here anymore
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
CREATE TABLE course_author (
                               course_id BIGINT NOT NULL,
                               author_id BIGINT NOT NULL,
                               PRIMARY KEY (course_id, author_id),
                               CONSTRAINT fk_ca_course FOREIGN KEY (course_id) REFERENCES course(id),
                               CONSTRAINT fk_ca_author FOREIGN KEY (author_id) REFERENCES author(author_id)
);


