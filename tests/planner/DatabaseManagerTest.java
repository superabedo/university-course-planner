
package planner;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DatabaseManagerTest {

    @TempDir
    Path temporaryFolder;

    private String previousDatabaseUrl;

    @BeforeEach
    public void setUp() {

        // Remember any existing database configuration
        previousDatabaseUrl =
            System.getProperty("planner.database.url");

        // Each test gets its own temporary SQLite database
        Path databaseFile =
            temporaryFolder.resolve("test-courseplanner.db");

        System.setProperty(
            "planner.database.url",
            "jdbc:sqlite:" + databaseFile.toAbsolutePath()
        );

        DatabaseManager.initializeDatabase();
    }

    @AfterEach
    public void tearDown() {

        // Restore the original database configuration
        if (previousDatabaseUrl == null) {
            System.clearProperty("planner.database.url");
        } else {
            System.setProperty(
                "planner.database.url",
                previousDatabaseUrl
            );
        }
    }

    // Test 1: Courses can be saved and loaded
    @Test
    public void testSaveAndLoadCourse() {

        Course course = new Course(
            "ECE 325",
            "Object-Oriented Programming"
        );

        assertTrue(DatabaseManager.addCourse(course));

        ArrayList<Course> courses =
            DatabaseManager.loadCourses();

        assertEquals(1, courses.size());
        assertEquals(
            "ECE 325",
            courses.get(0).getCourseCode()
        );
        assertEquals(
            "Object-Oriented Programming",
            courses.get(0).getCourseName()
        );

        // The primary key must reject a duplicate
        assertFalse(DatabaseManager.addCourse(course));
        assertEquals(1, DatabaseManager.loadCourses().size());
    }

    // Test 2: Assignments can be saved and loaded
    @Test
    public void testSaveAndLoadAssignment() {

        createTestCourse();

        Assignment assignment = new Assignment(
            "Java Lab",
            "ECE 325",
            LocalDate.of(2026, 11, 10)
        );

        assertTrue(
            DatabaseManager.addAssignment(assignment)
        );

        ArrayList<Assignment> assignments =
            DatabaseManager.loadAssignments();

        assertEquals(1, assignments.size());

        Assignment saved = assignments.get(0);

        assertTrue(saved.getId() > 0);
        assertEquals("Java Lab", saved.getTitle());
        assertEquals("ECE 325", saved.getCourseCode());
        assertEquals(
            LocalDate.of(2026, 11, 10),
            saved.getDueDate()
        );
        assertFalse(saved.isCompleted());
    }

    // Test 3: Completion status is saved to SQLite
    @Test
    public void testMarkAssignmentCompleted() {

        int id = createTestAssignment();

        assertTrue(
            DatabaseManager.markAssignmentCompleted(id)
        );

        // Reload from SQLite to verify persistence
        ArrayList<Assignment> assignments =
            DatabaseManager.loadAssignments();

        assertEquals(1, assignments.size());
        assertTrue(assignments.get(0).isCompleted());
    }

    // Test 4: Editing updates the database record
    @Test
    public void testUpdateAssignment() {

        int id = createTestAssignment();

        LocalDate newDate =
            LocalDate.of(2026, 12, 15);

        assertTrue(
            DatabaseManager.updateAssignment(
                id,
                "Updated Java Lab",
                newDate
            )
        );

        Assignment updated =
            DatabaseManager.loadAssignments().get(0);

        assertEquals(id, updated.getId());
        assertEquals(
            "Updated Java Lab",
            updated.getTitle()
        );
        assertEquals(newDate, updated.getDueDate());
        assertFalse(updated.isCompleted());
    }

    // Test 5: Deleting removes the database record
    @Test
    public void testDeleteAssignment() {

        int id = createTestAssignment();

        assertEquals(
            1,
            DatabaseManager.loadAssignments().size()
        );

        assertTrue(
            DatabaseManager.deleteAssignment(id)
        );

        assertTrue(
            DatabaseManager.loadAssignments().isEmpty()
        );

        // Cannot delete the same record twice
        assertFalse(
            DatabaseManager.deleteAssignment(id)
        );
    }

    // Test 6: An assignment must reference a valid course
    @Test
    public void testForeignKeyConstraint() {

        Assignment invalidAssignment = new Assignment(
            "Unknown Course Lab",
            "FAKE 999",
            LocalDate.of(2026, 11, 20)
        );

        assertFalse(
            DatabaseManager.addAssignment(invalidAssignment)
        );

        assertTrue(
            DatabaseManager.loadAssignments().isEmpty()
        );
    }

    // Helper: Add a course for assignment tests
    private void createTestCourse() {

        Course course = new Course(
            "ECE 325",
            "Object-Oriented Programming"
        );

        assertTrue(
            DatabaseManager.addCourse(course)
        );
    }

    // Helper: Add an assignment and return its SQLite ID
    private int createTestAssignment() {

        createTestCourse();

        Assignment assignment = new Assignment(
            "Java Lab",
            "ECE 325",
            LocalDate.of(2026, 11, 10)
        );

        assertTrue(
            DatabaseManager.addAssignment(assignment)
        );

        ArrayList<Assignment> assignments =
            DatabaseManager.loadAssignments();

        assertEquals(1, assignments.size());

        return assignments.get(0).getId();
    }
}
