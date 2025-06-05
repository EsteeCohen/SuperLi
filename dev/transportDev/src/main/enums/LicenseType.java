package transportDev.src.main.enums;

/**
 * Enum representing different types of licenses.
 */
public enum LicenseType {
    C1, // Light truck license
    C,  // Heavy truck license
    CE, // Heavy truck with trailer license
    C1E; // Light truck with trailer license

    public boolean isValidFor(LicenseType requiredLicense){
        return this.ordinal() >= requiredLicense.ordinal();
    }

    @Override
    public String toString() {
        switch (this) {
            case C1:
                return "C1 - Light truck license";
            case C:
                return "C - Heavy truck license";
            case CE:
                return "CE - Heavy truck with trailer license";
            case C1E:
                return "C1E - Light truck with trailer license";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public String getTypeInString() {
        switch (this) {
            case C1:
                return "C1";
            case C:
                return "C";
            case CE:
                return "CE";
            case C1E:
                return "C1E";
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    public static LicenseType fromString(String type) {
    for (LicenseType license : LicenseType.values()) {
        if (license.name().equalsIgnoreCase(type) || license.toString().equalsIgnoreCase(type)) {
            return license;
        }
    }
    throw new IllegalArgumentException("Unknown license type: " + type);
}

    public static boolean isValidLicenseType(String input) {
        try {
            LicenseType.valueOf(input.toUpperCase());
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
} // :)
