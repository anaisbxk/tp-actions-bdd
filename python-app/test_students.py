
import os
import psycopg2
import pytest

from students import add_student, get_student, average_grade


@pytest.fixture
def conn():
    connection = psycopg2.connect(
        host=os.getenv("DB_HOST", "localhost"),
        database=os.getenv("DB_NAME", "tp"),
        user=os.getenv("DB_USER", "postgres"),
        password=os.getenv("DB_PASSWORD", "postgres"),
        port=os.getenv("DB_PORT", "5432")
    )

    with connection.cursor() as cur:
        cur.execute("TRUNCATE TABLE students RESTART IDENTITY CASCADE")

    connection.commit()

    yield connection

    connection.close()


def test_add_and_get_student(conn):
    student_id = add_student(conn, "Sara", 16)

    student = get_student(conn, student_id)

    assert student[1] == "Sara"
    assert float(student[2]) == 16.0


def test_get_nonexistent_student(conn):
    student = get_student(conn, 9999)

    assert student is None


def test_average_grade(conn):
    add_student(conn, "Amel", 10)
    add_student(conn, "Karim", 14)

    assert average_grade(conn) == 12.0
