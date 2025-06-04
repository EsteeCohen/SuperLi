import java.sql.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class InitializeDatabase {
    
    private static final String DB_PATH = "../db.db";
    
    public static void main(String[] args) {
        System.out.println("Creating transport tables...");
        
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DB_PATH)) {
            
            // Create truck tables
            createTruckTables(conn);
            
            // Create transport tables
            createTransportTables(conn);
            
            // Create order and item tables
            createOrderTables(conn);
            
            // Update existing driver and site tables
            updateExistingTables(conn);
            
            // Insert sample data
            insertSampleData(conn);
            
            System.out.println("Database setup completed successfully!");
            
        } catch (SQLException e) {
            System.out.println("Error creating database: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void createTruckTables(Connection conn) throws SQLException {
        System.out.println("Creating truck tables...");
        
        // Trucks table
        String createTrucksTable = """
            CREATE TABLE IF NOT EXISTS Trucks (
                license_plate TEXT PRIMARY KEY,
                model TEXT NOT NULL,
                empty_weight REAL NOT NULL,
                max_weight REAL NOT NULL,
                available BOOLEAN DEFAULT 1
            )
        """;
        executeSQL(conn, createTrucksTable);
        
        // Truck license types table
        String createTruckLicenseTypes = """
            CREATE TABLE IF NOT EXISTS TruckLicenseTypes (
                truck_license_plate TEXT,
                license_type TEXT,
                PRIMARY KEY (truck_license_plate, license_type),
                FOREIGN KEY (truck_license_plate) REFERENCES Trucks(license_plate)
            )
        """;
        executeSQL(conn, createTruckLicenseTypes);
        
        // Driver license types table
        String createDriverLicenseTypes = """
            CREATE TABLE IF NOT EXISTS DriverLicenseTypes (
                driver_id TEXT,
                license_type TEXT,
                PRIMARY KEY (driver_id, license_type),
                FOREIGN KEY (driver_id) REFERENCES Drivers(id)
            )
        """;
        executeSQL(conn, createDriverLicenseTypes);
    }
    
    private static void createTransportTables(Connection conn) throws SQLException {
        System.out.println("Creating transport tables...");
        
        // Transports table
        String createTransports = """
            CREATE TABLE IF NOT EXISTS Transports (
                id INTEGER PRIMARY KEY,
                date TEXT NOT NULL,
                time TEXT NOT NULL,
                truck_license_plate TEXT,
                driver_id TEXT,
                source_site_name TEXT,
                current_weight REAL DEFAULT 0,
                status TEXT DEFAULT 'PLANNING',
                FOREIGN KEY (truck_license_plate) REFERENCES Trucks(license_plate),
                FOREIGN KEY (driver_id) REFERENCES Drivers(id),
                FOREIGN KEY (source_site_name) REFERENCES Sites(name)
            )
        """;
        executeSQL(conn, createTransports);
        
        // Transport destinations table
        String createTransportDestinations = """
            CREATE TABLE IF NOT EXISTS TransportDestinations (
                transport_id INTEGER,
                site_name TEXT,
                PRIMARY KEY (transport_id, site_name),
                FOREIGN KEY (transport_id) REFERENCES Transports(id),
                FOREIGN KEY (site_name) REFERENCES Sites(name)
            )
        """;
        executeSQL(conn, createTransportDestinations);
    }
    
    private static void createOrderTables(Connection conn) throws SQLException {
        System.out.println("Creating order tables...");
        
        // Items table
        String createItems = """
            CREATE TABLE IF NOT EXISTS Items (
                id INTEGER PRIMARY KEY,
                name TEXT NOT NULL,
                weight REAL NOT NULL,
                quantity INTEGER DEFAULT 0,
                description TEXT
            )
        """;
        executeSQL(conn, createItems);
        
        // Orders table
        String createOrders = """
            CREATE TABLE IF NOT EXISTS Orders (
                id INTEGER PRIMARY KEY,
                order_date TEXT NOT NULL,
                status TEXT DEFAULT 'PENDING',
                source_site_name TEXT,
                destination_site_name TEXT,
                total_weight REAL DEFAULT 0,
                FOREIGN KEY (source_site_name) REFERENCES Sites(name),
                FOREIGN KEY (destination_site_name) REFERENCES Sites(name)
            )
        """;
        executeSQL(conn, createOrders);
        
        // Order items table
        String createOrderItems = """
            CREATE TABLE IF NOT EXISTS OrderItems (
                order_id INTEGER,
                item_id INTEGER,
                quantity INTEGER DEFAULT 1,
                PRIMARY KEY (order_id, item_id),
                FOREIGN KEY (order_id) REFERENCES Orders(id),
                FOREIGN KEY (item_id) REFERENCES Items(id)
            )
        """;
        executeSQL(conn, createOrderItems);
    }
    
    private static void updateExistingTables(Connection conn) throws SQLException {
        System.out.println("Updating existing tables...");
        
        // Update drivers table
        try {
            executeSQL(conn, "ALTER TABLE Drivers ADD COLUMN name TEXT");
        } catch (SQLException e) {
            // Column already exists
        }
        try {
            executeSQL(conn, "ALTER TABLE Drivers ADD COLUMN phone_number TEXT");
        } catch (SQLException e) {
            // Column already exists
        }
        try {
            executeSQL(conn, "ALTER TABLE Drivers ADD COLUMN available BOOLEAN DEFAULT 1");
        } catch (SQLException e) {
            // Column already exists
        }
        
        // Update sites table
        try {
            executeSQL(conn, "ALTER TABLE Sites ADD COLUMN address TEXT");
        } catch (SQLException e) {
            // Column already exists
        }
        try {
            executeSQL(conn, "ALTER TABLE Sites ADD COLUMN phone_number TEXT");
        } catch (SQLException e) {
            // Column already exists
        }
        try {
            executeSQL(conn, "ALTER TABLE Sites ADD COLUMN contact_person TEXT");
        } catch (SQLException e) {
            // Column already exists
        }
        try {
            executeSQL(conn, "ALTER TABLE Sites ADD COLUMN shipping_zone TEXT");
        } catch (SQLException e) {
            // Column already exists
        }
    }
    
    private static void insertSampleData(Connection conn) throws SQLException {
        System.out.println("Inserting sample data...");
        
        // Trucks
        String[] trucks = {
            "INSERT OR IGNORE INTO Trucks VALUES ('12-345-01', 'Mercedes Actros', 8000, 26000, 1)",
            "INSERT OR IGNORE INTO Trucks VALUES ('23-456-02', 'Volvo FH', 7500, 24000, 1)",
            "INSERT OR IGNORE INTO Trucks VALUES ('34-567-03', 'Iveco Stralis', 7200, 22000, 0)",
            "INSERT OR IGNORE INTO Trucks VALUES ('45-678-04', 'Scania R-Series', 8200, 28000, 1)"
        };
        for (String sql : trucks) {
            executeSQL(conn, sql);
        }
        
        // Truck licenses
        String[] truckLicenses = {
            "INSERT OR IGNORE INTO TruckLicenseTypes VALUES ('12-345-01', 'C')",
            "INSERT OR IGNORE INTO TruckLicenseTypes VALUES ('12-345-01', 'C1')",
            "INSERT OR IGNORE INTO TruckLicenseTypes VALUES ('23-456-02', 'C')",
            "INSERT OR IGNORE INTO TruckLicenseTypes VALUES ('23-456-02', 'CE')",
            "INSERT OR IGNORE INTO TruckLicenseTypes VALUES ('34-567-03', 'C')",
            "INSERT OR IGNORE INTO TruckLicenseTypes VALUES ('45-678-04', 'C')",
            "INSERT OR IGNORE INTO TruckLicenseTypes VALUES ('45-678-04', 'CE')"
        };
        for (String sql : truckLicenses) {
            executeSQL(conn, sql);
        }
        
        // Drivers - add new drivers only if table is empty
        try {
            String checkDrivers = "SELECT COUNT(*) FROM Drivers";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(checkDrivers)) {
                rs.next();
                int count = rs.getInt(1);
                
                if (count == 0) {
                    // Table is empty, insert new data
                    String[] drivers = {
                        "INSERT OR IGNORE INTO Drivers (id, name, phone_number, available) VALUES ('321654987', 'Yossi Cohen', '050-1234567', 1)",
                        "INSERT OR IGNORE INTO Drivers (id, name, phone_number, available) VALUES ('432165098', 'Danny Levi', '052-2345678', 1)", 
                        "INSERT OR IGNORE INTO Drivers (id, name, phone_number, available) VALUES ('543216109', 'Michael David', '054-3456789', 0)",
                        "INSERT OR IGNORE INTO Drivers (id, name, phone_number, available) VALUES ('654321987', 'Avi Samuel', '053-4567890', 1)"
                    };
                    for (String sql : drivers) {
                        executeSQL(conn, sql);
                    }
                } else {
                    // Table not empty, only update missing fields
                    executeSQL(conn, "UPDATE Drivers SET name = 'Existing Driver', phone_number = '050-0000000', available = 1 WHERE name IS NULL");
                }
            }
        } catch (SQLException e) {
            System.out.println("Issue updating driver data: " + e.getMessage());
        }
        
        // Driver licenses
        String[] driverLicenses = {
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('321654987', 'B')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('321654987', 'C')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('432165098', 'B')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('432165098', 'C')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('432165098', 'CE')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('543216109', 'B')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('543216109', 'C1')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('654321987', 'B')",
            "INSERT OR IGNORE INTO DriverLicenseTypes VALUES ('654321987', 'C')"
        };
        for (String sql : driverLicenses) {
            executeSQL(conn, sql);
        }
        
        // Sites - add new sites only if table is empty
        try {
            String checkSites = "SELECT COUNT(*) FROM Sites";
            try (Statement stmt = conn.createStatement();
                 ResultSet rs = stmt.executeQuery(checkSites)) {
                rs.next();
                int count = rs.getInt(1);
                
                if (count == 0) {
                    // Table is empty, insert new data
                    String[] sites = {
                        "INSERT OR IGNORE INTO Sites (name, address, phone_number, contact_person, shipping_zone) VALUES ('Tel Aviv Branch', 'Dizengoff St 100, Tel Aviv', '03-1234567', 'Rachel Cohen', 'Center')",
                        "INSERT OR IGNORE INTO Sites (name, address, phone_number, contact_person, shipping_zone) VALUES ('Haifa Branch', 'Hanassi Blvd 50, Haifa', '04-2345678', 'Moshe Levi', 'North')",
                        "INSERT OR IGNORE INTO Sites (name, address, phone_number, contact_person, shipping_zone) VALUES ('Beer Sheva Branch', 'Rager St 25, Beer Sheva', '08-3456789', 'Sara David', 'South')",
                        "INSERT OR IGNORE INTO Sites (name, address, phone_number, contact_person, shipping_zone) VALUES ('Main Warehouse', 'Industrial Zone, Petah Tikva', '03-4567890', 'David Moshe', 'Center')"
                    };
                    for (String sql : sites) {
                        executeSQL(conn, sql);
                    }
                } else {
                    // Table not empty, only update missing fields
                    executeSQL(conn, "UPDATE Sites SET address = 'Unknown Address', phone_number = '03-0000000', contact_person = 'Contact Person', shipping_zone = 'General' WHERE address IS NULL");
                }
            }
        } catch (SQLException e) {
            System.out.println("Issue updating site data: " + e.getMessage());
        }
        
        // Items
        String[] items = {
            "INSERT OR IGNORE INTO Items VALUES (1, 'Milk Carton', 1.2, 100, '1 liter milk carton')",
            "INSERT OR IGNORE INTO Items VALUES (2, 'Flour Sack', 25.0, 50, '25kg flour sack')",
            "INSERT OR IGNORE INTO Items VALUES (3, 'Vegetable Crate', 15.5, 30, 'Mixed vegetable crate')",
            "INSERT OR IGNORE INTO Items VALUES (4, 'Oil Bottle', 0.9, 200, '1 liter olive oil bottle')",
            "INSERT OR IGNORE INTO Items VALUES (5, 'Canned Goods', 0.4, 500, 'Tomato sauce cans')"
        };
        for (String sql : items) {
            executeSQL(conn, sql);
        }
        
        System.out.println("Sample data inserted successfully!");
    }
    
    private static void executeSQL(Connection conn, String sql) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(sql);
        }
    }
} 