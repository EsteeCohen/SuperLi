package ServiceLayer;

import DomainLayer.ProductFacade;

import java.time.LocalDate;
import java.util.List;

public class ProductService
{
    protected ProductFacade pf;

    public void AddProduct(String productName, String category, List<String> subCategories, String manufacturer, int sellPrice, String shelfLocation, String storageLocation)
    {
        try
        {
            pf.AddProduct(productName, category, subCategories, manufacturer, sellPrice,shelfLocation,storageLocation);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void SetDiscountForProduct(String productName, LocalDate startDate, LocalDate endDate, double percentage)
    {
        try
        {
            pf.SetDiscountForCategory(productName, startDate, endDate, percentage);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    public void SetDiscount(String productName, LocalDate startDate, LocalDate endDate, double percentage)
    {
        try
        {
            pf.SetDiscount(productName, startDate, endDate, percentage);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    public String reportSales(String productName, int supplyId, int shelfCount, int storageCount) throws Exception
    {
        return "";
    }
}
