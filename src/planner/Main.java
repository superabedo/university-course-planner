
package planner;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        DatabaseManager.initializeDatabase();

        Scanner scanner = new Scanner(System.in);
        ArrayList<Course> courses = DatabaseManager.loadCourses();
        ArrayList<Assignment> assignments =
            DatabaseManager.loadAssignments();

        boolean running = true;

        while (running) {

            System.out.println("\n=== UNIVERSITY COURSE PLANNER ===");
            System.out.println("1. Add Course");
            System.out.println("2. View Courses");
            System.out.println("3. Add Assignment");
            System.out.println("4. View Assignments");
            System.out.println("5. Mark Assignment Completed");
            System.out.println("6. Edit Assignment");
            System.out.println("7. Delete Assignment");
            System.out.println("8. View Deadline Dashboard");
            System.out.println("9. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine().trim();

            switch (choice) {

                case "1": {
                    System.out.print("Enter course code: ");
                    String code = scanner.nextLine().trim();

                    System.out.print("Enter course name: ");
                    String name = scanner.nextLine().trim();

                    if (code.isEmpty() || name.isEmpty()) {
                        System.out.println(
                            "Course code and name cannot be empty."
                        );
                        break;
                    }

                    boolean exists = false;

                    for (Course c : courses) {
                        if (c.getCourseCode().equalsIgnoreCase(code)) {
                            exists = true;
                            break;
                        }
                    }

                    if (exists) {
                        System.out.println("Course already exists.");
                        break;
                    }

                    if (DatabaseManager.addCourse(new Course(code, name))) {
                        courses = DatabaseManager.loadCourses();
                        System.out.println("Course saved successfully!");
                    } else {
                        System.out.println("Course was not saved.");
                    }
                    break;
                }

                case "2": {
                    if (courses.isEmpty()) {
                        System.out.println("No courses added yet.");
                    } else {
                        System.out.println("\nYour Courses:");
                        for (Course c : courses) {
                            c.displayCourse();
                        }
                    }
                    break;
                }

                case "3": {
                    if (courses.isEmpty()) {
                        System.out.println("Add a course first.");
                        break;
                    }

                    System.out.print("Enter course code: ");
                    String code = scanner.nextLine().trim();

                    String matchedCode = null;

                    for (Course c : courses) {
                        if (c.getCourseCode().equalsIgnoreCase(code)) {
                            matchedCode = c.getCourseCode();
                            break;
                        }
                    }

                    if (matchedCode == null) {
                        System.out.println("Course not found.");
                        break;
                    }

                    System.out.print("Enter assignment title: ");
                    String title = scanner.nextLine().trim();

                    if (title.isEmpty()) {
                        System.out.println("Title cannot be empty.");
                        break;
                    }

                    System.out.print("Enter due date (YYYY-MM-DD): ");

                    try {
                        LocalDate date = LocalDate.parse(
                            scanner.nextLine().trim()
                        );

                        Assignment assignment =
                            new Assignment(title, matchedCode, date);

                        if (DatabaseManager.addAssignment(assignment)) {
                            assignments = DatabaseManager.loadAssignments();
                            System.out.println(
                                "Assignment saved successfully!"
                            );
                        } else {
                            System.out.println("Assignment was not saved.");
                        }

                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date. Use YYYY-MM-DD.");
                    }
                    break;
                }

                case "4": {
                    displayAssignments(assignments);
                    break;
                }

                case "5": {
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments to complete.");
                        break;
                    }

                    displayAssignments(assignments);

                    int index = selectAssignment(
                        scanner, assignments.size()
                    );

                    if (index == -1) {
                        break;
                    }

                    Assignment selected = assignments.get(index);

                    if (selected.isCompleted()) {
                        System.out.println("Already completed.");
                        break;
                    }

                    if (DatabaseManager.markAssignmentCompleted(
                            selected.getId())) {

                        assignments = DatabaseManager.loadAssignments();

                        System.out.println(
                            "Assignment marked completed and saved!"
                        );
                    } else {
                        System.out.println("Could not update assignment.");
                    }
                    break;
                }

                case "6": {
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments to edit.");
                        break;
                    }

                    displayAssignments(assignments);

                    int index = selectAssignment(
                        scanner, assignments.size()
                    );

                    if (index == -1) {
                        break;
                    }

                    Assignment selected = assignments.get(index);

                    System.out.print("New assignment title: ");
                    String newTitle = scanner.nextLine().trim();

                    if (newTitle.isEmpty()) {
                        System.out.println("Title cannot be empty.");
                        break;
                    }

                    System.out.print("New due date (YYYY-MM-DD): ");

                    try {
                        LocalDate newDate = LocalDate.parse(
                            scanner.nextLine().trim()
                        );

                        if (DatabaseManager.updateAssignment(
                                selected.getId(), newTitle, newDate)) {

                            assignments = DatabaseManager.loadAssignments();

                            System.out.println(
                                "Assignment updated successfully!"
                            );
                        } else {
                            System.out.println("Could not edit assignment.");
                        }

                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date. Use YYYY-MM-DD.");
                    }

                    break;
                }

                case "7": {
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments to delete.");
                        break;
                    }

                    displayAssignments(assignments);

                    int index = selectAssignment(
                        scanner, assignments.size()
                    );

                    if (index == -1) {
                        break;
                    }

                    Assignment selected = assignments.get(index);

                    System.out.print(
                        "Delete '" + selected.getTitle()
                        + "'? (yes/no): "
                    );

                    String confirmation = scanner.nextLine().trim();

                    if (!confirmation.equalsIgnoreCase("yes")) {
                        System.out.println("Deletion cancelled.");
                        break;
                    }

                    if (DatabaseManager.deleteAssignment(selected.getId())) {
                        assignments = DatabaseManager.loadAssignments();
                        System.out.println(
                            "Assignment deleted successfully!"
                        );
                    } else {
                        System.out.println("Could not delete assignment.");
                    }
                    break;
                }

                case "8": {
                    displayDeadlineDashboard(assignments);
                    break;
                }

                case "9": {
                    running = false;
                    System.out.println("Goodbye!");
                    break;
                }

                default: {
                    System.out.println("Invalid option. Try again.");
                }
            }
        }

        scanner.close();
    }

    private static void displayAssignments(
            ArrayList<Assignment> assignments) {

        if (assignments.isEmpty()) {
            System.out.println("No assignments added yet.");
            return;
        }

        System.out.println("\nYour Assignments:");

        for (int i = 0; i < assignments.size(); i++) {
            System.out.print((i + 1) + ". ");
            assignments.get(i).displayAssignment();
        }
    }

    private static int selectAssignment(
            Scanner scanner, int totalAssignments) {

        System.out.print("Enter assignment number: ");

        try {
            int number = Integer.parseInt(
                scanner.nextLine().trim()
            );

            if (number < 1 || number > totalAssignments) {
                System.out.println("Invalid assignment number.");
                return -1;
            }

            return number - 1;

        } catch (NumberFormatException e) {
            System.out.println("Please enter a valid number.");
            return -1;
        }
    }

    // Display assignments grouped by deadline status
    private static void displayDeadlineDashboard(
            ArrayList<Assignment> assignments) {

        LocalDate today = LocalDate.now();

        ArrayList<Assignment> overdue = new ArrayList<>();
        ArrayList<Assignment> dueSoon = new ArrayList<>();
        ArrayList<Assignment> upcoming = new ArrayList<>();
        ArrayList<Assignment> completed = new ArrayList<>();

        for (Assignment assignment : assignments) {

            if (assignment.isCompleted()) {
                completed.add(assignment);
                continue;
            }

            long daysUntilDue = ChronoUnit.DAYS.between(
                today, assignment.getDueDate()
            );

            if (daysUntilDue < 0) {
                overdue.add(assignment);
            } else if (daysUntilDue <= 7) {
                dueSoon.add(assignment);
            } else {
                upcoming.add(assignment);
            }
        }

        System.out.println("\n=== DEADLINE DASHBOARD ===");
        System.out.println("Today's date: " + today);

        System.out.println("\n--- OVERDUE ---");
        printDashboardGroup(overdue);

        System.out.println("\n--- DUE SOON (NEXT 7 DAYS) ---");
        printDashboardGroup(dueSoon);

        System.out.println("\n--- UPCOMING (MORE THAN 7 DAYS) ---");
        printDashboardGroup(upcoming);

        System.out.println("\n--- COMPLETED ---");
        printDashboardGroup(completed);

        System.out.println("\nSummary:");
        System.out.println("Overdue: " + overdue.size());
        System.out.println("Due Soon: " + dueSoon.size());
        System.out.println("Upcoming: " + upcoming.size());
        System.out.println("Completed: " + completed.size());
    }

    private static void printDashboardGroup(
            ArrayList<Assignment> group) {

        if (group.isEmpty()) {
            System.out.println("None");
            return;
        }

        group.sort(
            (a, b) -> a.getDueDate().compareTo(b.getDueDate())
        );

        for (Assignment assignment : group) {
            assignment.displayAssignment();
        }
    }
}
