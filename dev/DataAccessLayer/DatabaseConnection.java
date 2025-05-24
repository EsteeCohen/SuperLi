package DataAccessLayer;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

// Manages the DB connection (Singleton)
public class DatabaseConnection {
    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection("jdbc:sqlite:C:/Users/shiri/ADSS_Group_AI/dev/supplyinventory.db");
        }
        return connection;
    }
}
