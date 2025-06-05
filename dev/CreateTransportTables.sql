-- Create Transport Management System Tables
-- Transport Management System Tables Creation

-- Trucks table
CREATE TABLE IF NOT EXISTS Trucks (
    license_plate TEXT PRIMARY KEY,
    model TEXT NOT NULL,
    empty_weight REAL NOT NULL,
    max_weight REAL NOT NULL,
    available BOOLEAN DEFAULT 1
);

-- Truck license types table
CREATE TABLE IF NOT EXISTS TruckLicenseTypes (
    truck_license_plate TEXT,
    license_type TEXT,
    PRIMARY KEY (truck_license_plate, license_type),
    FOREIGN KEY (truck_license_plate) REFERENCES Trucks(license_plate)
);

-- Driver license types table (complement to existing Drivers table)
CREATE TABLE IF NOT EXISTS DriverLicenseTypes (
    driver_id TEXT,
    license_type TEXT,
    PRIMARY KEY (driver_id, license_type),
    FOREIGN KEY (driver_id) REFERENCES Drivers(id)
);

-- Update existing drivers table (if needed)
-- Add missing columns to drivers table
ALTER TABLE Drivers ADD COLUMN name TEXT;
ALTER TABLE Drivers ADD COLUMN phone_number TEXT;
ALTER TABLE Drivers ADD COLUMN available BOOLEAN DEFAULT 1;

-- Transports table
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
);

-- Transport destinations table
CREATE TABLE IF NOT EXISTS TransportDestinations (
    transport_id INTEGER,
    site_name TEXT,
    PRIMARY KEY (transport_id, site_name),
    FOREIGN KEY (transport_id) REFERENCES Transports(id),
    FOREIGN KEY (site_name) REFERENCES Sites(name)
);

-- Items table
CREATE TABLE IF NOT EXISTS Items (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    weight REAL NOT NULL,
    quantity INTEGER DEFAULT 0,
    description TEXT
);

-- Orders table
CREATE TABLE IF NOT EXISTS Orders (
    id INTEGER PRIMARY KEY,
    order_date TEXT NOT NULL,
    status TEXT DEFAULT 'PENDING',
    source_site_name TEXT,
    destination_site_name TEXT,
    total_weight REAL DEFAULT 0,
    FOREIGN KEY (source_site_name) REFERENCES Sites(name),
    FOREIGN KEY (destination_site_name) REFERENCES Sites(name)
);

-- Order items table
CREATE TABLE IF NOT EXISTS OrderItems (
    order_id INTEGER,
    item_id INTEGER,
    quantity INTEGER DEFAULT 1,
    PRIMARY KEY (order_id, item_id),
    FOREIGN KEY (order_id) REFERENCES Orders(id),
    FOREIGN KEY (item_id) REFERENCES Items(id)
);

-- Add missing fields to Sites table (if needed)
ALTER TABLE Sites ADD COLUMN address TEXT;
ALTER TABLE Sites ADD COLUMN phone_number TEXT;
ALTER TABLE Sites ADD COLUMN contact_person TEXT;
ALTER TABLE Sites ADD COLUMN shipping_zone TEXT;

-- Insert sample truck data
INSERT OR IGNORE INTO Trucks (license_plate, model, empty_weight, max_weight, available) VALUES
('12-345-01', 'Mercedes Actros', 8000, 26000, 1),
('23-456-02', 'Volvo FH', 7500, 24000, 1),
('34-567-03', 'Iveco Stralis', 7200, 22000, 0),
('45-678-04', 'Scania R-Series', 8200, 28000, 1);

-- Insert truck license types
INSERT OR IGNORE INTO TruckLicenseTypes (truck_license_plate, license_type) VALUES
('12-345-01', 'C'),
('12-345-01', 'C1'),
('23-456-02', 'C'),
('23-456-02', 'CE'),
('34-567-03', 'C'),
('45-678-04', 'C'),
('45-678-04', 'CE');

-- Insert sample driver data (if table is empty)
INSERT OR IGNORE INTO Drivers (id, name, phone_number, available) VALUES
('321654987', 'Yossi Cohen', '050-1234567', 1),
('432165098', 'Danny Levi', '052-2345678', 1),
('543216109', 'Michael David', '054-3456789', 0),
('654321987', 'Avi Samuel', '053-4567890', 1);

-- Insert driver licenses
INSERT OR IGNORE INTO DriverLicenseTypes (driver_id, license_type) VALUES
('321654987', 'B'),
('321654987', 'C'),
('432165098', 'B'),
('432165098', 'C'),
('432165098', 'CE'),
('543216109', 'B'),
('543216109', 'C1'),
('654321987', 'B'),
('654321987', 'C');

-- Insert sample site data (if table is empty)
INSERT OR IGNORE INTO Sites (name, address, phone_number, contact_person, shipping_zone) VALUES
('Tel Aviv Branch', 'Dizengoff St 100, Tel Aviv', '03-1234567', 'Rachel Cohen', 'Center'),
('Haifa Branch', 'Hanassi Blvd 50, Haifa', '04-2345678', 'Moshe Levi', 'North'),
('Beer Sheva Branch', 'Rager St 25, Beer Sheva', '08-3456789', 'Sara David', 'South'),
('Main Warehouse', 'Industrial Zone, Petah Tikva', '03-4567890', 'David Moshe', 'Center');

-- Insert sample item data
INSERT OR IGNORE INTO Items (id, name, weight, quantity, description) VALUES
(1, 'Milk Carton', 1.2, 100, '1 liter milk carton'),
(2, 'Flour Sack', 25.0, 50, '25kg flour sack'),
(3, 'Vegetable Crate', 15.5, 30, 'Mixed vegetable crate'),
(4, 'Oil Bottle', 0.9, 200, '1 liter olive oil bottle'),
(5, 'Canned Goods', 0.4, 500, 'Tomato sauce cans');

-- Insert sample order data
INSERT OR IGNORE INTO Orders (id, order_date, status, source_site_name, destination_site_name, total_weight) VALUES
(1, '2024-01-15', 'PENDING', 'Main Warehouse', 'Tel Aviv Branch', 850.0),
(2, '2024-01-15', 'CONFIRMED', 'Main Warehouse', 'Haifa Branch', 1200.0),
(3, '2024-01-16', 'PENDING', 'Tel Aviv Branch', 'Beer Sheva Branch', 450.0);

-- Insert order items
INSERT OR IGNORE INTO OrderItems (order_id, item_id, quantity) VALUES
(1, 1, 50),
(1, 4, 100),
(2, 2, 20),
(2, 3, 15),
(2, 5, 100),
(3, 1, 30),
(3, 3, 10); 