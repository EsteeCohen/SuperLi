package ServiceLayer;

import DomainLayer.ReportFacade;

import java.time.LocalDate;
import java.util.List;

class ReportService
{
<<<<<<< HEAD
    private final ReportFacade rf;
    ReportService(ReportFacade rf)
    {
        this.rf=rf;
    }
=======
    private ReportFacade rf;
>>>>>>> d1700c56bf3ed291593488561a2c7c09bf4cad8e

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
