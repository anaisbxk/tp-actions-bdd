
const { Pool } = require("pg");

async function addStudent(pool, name, grade) {
    const result = await pool.query(
        "INSERT INTO students (name, grade) VALUES ($1, $2) RETURNING id",
        [name, grade]
    );

    return result.rows[0].id;
}

async function getStudent(pool, studentId) {
    const result = await pool.query(
        "SELECT id, name, grade FROM students WHERE id = $1",
        [studentId]
    );

    return result.rows[0] || null;
}

async function averageGrade(pool) {
    const result = await pool.query(
        "SELECT AVG(grade) AS average FROM students"
    );

    return result.rows[0].average === null
        ? 0
        : Number(result.rows[0].average);
}

module.exports = {
    addStudent,
    getStudent,
    averageGrade
};
