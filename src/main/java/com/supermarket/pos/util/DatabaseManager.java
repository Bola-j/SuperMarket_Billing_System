package com.supermarket.pos.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * DatabaseManager handles SQLite database connections and operations.
 * Provides methods for database initialization and connection management.
 */
public class DatabaseManager {

    private static final String DATABASE_URL = "jdbc:sqlite:supermarket_pos.db";
    private static Connection connection;

    /**
     * Private constructor to prevent instantiation
     */
    private DatabaseManager() {
    }

    /**
     * Establish a database connection
     */
    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(DATABASE_URL);
        }
        return connection;
    }

    /**
     * Close the database connection
     */
    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Initialize the database and create tables if they don't exist
     */
    public static void initializeDatabase() {
        // TODO: Create database schema and tables
    }
}
