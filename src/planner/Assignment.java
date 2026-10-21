package planner;

import java.time.LocalDate;

public class Assignment {

    private String title;
    private String courseCode;
    private LocalDate dueDate;
    private boolean completed;

    public Assignment(String title, String courseCode,
                      LocalDate dueDate) {
        this.title = title;
        this.courseCode = courseCode;
        this.dueDate = dueDate;
        this.completed = false;
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
