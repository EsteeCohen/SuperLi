package ServiceLayer;

import DomainLayer.ReportFacade;

import java.time.LocalDate;
import java.util.List;

class ReportService
{
    private final ReportFacade rf;
    ReportService(ReportFacade rf)
    {
        this.rf=rf;
    }

    public String GenerateExpiryReport(LocalDate until)
    {
        rf.GenerateExpiryReport(until);
        return rf.GetLatestExpiryReport();
    }
    public String GenerateInventoryReport(String category)
    {
        rf.GenerateInventoryReport(category);
        return rf.GetLatestInventoryReport();
    }
    public String GenerateInventoryReport(List<String> categories)
    {
        rf.GenerateInventoryReport(categories);
        return rf.GetLatestInventoryReport();
    }
    public String GenerateDamageReport()
    {
        rf.GenerateDamageReport();
        return rf.GetLatestDamageReport();
    }
    public String GenerateAbscenceReport()
    {
        rf.GenerateAbscenceReport();
        return rf.GetLatestAbscenceReport();
    }


}
