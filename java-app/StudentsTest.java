
import org.junit.jupiter.api.*;
import java.sql.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class StudentsTest {

    private static Connection conn;
    private Students students;

    @BeforeAll
    static void connect() throws SQLException {
        conn = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/tp",
            "postgres",
            "postgres"
        );
    }

    @BeforeEach
    void setUp() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute("TRUNCATE TABLE students RESTART IDENTITY CASCADE");
        }

        students = new Students(conn);
    }

    @AfterAll
    static void close() throws SQLException {
        if (conn != null) {
            conn.close();
        }
    }

    @Test
    void testAddAndGetStudent() throws SQLException {
        int id = students.addStudent("Sara", 16);

        Optional<Students.Student> student = students.getStudent(id);

        assertTrue(student.isPresent());
        assertEquals("Sara", student.get().name);
        assertEquals(16.0, student.get().grade);
    }

    @Test
    void testGetNonexistentStudent() throws SQLException {
        Optional<Students.Student> student = students.getStudent(9999);

        assertTrue(student.isEmpty());
    }

    @Test
    void testAverageGrade() throws SQLException {
        students.addStudent("Amel", 10);
        students.addStudent("Karim", 14);

        assertEquals(12.0, students.averageGrade(), 0.001);
    }
}
