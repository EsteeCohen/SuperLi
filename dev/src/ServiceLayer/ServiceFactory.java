package ServiceLayer;
import DomainLayer.Inventory.ProductFacade;
import DomainLayer.Inventory.ReportFacade;
import DomainLayer.OrderController;
import DomainLayer.Supplier.SystemController;


import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/*
unifies all the services in one place and allows to use the system
 */
public class ServiceFactory {

    private final ProductFacade productFacade;
    private final ReportFacade reportFacade;
    private final SystemController systemController;
    private final ProductService productService;
    private final ReportService reportService;
    public ServiceFactory()
    {
        productFacade=ProductFacade.getInstance();
        reportFacade=ReportFacade.getInstance();
        productService=new ProductService(productFacade);
        reportService=new ReportService(reportFacade);
        systemController=SystemController.getInstance();

    }

    public String GenerateExpiryReport(LocalDate until) {
        return reportService.GenerateExpiryReport(until);
    }
    public String GenerateDamageReport() {
        return reportService.GenerateDamageReport();
    }
    public String GenerateInventoryReport(List<String> categories) {
        return reportService.GenerateInventoryReport(categories);
    }
    public String GenerateInventoryReport(String category) {
        return reportService.GenerateInventoryReport(category);
    }
    public String GenerateAbscenceReport()
    {
        return reportService.GenerateAbscenceReport();
    }
    public String reportBroke(String productName, int supplyId, int shelfCount, int storageCount) throws Exception
    {
        return productService.reportBroke(productName,supplyId,shelfCount,storageCount);
    }
    public String reportSales(String productName, int supplyId, int shelfCount, int storageCount) throws Exception
    {
        return productService.reportSales(productName,supplyId,shelfCount,storageCount);
    }

    public String addSupply(String productName,double cost, LocalDate expirationDate, int shelfQuantity, int storageQuantity) throws Exception
    {
        return productService.addSupply(productName,cost,expirationDate,shelfQuantity,storageQuantity);
    }
    public String SetDiscountForCategory(String category, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        return productService.SetDiscountForCategory(category,startDate,endDate,percentage);
    }
    public String SetDiscount(String productName, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        return productService.SetDiscount(productName,startDate,endDate,percentage);
    }
    public String AddProduct(String productName, String category, List<String> subCategories, String manufacturer, double sellPrice, String shelfLocation, String storageLocation) throws  Exception
    {
        return productService.AddProduct(productName,category,subCategories,manufacturer,sellPrice,shelfLocation,storageLocation);
    }
}
