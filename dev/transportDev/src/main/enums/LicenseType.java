package src.main.enums;

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
}
