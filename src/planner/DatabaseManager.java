
package planner;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

public class DatabaseManager {

    private static final String DATABASE_URL =
        "jdbc:sqlite:courseplanner.db";

    public static Connection getConnection() throws SQLException {
        Connection connection =
            DriverManager.getConnection(DATABASE_URL);

        try (Statement statement = connection.createStatement()) {
            statement.execute("PRAGMA foreign_keys = ON");
        }

        return connection;
    }

    public static void initializeDatabase() {
        String coursesSQL = """
            CREATE TABLE IF NOT EXISTS courses (
                course_code TEXT PRIMARY KEY,
                course_name TEXT NOT NULL
            )
            """;

        String assignmentsSQL = """
            CREATE TABLE IF NOT EXISTS assignments (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                title TEXT NOT NULL,
                course_code TEXT NOT NULL,
                due_date TEXT NOT NULL,
                completed INTEGER NOT NULL DEFAULT 0,
                FOREIGN KEY (course_code)
                    REFERENCES courses(course_code)
            )
            """;

        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            statement.execute(coursesSQL);
            statement.execute(assignmentsSQL);
            System.out.println("Database initialized successfully!");

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Could not initialize database", e
            );
        }
    }

    public static boolean addCourse(Course course) {
        String sql = """
            INSERT INTO courses (course_code, course_name)
            VALUES (?, ?)
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setString(1, course.getCourseCode());
            statement.setString(2, course.getCourseName());

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                "Could not save course: " + e.getMessage()
            );
            return false;
        }
    }

    public static ArrayList<Course> loadCourses() {
        ArrayList<Course> courses = new ArrayList<>();

        String sql = """
            SELECT course_code, course_name
            FROM courses
            ORDER BY course_code
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                courses.add(new Course(
                    results.getString("course_code"),
                    results.getString("course_name")
                ));
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Could not load courses", e
            );
        }

        return courses;
    }

    public static boolean addAssignment(Assignment assignment) {
        String sql = """
            INSERT INTO assignments
            (title, course_code, due_date, completed)
            VALUES (?, ?, ?, ?)
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setString(1, assignment.getTitle());
            statement.setString(2, assignment.getCourseCode());
            statement.setString(
                3, assignment.getDueDate().toString()
            );
            statement.setInt(
                4, assignment.isCompleted() ? 1 : 0
            );

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                "Could not save assignment: " + e.getMessage()
            );
            return false;
        }
    }

    public static ArrayList<Assignment> loadAssignments() {
        ArrayList<Assignment> assignments = new ArrayList<>();

        String sql = """
            SELECT id, title, course_code, due_date, completed
            FROM assignments
            ORDER BY due_date, id
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql);
             ResultSet results = statement.executeQuery()) {

            while (results.next()) {
                assignments.add(new Assignment(
                    results.getInt("id"),
                    results.getString("title"),
                    results.getString("course_code"),
                    LocalDate.parse(results.getString("due_date")),
                    results.getInt("completed") == 1
                ));
            }

        } catch (SQLException e) {
            throw new IllegalStateException(
                "Could not load assignments", e
            );
        }

        return assignments;
    }

    public static boolean markAssignmentCompleted(int assignmentId) {
        String sql = """
            UPDATE assignments
            SET completed = 1
            WHERE id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setInt(1, assignmentId);
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                "Could not update assignment: " + e.getMessage()
            );
            return false;
        }
    }

    // Update an assignment's title and due date
    public static boolean updateAssignment(
            int assignmentId, String newTitle, LocalDate newDueDate) {

        String sql = """
            UPDATE assignments
            SET title = ?, due_date = ?
            WHERE id = ?
            """;

        try (Connection connection = getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setString(1, newTitle);
            statement.setString(2, newDueDate.toString());
            statement.setInt(3, assignmentId);

            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                "Could not edit assignment: " + e.getMessage()
            );
            return false;
        }
    }

    // Delete an assignment by its database ID
    public static boolean deleteAssignment(int assignmentId) {
        String sql = "DELETE FROM assignments WHERE id = ?";

        try (Connection connection = getConnection();
             PreparedStatement statement =
                 connection.prepareStatement(sql)) {

            statement.setInt(1, assignmentId);
            return statement.executeUpdate() == 1;

        } catch (SQLException e) {
            System.out.println(
                "Could not delete assignment: " + e.getMessage()
            );
            return false;
        }
    }
}
