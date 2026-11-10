
import java.sql.*;
import java.util.Optional;

public class Students {

    private final Connection conn;

    public Students(Connection conn) {
        this.conn = conn;
    }

    public int addStudent(String name, double grade) throws SQLException {
        String sql = "INSERT INTO students (name, grade) VALUES (?, ?) RETURNING id";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, name);
            stmt.setDouble(2, grade);

            try (ResultSet rs = stmt.executeQuery()) {
                rs.next();
                return rs.getInt("id");
            }
        }
    }

    public Optional<Student> getStudent(int studentId) throws SQLException {
        String sql = "SELECT id, name, grade FROM students WHERE id = ?";

        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, studentId);

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Student(
                        rs.getInt("id"),
                        rs.getString("name"),
                        rs.getDouble("grade")
                    ));
                }
                return Optional.empty();
            }
        }
    }

    public double averageGrade() throws SQLException {
        String sql = "SELECT AVG(grade) FROM students";

        try (Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            rs.next();
            return rs.getDouble(1);
        }
    }

    public static class Student {
        public final int id;
        public final String name;
        public final double grade;

        public Student(int id, String name, double grade) {
            this.id = id;
            this.name = name;
            this.grade = grade;
        }
    }
}
