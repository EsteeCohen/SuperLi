package serviceLayer;

public enum WageTypeSL {
    HOURLY('H'),
    GLOBAL('G');

    private final char code;

    private WageTypeSL(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static WageTypeSL fromChar(char code) {
        for (WageTypeSL wageType : WageTypeSL.values()) {
            if (wageType.getCode() == code) {
                return wageType;
            }
        }
        throw new IllegalArgumentException("Invalid WageType code: " + code);
    }

}
