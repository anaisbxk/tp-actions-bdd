
<?php

class Students
{
    private PDO $conn;

    public function __construct(PDO $conn)
    {
        $this->conn = $conn;
    }

    public function addStudent(string $name, float $grade): int
    {
        $sql = "INSERT INTO students (name, grade)
                VALUES (:name, :grade) RETURNING id";

        $stmt = $this->conn->prepare($sql);

        $stmt->execute([
            ':name' => $name,
            ':grade' => $grade
        ]);

        return (int) $stmt->fetchColumn();
    }

    public function getStudent(int $studentId): ?array
    {
        $sql = "SELECT id, name, grade
                FROM students WHERE id = :id";

        $stmt = $this->conn->prepare($sql);
        $stmt->execute([':id' => $studentId]);

        $student = $stmt->fetch(PDO::FETCH_ASSOC);

        return $student ?: null;
    }

    public function averageGrade(): float
    {
        $stmt = $this->conn->query(
            "SELECT AVG(grade) FROM students"
        );

        $result = $stmt->fetchColumn();

        return $result !== null ? (float) $result : 0.0;
    }
}
