package transportDev.src.main.enums;

public enum TransportStatus {
    PLANNING("בתכנון"),
    ACTIVE("פעילה"),
    COMPLETED("הושלמה"),
    CANCELLED("בוטלה");

    private final String hebrewName;

    /**
     * Constructor for TransportStatus enum.
     *
     * @param hebrewName The Hebrew name of the transport status.
     */
    TransportStatus(String hebrewName) {
        this.hebrewName = hebrewName;
    }

    /**
     * Returns the Hebrew name of the transport status.
     *
     * @return The Hebrew name of the transport status.
     */
    public static TransportStatus getByHebrewName(String hebrewName){
        for (TransportStatus status : TransportStatus.values()) {
            if (status.toString().equals(hebrewName)) {
                return status;
            }
        }
        return null; // or throw an exception if preferred
    }
    
    /**
     * Checks if the current status can transition to a new status.
     *
     * @param newStatus The new transport status to check.
     * @return true if the transition is valid, false otherwise.
     */
    public boolean canTransitionTo(TransportStatus newStatus){
        switch (this) {
            case PLANNING:
                return newStatus == ACTIVE || newStatus == CANCELLED;
            case ACTIVE:
                return newStatus == COMPLETED || newStatus == CANCELLED;
            case COMPLETED:
                return false; // No transitions allowed from COMPLETED
            case CANCELLED:
                return false; // No transitions allowed from CANCELLED
            default:
                throw new IllegalStateException("Unexpected value: " + this);
        }
    }

    @Override
    public String toString() {
        return hebrewName;
    }



    
}
