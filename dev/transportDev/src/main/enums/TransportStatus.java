package transportDev.src.main.enums;

public enum TransportStatus {
    // :)
    PLANNING("Planning"),
    ACTIVE("Active"),
    COMPLETED("Completed"),
    CANCELLED("Cancelled");

    private final String displayName;

    TransportStatus(String displayName) {
        this.displayName = displayName;
    }

    public static TransportStatus getByName(String name) {
        for (TransportStatus status : TransportStatus.values()) {
            if (status.toString().equalsIgnoreCase(name)) {
                return status;
            }
        }
        return null; // or throw exception
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
        return displayName;
    }
    
}
