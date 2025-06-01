package employeeDev.src.domainLayer.Enums;

public enum ShiftType {
    MORNING("Morning"),
    EVENING("Evening");

    private final String displayName;

    ShiftType(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }
}
