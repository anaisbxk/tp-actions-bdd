
CREATE TABLE students (
    id    SERIAL PRIMARY KEY,
    name  VARCHAR(100) NOT NULL,
    grade NUMERIC(4,2) NOT NULL
);

INSERT INTO students (name, grade) VALUES ('Amel', 15);
INSERT INTO students (name, grade) VALUES ('Karim', 12);
