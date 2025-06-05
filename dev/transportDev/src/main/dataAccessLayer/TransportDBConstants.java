package transportDev.src.main.dataAccessLayer;

public class TransportDBConstants {
    public static final String DB_PATH = "dev\\db.db";
    
    // Table names
    public static final String TRANSPORT_TABLE = "Transports";
    public static final String TRUCK_TABLE = "Trucks";
    public static final String DRIVER_TABLE = "Drivers";
    public static final String SITE_TABLE = "Sites";
    public static final String ORDER_TABLE = "Orders";
    public static final String ITEM_TABLE = "Items";
    public static final String TRANSPORT_DESTINATIONS_TABLE = "TransportDestinations";
    public static final String ORDER_ITEMS_TABLE = "OrderItems";
    public static final String TRUCK_LICENSE_TYPES_TABLE = "TruckLicenseTypes";
    public static final String DRIVER_LICENSE_TYPES_TABLE = "DriverLicenseTypes";

    private TransportDBConstants() {
        // Prevent instantiation
    }
} // :)