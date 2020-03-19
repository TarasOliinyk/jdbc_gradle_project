package com.lits.utils;

import java.sql.*;

public class DBConnectionUtil {

    /**
     * Loads specific DB driver.
     *
     * @param dbDriver - Specifies the DB driver intended to be loaded
     */
    private static void loadDatabaseDriver(String dbDriver) {

        try {
            Class.forName(dbDriver);
        } catch (ClassNotFoundException e) {
            System.out.println(String.format("\nDatabase driver '%s' has not been found/loaded.\n", dbDriver));
            e.printStackTrace();
        }
    }

    /**
     * Establishes Connection to the 'students_data' Database.
     *
     * @return {@code Connection}
     */
    public static Connection establishDBConnection() {
        Connection connection = null;
        String connectionURL = "jdbc:mysql://localhost:3306/students_data";
        String username = "root";
        String password = "Mypass@123";
        loadDatabaseDriver("com.mysql.jdbc.Driver");

        try {
            connection = DriverManager.getConnection(connectionURL, username, password);
        } catch (SQLException e) {
            System.out.println(String.format("\nConnection to '%s' database has not been established.\n", connectionURL));
            e.printStackTrace();
        }
        return connection;
    }

    /**
     * Closes specific Database Connection.
     *
     * @param connection - Specifies instance of the DB Connection intended to be closed
     */
    public static void closeConnection(Connection connection) {

        if (connection != null) {

            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
