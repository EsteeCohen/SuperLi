package DomainLayer.Supplier.Enums;

public enum DaysOfTheWeek {
    SUNDAY,
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY;

    public static DaysOfTheWeek of(int index) {
        if (index < 1 || index > 7) {
            throw new IllegalArgumentException("index must be 1‑7 (got " + index + ')');
        }
        return values()[index - 1];
    }
}
