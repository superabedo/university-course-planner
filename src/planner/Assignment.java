
package planner;

import java.time.LocalDate;

public class Assignment {

    private int id;
    private String title;
    private String courseCode;
    private LocalDate dueDate;
    private boolean completed;

    // Constructor for a new assignment
    public Assignment(String title, String courseCode,
                      LocalDate dueDate) {
        this(0, title, courseCode, dueDate, false);
    }

    // Constructor for an assignment loaded from SQLite
    public Assignment(int id, String title, String courseCode,
                      LocalDate dueDate, boolean completed) {
        this.id = id;
        this.title = title;
        this.courseCode = courseCode;
        this.dueDate = dueDate;
        this.completed = completed;
    }

    public int getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public boolean isCompleted() {
        return completed;
    }

    public void markCompleted() {
        completed = true;
    }

    public void displayAssignment() {
        String status = completed ? "Completed" : "Pending";

        System.out.println(
            title + " | " +
            courseCode + " | Due: " +
            dueDate + " | " + status
        );
    }
}
