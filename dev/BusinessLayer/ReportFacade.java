package BusinessLayer;

import java.time.LocalDate;
import java.util.List;

public class ReportFacade
{
    private List<ExpiryReport> expiryReports;
    private List<InventoryReport> inventoryReports;

    String generateExpiryReport(LocalDate start, LocalDate end)
    {
        return null;
    }
    String generateInventoryReport(String category)
    {
        return null;
    }
    String generateInventoryReport(List<String> categories)
    {
        return null;
    }
    void reportExpired(String productName, int quantity)
    {

    }
}
