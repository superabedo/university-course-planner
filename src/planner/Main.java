package planner;

import java.util.ArrayList;
import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Main {

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        ArrayList<Course> courses = new ArrayList<>();
        ArrayList<Assignment> assignments = new ArrayList<>();

        boolean running = true;

        while (running) {
            System.out.println("\n=== UNIVERSITY COURSE PLANNER ===");
            System.out.println("1. Add Course");
            System.out.println("2. View Courses");
            System.out.println("3. Add Assignment");
            System.out.println("4. View Assignments");
            System.out.println("5. Mark Assignment Completed");
            System.out.println("6. Exit");
            System.out.print("Choose an option: ");

            String choice = scanner.nextLine();

            switch (choice) {

                case "1":
                    System.out.print("Enter course code: ");
                    String code = scanner.nextLine().trim();

                    System.out.print("Enter course name: ");
                    String name = scanner.nextLine().trim();

                    if (code.isEmpty() || name.isEmpty()) {
                        System.out.println("Course code and name cannot be empty.");
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
                        System.out.println("That course already exists.");
                    } else {
                        courses.add(new Course(code, name));
                        System.out.println("Course added successfully!");
                    }
                    break;

                case "2":
                    if (courses.isEmpty()) {
                        System.out.println("No courses added yet.");
                    } else {
                        System.out.println("\nYour Courses:");
                        for (Course c : courses) {
                            c.displayCourse();
                        }
                    }
                    break;

                case "3":
                    if (courses.isEmpty()) {
                        System.out.println("Add a course first.");
                        break;
                    }

                    System.out.print("Enter course code: ");
                    String assignmentCode = scanner.nextLine().trim();

                    boolean courseFound = false;
                    for (Course c : courses) {
                        if (c.getCourseCode().equalsIgnoreCase(assignmentCode)) {
                            assignmentCode = c.getCourseCode();
                            courseFound = true;
                            break;
                        }
                    }

                    if (!courseFound) {
                        System.out.println("Course not found.");
                        break;
                    }

                    System.out.print("Enter assignment title: ");
                    String title = scanner.nextLine().trim();

                    if (title.isEmpty()) {
                        System.out.println("Assignment title cannot be empty.");
                        break;
                    }

                    System.out.print("Enter due date (YYYY-MM-DD): ");
                    String dateInput = scanner.nextLine().trim();

                    try {
                        LocalDate dueDate = LocalDate.parse(dateInput);

                        assignments.add(new Assignment(
                            title, assignmentCode, dueDate
                        ));

                        System.out.println("Assignment added successfully!");
                    } catch (DateTimeParseException e) {
                        System.out.println("Invalid date. Use YYYY-MM-DD.");
                    }
                    break;

                case "4":
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments added yet.");
                    } else {
                        System.out.println("\nYour Assignments:");

                        for (int i = 0; i < assignments.size(); i++) {
                            System.out.print((i + 1) + ". ");
                            assignments.get(i).displayAssignment();
                        }
                    }
                    break;

                case "5":
                    if (assignments.isEmpty()) {
                        System.out.println("No assignments to complete.");
                        break;
                    }

                    for (int i = 0; i < assignments.size(); i++) {
                        System.out.print((i + 1) + ". ");
                        assignments.get(i).displayAssignment();
                    }

                    System.out.print("Enter assignment number: ");

                    try {
                        int number = Integer.parseInt(
                            scanner.nextLine().trim()
                        );

                        if (number < 1 || number > assignments.size()) {
                            System.out.println("Invalid assignment number.");
                        } else {
                            assignments.get(number - 1).markCompleted();
                            System.out.println("Assignment marked completed!");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Please enter a valid number.");
                    }
                    break;

                case "6":
                    running = false;
                    System.out.println("Goodbye!");
                    break;

                default:
                    System.out.println("Invalid option. Try again.");
            }
        }

        scanner.close();
    }
}
