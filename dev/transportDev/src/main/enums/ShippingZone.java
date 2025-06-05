package transportDev.src.main.enums;

public enum ShippingZone {
    // :)
     NORTH("North"),
    CENTER("Center"),
    SOUTH("South"),
    JERUSALEM("Jerusalem");

    private final String displayName;

    ShippingZone(String displayName) {
        this.displayName = displayName;
    }
    
     public static ShippingZone getByName(String name) {
        try {
        return ShippingZone.valueOf(name.toUpperCase());
    } catch (IllegalArgumentException e) {
        return null;
    }
    }

   @Override
    public String toString() {
        return displayName;
    }
}
