
<?php

use PHPUnit\Framework\TestCase;

require_once __DIR__ . '/Students.php';

class StudentsTest extends TestCase
{
    private static ?PDO $conn = null;
    private Students $students;

    public static function setUpBeforeClass(): void
    {
        self::$conn = new PDO(
            "pgsql:host=localhost;port=5432;dbname=tp",
            "postgres",
            "postgres"
        );

        self::$conn->setAttribute(
            PDO::ATTR_ERRMODE,
            PDO::ERRMODE_EXCEPTION
        );
    }

    protected function setUp(): void
    {
        self::$conn->exec(
            "TRUNCATE TABLE students RESTART IDENTITY CASCADE"
        );

        $this->students = new Students(self::$conn);
    }

    public static function tearDownAfterClass(): void
    {
        self::$conn = null;
    }

    public function testAddAndGetStudent(): void
    {
        $id = $this->students->addStudent("Sara", 16);

        $student = $this->students->getStudent($id);

        $this->assertEquals("Sara", $student['name']);
        $this->assertEquals(16.0, (float) $student['grade']);
    }

    public function testGetNonexistentStudent(): void
    {
        $student = $this->students->getStudent(9999);

        $this->assertNull($student);
    }

    public function testAverageGrade(): void
    {
        $this->students->addStudent("Amel", 10);
        $this->students->addStudent("Karim", 14);

        $this->assertEquals(12.0, $this->students->averageGrade());
    }
}
