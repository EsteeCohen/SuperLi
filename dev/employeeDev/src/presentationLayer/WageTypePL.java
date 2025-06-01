package employeeDev.src.presentationLayer;

public enum WageTypePL {
    HOURLY('H'),
    GLOBAL('G');

    private final char code;

    private WageTypePL(char code) {
        this.code = code;
    }

    public char getCode() {
        return code;
    }

    public static WageTypePL fromChar(char code) {
        for (WageTypePL wageType : WageTypePL.values()) {
            if (wageType.getCode() == code) {
                return wageType;
            }
        }
        throw new IllegalArgumentException("Invalid WageType code: " + code);
    }
}
