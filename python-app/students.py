
import psycopg2


def add_student(conn, name, grade):
    with conn.cursor() as cur:
        cur.execute(
            "INSERT INTO students (name, grade) VALUES (%s, %s) RETURNING id",
            (name, grade)
        )
        return cur.fetchone()[0]


def get_student(conn, student_id):
    with conn.cursor() as cur:
        cur.execute(
            "SELECT id, name, grade FROM students WHERE id = %s",
            (student_id,)
        )
        return cur.fetchone()


def average_grade(conn):
    with conn.cursor() as cur:
        cur.execute("SELECT AVG(grade) FROM students")
        result = cur.fetchone()[0]
        return float(result) if result is not None else 0.0
