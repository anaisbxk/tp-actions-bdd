
const { Pool } = require("pg");

const {
    addStudent,
    getStudent,
    averageGrade
} = require("./students");

const pool = new Pool({
    host: process.env.DB_HOST || "localhost",
    database: process.env.DB_NAME || "tp",
    user: process.env.DB_USER || "postgres",
    password: process.env.DB_PASSWORD || "postgres",
    port: process.env.DB_PORT || 5432
});

beforeEach(async () => {
    await pool.query(
        "TRUNCATE TABLE students RESTART IDENTITY CASCADE"
    );
});

afterAll(async () => {
    await pool.end();
});

test("Ajouter puis relire un étudiant", async () => {
    const id = await addStudent(pool, "Sara", 16);

    const student = await getStudent(pool, id);

    expect(student.name).toBe("Sara");
    expect(Number(student.grade)).toBe(16);
});

test("Lire un étudiant inexistant", async () => {
    const student = await getStudent(pool, 9999);

    expect(student).toBeNull();
});

test("Calculer la moyenne des notes", async () => {
    await addStudent(pool, "Amel", 10);
    await addStudent(pool, "Karim", 14);

    const average = await averageGrade(pool);

    expect(average).toBe(12);
});
