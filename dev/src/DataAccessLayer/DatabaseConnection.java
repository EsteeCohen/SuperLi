/*package DataAccessLayer;

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
}*/
package DataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Manages the DB connection (Singleton)
public class DatabaseConnection {
    private static Connection connection;
    private static final String DB_URL = "jdbc:sqlite:../supplyinventory.db";
    private static boolean isTestMode = false;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            try {
                connection = DriverManager.getConnection(DB_URL);
                connection.setNetworkTimeout(null, 30000); // 30 seconds
                // Enable foreign keys for SQLite
                connection.createStatement().execute("PRAGMA foreign_keys = ON");
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
                if (!connection.isClosed()) {
                    connection.close();
                }
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

    // Method specifically for tests
    public static void setTestMode(boolean testMode) {
        isTestMode = testMode;
        if (testMode) {
            // In test mode, ensure we have a fresh connection
            resetConnection();
        }
    }

    // Method to ensure connection is valid before use
    public static Connection getValidConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            return getConnection();
        }

        // Test if connection is actually valid
        try {
            connection.createStatement().execute("SELECT 1");
            return connection;
        } catch (SQLException e) {
            // Connection is not valid, create new one
            return getConnection();
        }
    }
}