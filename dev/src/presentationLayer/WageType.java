package presentationLayer;

public enum WageType {
    HOURLY('H'),
    GLOBAL('G');

    private final char code;

    private WageType(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static WageType fromChar(char code) {
        for (WageType wageType : WageType.values()) {
            if (wageType.getCode() == code) {
                return wageType;
            }
        }
        throw new IllegalArgumentException("Invalid WageType code: " + code);
    }
}
