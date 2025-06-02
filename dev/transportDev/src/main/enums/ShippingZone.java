package transportDev.src.main.enums;

public enum ShippingZone {
    NORTH("צפון"),
    CENTER("מרכז"),
    SOUTH("דרום"),
    JERUSALEM("ירושלים");

    private final String hebrewName;
    
    ShippingZone(String hebrewName) {
        this.hebrewName = hebrewName;
    }
    
    public static ShippingZone getByHebrewName(String hebrewName){
        for (ShippingZone zone : ShippingZone.values()) {
            if (zone.toString().equals(hebrewName)) {
                return zone;
            }
        }
        return null; // or throw an exception if preferred
    }

    @Override
    public String toString() {
        return hebrewName;
    }
}
