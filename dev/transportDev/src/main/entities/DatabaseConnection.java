package transportDev.src.main.entities;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private static Connection connection;
    private static final String DB_PATH = "db.db";

    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("SQLite JDBC driver not found", e);
        }
    }

    private DatabaseConnection() throws SQLException {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH);
        } catch (SQLException e) {
            throw new SQLException("Failed to connect to database", e);
        }
    }

    public static DatabaseConnection getInstance() throws SQLException {
        if (instance == null || connection.isClosed()) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public static Connection getValidConnection() throws SQLException {
        getInstance();
        return connection;
    }

    public void close() throws SQLException {
        if (connection != null && !connection.isClosed()) {
            connection.close();
        }
    }
    // :)
} 