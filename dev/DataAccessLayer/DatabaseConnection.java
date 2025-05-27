package DataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Manages the DB connection (Singleton)
public class DatabaseConnection {
    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlite:C:/Users/shiri/ADSS_Group_AI/dev/supplyinventory.db";

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                connection.setNetworkTimeout(null, 30000); // 30 seconds
            } catch (SQLException e) {
                System.err.println("Failed to create database connection: " + e.getMessage());
                throw e;
            }
        }
        return connection;
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
                connection = null;
            } catch (SQLException e) {
                System.err.println("Error closing database connection: " + e.getMessage());
            }
        }
    }

    public static boolean isConnectionActive() {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException e) {
            return false;
        }
    }

    public static void resetConnection() {
        closeConnection();
        try {
            getConnection();
        } catch (SQLException e) {
            System.err.println("Failed to reset database connection: " + e.getMessage());
        }
    }
}