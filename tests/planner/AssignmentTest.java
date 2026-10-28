
package planner;

import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

public class AssignmentTest {

    // Test 1: Verify course information
    @Test
    public void testCourseCreation() {

        Course course = new Course(
            "ECE 325",
            "Object-Oriented Software Design"
        );

        assertEquals("ECE 325", course.getCourseCode());

        assertEquals(
            "Object-Oriented Software Design",
            course.getCourseName()
        );
    }

    // Test 2: Verify assignment information
    @Test
    public void testAssignmentCreation() {

        LocalDate dueDate = LocalDate.of(2026, 11, 10);

        Assignment assignment = new Assignment(
            "Java Lab",
            "ECE 325",
            dueDate
        );

        assertEquals("Java Lab", assignment.getTitle());
        assertEquals("ECE 325", assignment.getCourseCode());
        assertEquals(dueDate, assignment.getDueDate());
    }

    // Test 3: New assignments should be pending
    @Test
    public void testNewAssignmentIsPending() {

        Assignment assignment = new Assignment(
            "SQL Assignment",
            "CMPUT 291",
            LocalDate.of(2026, 11, 15)
        );

        assertFalse(assignment.isCompleted());
    }

    // Test 4: Marking an assignment completed
    @Test
    public void testMarkAssignmentCompleted() {

        Assignment assignment = new Assignment(
            "MATLAB Lab",
            "ECE 340",
            LocalDate.of(2026, 11, 20)
        );

        assignment.markCompleted();

        assertTrue(assignment.isCompleted());
    }

    // Test 5: Restore an assignment from saved data
    @Test
    public void testLoadedAssignment() {

        Assignment assignment = new Assignment(
            15,
            "Lab Report",
            "ECE 340",
            LocalDate.of(2026, 12, 1),
            true
        );

        assertEquals(15, assignment.getId());
        assertEquals("Lab Report", assignment.getTitle());
        assertTrue(assignment.isCompleted());
    }

    // Test 6: Completing one assignment should not
    // change another assignment
    @Test
    public void testIndependentAssignmentStatuses() {

        Assignment first = new Assignment(
            "Lab 1",
            "ECE 325",
            LocalDate.of(2026, 11, 1)
        );

        Assignment second = new Assignment(
            "Lab 2",
            "ECE 325",
            LocalDate.of(2026, 11, 8)
        );

        first.markCompleted();

        assertTrue(first.isCompleted());
        assertFalse(second.isCompleted());
    }
}
