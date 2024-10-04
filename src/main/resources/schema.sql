CREATE TABLE `spring-batch`.extracted_student_info_entity (
    id INTEGER NOT NULL AUTO_INCREMENT,
    score INTEGER NOT NULL,
    student_name VARCHAR(255),
    PRIMARY KEY (id)
);
CREATE TABLE `spring-batch`.student_score_entity (
    id INTEGER NOT NULL AUTO_INCREMENT,
    age INTEGER NOT NULL,
    gender VARCHAR(255),
    name VARCHAR(255),
    school_name VARCHAR(255),
    score INTEGER NOT NULL,
    PRIMARY KEY (id)
)


