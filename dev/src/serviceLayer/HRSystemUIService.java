package serviceLayer;

public class HRSystemUIService {
    private static HRSystemUIService instance = null;
    private HRSystemUIService() {
        // Private constructor to prevent instantiation
    }

    public static HRSystemUIService getInstance() {
        if (instance == null) {
            instance = new HRSystemUIService();
        }
        return instance;
    }

}
