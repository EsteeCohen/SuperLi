package DomainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class ReportFacade
{
    DamageReport damageReport;
    ExpiryReport expiryReport;
    InventoryReport inventoryReport;
    AbscenceReport abscenceReport;

    ProductFacade pf;

    public ReportFacade(ProductFacade pf)
    {
        this.pf=pf;

    }

    public void GenerateExpiryReport(LocalDate until)
    {
        List<Product> allProducts = pf.GetAllProducts();
        ExpiryReport expiryReport = new ExpiryReport();

        for (Product product : allProducts) {
            ExpiryDesc currentDesc = product.GetExpiryDescription(until);
            if (currentDesc.isNotEmpty())
                expiryReport.add(currentDesc);
        }
        this.expiryReport = expiryReport;
    }
    public void GenerateInventoryReport(String category)
    {
        InventoryReport inventoryReport = new InventoryReport();
        List<Product> products = pf.GetProductsByCategory(category);

        for (Product product : products) {
            inventoryReport.add(product.GetInventoryDescription());
        }
        this.inventoryReport = inventoryReport;

    }
    public void GenerateInventoryReport(List<String> categories)
    {
        InventoryReport inventoryReport = new InventoryReport();
        List<Product> products = pf.GetProductsByCategories(categories);

        for (Product product : products) {
            inventoryReport.add(product.GetInventoryDescription());
        }
        this.inventoryReport = inventoryReport;

    }
    public void GenerateDamageReport()
    {
        DamageReport damageReport = new DamageReport();
        List<Product> products = pf.GetAllProducts();

        for (Product product : products) {
            damageReport.add(product.GetDamageDescription());
        }
        this.damageReport = damageReport;
    }
    public void GenerateAbscenceReport()
    {
        AbscenceReport abscenceReport = new AbscenceReport();
        List<Product> products = pf.GetAllProducts();

        for (Product product : products) {
            abscenceReport.add(product.GetAbscenceDescription());
        }
        this.abscenceReport = abscenceReport;
    }

    public String GetLatestExpiryReport()
    {
        return expiryReport.toString();
    }

    public String GetLatestInventoryReport()
    {
        return inventoryReport.toString();
    }

    public String GetLatestDamageReport()
    {
        return damageReport.toString();
    }

    public String GetLatestAbscenceReport()
    {
        return abscenceReport.toString();
    }
}
