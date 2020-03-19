package com.lits.jdbs;

import java.sql.*;

public class Main {
    private static Connection connection = null;

    public static void main(String[] args) {
//        Statement statement = null;

        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/students_data", "root", "Mypass@123");

//            statement = connection.createStatement();
//            ResultSet resultSet = statement.executeQuery("SELECT * FROM students");

//            while (resultSet.next()) {
//                System.out.println(resultSet.getInt("student_id") + " | " +
//                resultSet.getString("first_name") + " | " +
//                resultSet.getString("last_name"));
//            }

            createStudent(6, "Vasyl", "Lomachenko");

        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
//            if (statement != null) {
//                try {
//                    statement.close();
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//            }
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static void createStudent(Integer id, String firstName, String lastName) throws SQLException {

        if (connection != null) {
            PreparedStatement statement = connection.prepareStatement("INSERT INTO students (student_id, first_name, last_name) VALUES (?, ?, ?)");
            statement.setInt(1, id);
            statement.setString(2, firstName);
            statement.setString(3, lastName);

            int result = statement.executeUpdate();

            if (result > 0) {
                System.out.println("Success");
            } else {
                System.out.println("Error");
            }
        }
    }
}
