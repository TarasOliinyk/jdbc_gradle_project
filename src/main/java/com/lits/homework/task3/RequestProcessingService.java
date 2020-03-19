package com.lits.homework.task3;

import com.lits.utils.DBConnectionUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Scanner;

public class RequestProcessingService {
    private static Connection connection = null;

    public static void main(String[] args) {
        processClientRequest();
    }

    /**
     * Processes requests (Create, Update, Delete) to the 'students_data' database basing on passed action intended to
     * be performed.
     */
    private static void processClientRequest() {

        try {
            connection = DBConnectionUtil.establishDBConnection();

            Scanner input = new Scanner(System.in);
            System.out.println("=====================================");
            System.out.println("> ACTION: ");
            String action = input.next().toLowerCase();

            switch (action) {
                case "add":
                    System.out.println("> Please enter first name of the student intended to be added:");
                    String firstName = input.next();
                    System.out.println("> Please enter last name of the student intended to be added:");
                    String lastName = input.next();
                    createStudent(firstName, lastName);
                    break;
                case "edit":
                    System.out.println("Please enter id of the student intended to be updated:");
                    int studentId = Integer.parseInt(input.next());
                    System.out.println("Please enter new first name for the student:");
                    String newFirstName = input.next();
                    System.out.println("Please enter new last name for the student:");
                    String newLastName = input.next();
                    editStudent(studentId, newFirstName, newLastName);
                    break;
                case "delete":
                    System.out.println("Please enter id of the student intended to be deleted:");
                    int idOfStudentToDelete = Integer.parseInt(input.next());
                    deleteStudent(idOfStudentToDelete);
                    break;
                case "marks":
                    System.out.println("Please enter id of the student mark of which is intended to be updated:");
                    int id = Integer.parseInt(input.next());
                    System.out.println("PLease enter name of the subject the mark of which is intended to be updated:");
                    String subject = input.next();
                    System.out.println("Please enter new mark for the subject:");
                    int newMark = Integer.parseInt(input.next());
                    updateSubjectMarkForStudent(id, subject, newMark);
                    break;
                default:
                    throw new RuntimeException("An unsupported action has been entered. " +
                            "The supported actions are: 'Add', 'Edit', 'Delete', 'Marks'.");
            }
        } finally {
            System.out.println("=====================================");
            DBConnectionUtil.closeConnection(connection);
        }
    }

//======================================================================================================================

    /**
     * Creates a new student record in the 'students' table of the 'students_data' database.
     *
     * @param firstName - Specifies first name of the student intended to be created
     * @param lastName  - Specifies last name of the student intended to be created
     */
    private static void createStudent(String firstName, String lastName) {
        String sqlQuery = "INSERT INTO students (first_name, last_name) VALUES (?, ?)";
        PreparedStatement statement = prepareStatement(sqlQuery);

        try {
            statement.setString(1, firstName);
            statement.setString(2, lastName);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeStatement(statement);
    }

    /**
     * Edits specific student record in the 'students' table of the 'students_data' database.
     *
     * @param studentId    - Specifies id of the student intended to be edited
     * @param newFirstName - Specifies new fist name intended to be set for the student
     * @param newLastName  - Specifies new last name intended to be set for the student
     */
    private static void editStudent(int studentId, String newFirstName, String newLastName) {
        String sqlQuery = "UPDATE students SET first_name = ?, last_name = ? WHERE student_id = ?";
        PreparedStatement statement = prepareStatement(sqlQuery);

        try {
            statement.setString(1, newFirstName);
            statement.setString(2, newLastName);
            statement.setInt(3, studentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeStatement(statement);
    }

    /**
     * Deletes specific student record from the 'students' table of the 'students_data' database.
     *
     * @param studentId - Specifies id of the student intended to be deleted
     */
    private static void deleteStudent(int studentId) {
        String sqlQuery = "DELETE FROM students WHERE student_id = ?";
        PreparedStatement statement = prepareStatement(sqlQuery);

        try {
            statement.setInt(1, studentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeStatement(statement);
    }

    /**
     * Updates mark of specific student record in the 'students' table of the 'students_data' database.
     *
     * @param studentId - Specifies id of the student mark of which is intended to be updated
     * @param subject   - Specifies subject the mark of which is intended to be updated
     * @param newMark   - Specifies the new mark intended to be set
     */
    private static void updateSubjectMarkForStudent(int studentId, String subject, int newMark) {
        String sqlQuery = "UPDATE marks m INNER JOIN subjects sb ON m.subject_id = sb.subject_id SET m.mark = ? " +
                "WHERE sb.subject_title = ? AND m.student_id = ?";
        PreparedStatement statement = prepareStatement(sqlQuery);

        try {
            statement.setInt(1, newMark);
            statement.setString(2, subject);
            statement.setInt(3, studentId);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        executeStatement(statement);
    }

//======================================================================================================================

    /**
     * Creates statement of the PreparedStatement type for specific SQL query.
     *
     * @param sqlQuery - Specifies the SQL query the statement is intended to be created for
     * @return {@code PreparedStatement}
     */
    private static PreparedStatement prepareStatement(String sqlQuery) {

        if (connection != null) {
            PreparedStatement statement = null;

            try {
                statement = connection.prepareStatement(sqlQuery);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return statement;
        } else {
            throw new RuntimeException("Connection has not been established");
        }
    }

    /**
     * Executes specified statement (PreparedStatement).
     *
     * @param statement - Specifies the statement intended to be executed
     */
    private static void executeStatement(PreparedStatement statement) {

        if (statement != null) {
            int result = 0;

            try {
                result = statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            String statusMessage = result > 0 ? "Success" : "Error";
            System.out.println(statusMessage);
        } else {
            throw new RuntimeException("Passed statement cannot be processed, its value is null");
        }
    }
}
