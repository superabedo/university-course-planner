
# University Course Planner

A Java command-line application designed to help university students organize courses, track assignments, manage deadlines, and monitor academic progress.

Built using Java, SQLite, JDBC, Maven, and JUnit 5.

## Features

- Add and view university courses
- Create assignments linked to specific courses
- Edit assignment titles and due dates
- Delete assignments with confirmation
- Mark assignments as completed
- Store courses and assignments persistently using SQLite
- Automatically sort assignments by due date
- View an interactive deadline dashboard:
  - Overdue assignments
  - Assignments due within 7 days
  - Upcoming assignments
  - Completed assignments
- Validate user input and prevent duplicate courses
- Run automated unit and database integration tests

## Technologies Used

- Java 17+
- SQLite
- JDBC
- Apache Maven
- JUnit 5
- Git and GitHub

## Project Structure

```text
university-course-planner/
├── src/
│   └── planner/
│       ├── Main.java
│       ├── Course.java
│       ├── Assignment.java
│       └── DatabaseManager.java
├── tests/
│   └── planner/
│       ├── AssignmentTest.java
│       └── DatabaseManagerTest.java
├── pom.xml
├── .gitignore
└── README.md
```

## Getting Started

### Requirements

- JDK 17 or newer
- Apache Maven
- Git

### Installation

Clone the repository:

```bash
git clone https://github.com/superabedo/university-course-planner.git
cd university-course-planner
```

Compile the application:

```bash
mvn clean compile
```

Run the application:

```bash
mvn exec:java
```

The SQLite database is created automatically when the program starts.

## Application Menu

```text
=== UNIVERSITY COURSE PLANNER ===
1. Add Course
2. View Courses
3. Add Assignment
4. View Assignments
5. Mark Assignment Completed
6. Edit Assignment
7. Delete Assignment
8. View Deadline Dashboard
9. Exit
```

## Deadline Dashboard

The dashboard automatically categorizes assignments based on the current date.

- **Overdue:** Incomplete assignments past their due date.
- **Due Soon:** Incomplete assignments due within 7 days.
- **Upcoming:** Incomplete assignments due more than 7 days from now.
- **Completed:** Assignments marked as finished.

## Database Design

The application uses two relational database tables.

### Courses

| Column | Type | Description |
|--------|------|-------------|
| course_code | TEXT | Primary key |
| course_name | TEXT | Course name |

### Assignments

| Column | Type | Description |
|--------|------|-------------|
| id | INTEGER | Primary key |
| title | TEXT | Assignment title |
| course_code | TEXT | Foreign key to courses |
| due_date | TEXT | Due date in YYYY-MM-DD format |
| completed | INTEGER | Completion status (0 or 1) |

The application uses prepared statements for database operations and enables SQLite foreign key constraints.

## Automated Testing

The project contains 12 automated tests:

- 6 unit tests for Java classes
- 6 database integration tests

The database tests use isolated temporary SQLite databases to protect user data.

Run the tests using:

```bash
mvn clean test
```

## Future Improvements

- Edit and delete courses
- Assignment search and filtering
- Improved input validation
- Graphical user interface
- Deadline notifications

## Author

Developed by [superabedo](https://github.com/superabedo).
