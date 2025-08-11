CREATE TABLE Author (
  ID INT PRIMARY KEY AUTO_INCREMENT,
  name VARCHAR(255),
  email VARCHAR(255),
  birthdate DATE
);

CREATE TABLE Course (
   id INT PRIMARY KEY AUTO_INCREMENT,
   name VARCHAR(100) NOT NULL,
   description VARCHAR(255),
   credit INT,
   author_id INT NOT NULL,
   FOREIGN KEY (author_id) REFERENCES Author(ID)
);

CREATE TABLE Rating (
  id INT PRIMARY KEY AUTO_INCREMENT,
  number INT,
  course_id INT,
  FOREIGN KEY (course_id) REFERENCES Course(id)
);

CREATE TABLE Assessment (
  id INT PRIMARY KEY AUTO_INCREMENT,
  content TEXT,
  course_id INT,
  FOREIGN KEY (course_id) REFERENCES Course(id)
);
