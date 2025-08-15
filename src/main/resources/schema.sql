CREATE TABLE IF NOT EXISTS author (
    ID INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255),
    email VARCHAR(255),
    birthdate DATE
    );

CREATE TABLE IF NOT EXISTS course (
    id INT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(100) NOT NULL,
    description VARCHAR(255),
    credit INT,
    author_id INT NOT NULL,
    FOREIGN KEY (author_id) REFERENCES Author(ID)
    );

CREATE TABLE IF NOT EXISTS rating (
    id INT PRIMARY KEY AUTO_INCREMENT,
    number INT,
    course_id INT,
    FOREIGN KEY (course_id) REFERENCES Course(id)
    );

CREATE TABLE IF NOT EXISTS assessment (
     id INT PRIMARY KEY AUTO_INCREMENT,
     content TEXT,
     course_id INT,
     FOREIGN KEY (course_id) REFERENCES Course(id)
    );
