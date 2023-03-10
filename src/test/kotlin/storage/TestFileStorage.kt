package storage

import kotlinx.coroutines.runBlocking
import kotlin.io.path.Path
import kotlin.io.path.deleteExisting
import kotlin.io.path.exists
import kotlin.test.*

data class Student(val nr: Int, val name: String)

// Serializer example
private object StudentSerializer: Serializer<Student, String> {
    override fun write(obj: Student) = "${obj.nr} ${obj.name}"
    override fun parse(stream: String): Student {
        val words = stream.split(" ")
        require(words.size == 2) { "Source stream in illegal format, " +
                "it should contain <nr> <name>" }
        val nr = words.first().toIntOrNull()
        requireNotNull(nr) { "Number received is not a valid number" }
        return Student(nr, words[1])
    }
}

private class TestFileStorage {
    // Constants
    private val studentNr = 934973
    private val folder = "out"
    // Setup function to run always before every test and to delete created files
    // from previous tests
    @BeforeTest fun setup() {
        val f = Path("$folder/$studentNr.txt")
        if (f.exists()) f.deleteExisting()
    }
    @Test fun `Check if we cannot create the same file twice`() {
        runBlocking {
            val fs = FileStorage<Int, Student>(folder, StudentSerializer)
            fs.create(studentNr, Student(studentNr, "ISEL"))
            assertFailsWith<IllegalArgumentException> {
                fs.create(studentNr, Student(studentNr, "TDS"))
            }
        }
    }
    @Test fun `Serialize and deserialize`() {
        runBlocking {
            val fs = FileStorage<Int, Student>(folder, StudentSerializer)
            assertNull(fs.read(studentNr))
            val student = Student(studentNr, "ISEL")
            fs.create(studentNr, student)
            val actual = fs.read(studentNr)
            assertEquals(student, actual)
            assertNotSame(student, actual)
        }
    }
    @Test fun `Update a file`() {
        val fs = FileStorage<Int, Student>(folder, StudentSerializer)
        val student1 = Student(studentNr, "LEIC")
        runBlocking {
            assertFailsWith<IllegalArgumentException> {
                fs.update(studentNr, student1)
            }
            fs.create(studentNr, student1)
            val before = fs.read(studentNr)
            val student2 = Student(studentNr, "TDS")
            fs.update(studentNr, student2)
            val after = fs.read(studentNr)
            assertNotEquals(before, after)
        }
    }
    @Test fun `Delete a file`() {
        val fs = FileStorage<Int, Student>(folder, StudentSerializer)
        val student = Student(studentNr, "CHECKERS")
        runBlocking {
            assertFailsWith<IllegalArgumentException> {
                fs.delete(studentNr)
            }
            fs.create(studentNr, student)
            fs.delete(studentNr)
        }
    }
}