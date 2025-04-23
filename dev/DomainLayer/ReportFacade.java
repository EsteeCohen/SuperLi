package DomainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportFacade
{
    List<DamageReport> damageReports;
    List<ExpiryReport> expiryReports;
    List<InventoryReport> inventoryReports;

    ProductFacade pf;

    public void GenerateExpiryReport(LocalDate until)
    {
        List<Product> allProducts = pf.GetAllProducts();
        ExpiryReport expiryReport = new ExpiryReport();

        for (Product product : allProducts) {
            ExpiryDesc currentDesc = product.GetExpiryDescription(until);
            if (currentDesc.isNotEmpty())
                expiryReport.add(currentDesc);
        }
        expiryReports.add(expiryReport);
    }
    public void GenerateInventoryReport(String category)
    {
        InventoryReport inventoryReport = new InventoryReport();
        List<Product> products = pf.GetProductsByCategory(category);

        for (Product product : products) {
            inventoryReport.add(product.GetInventoryDescription());
        }
        inventoryReports.add(inventoryReport);

    }
    public void GenerateInventoryReport(List<String> categories)
    {
        InventoryReport inventoryReport = new InventoryReport();
        List<Product> products = pf.GetProductsByCategories(categories);

        for (Product product : products) {
            inventoryReport.add(product.GetInventoryDescription());
        }
        inventoryReports.add(inventoryReport);

    }
    public void GenerateDamageReport()
    {
        DamageReport damageReport = new DamageReport();
        List<Product> products = pf.GetAllProducts();

        for (Product product : products) {
            damageReport.add(product.GetDamageDescription());
        }
        damageReports.add(damageReport);
    }

}
