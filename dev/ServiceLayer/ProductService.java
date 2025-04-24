package ServiceLayer;

import DomainLayer.ProductFacade;

import java.time.LocalDate;
import java.util.List;

public class ProductService
{
    private ProductFacade pf;

    /**
     * adds a new product to the system
     * @param productName name
     * @param category category
     * @param subCategories sub categories
     * @param manufacturer manufacturer
     * @param sellPrice the price the supermarket sells the product for
     * @param shelfLocation the location of the product on the shelfs
     * @param storageLocation the locations in the storage where the product is stored
     */
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

    /**
     * adds a new supply of a product
     * @param productName the product the supply belongs to
     * @param cost how much did the supermarket payed per item
     * @param expirationDate when does the supply get expired
     * @param shelfQuantity how many are on the shelfs
     * @param storageQuantity how many are in the storage
     * @return the supply id
     * @throws Exception if product not found or either of the quantities or cost in negative
     */
    public int addSupply(String productName, int cost, LocalDate expirationDate,int shelfQuantity,int storageQuantity) throws Exception
    {
        return pf.addSupply(productName,cost,expirationDate,shelfQuantity,storageQuantity);
    }

    /**
     * sets a discount for all products in a category
     * @param category the category to put a discount on
     * @param startDate start date of the discount
     * @param endDate end date of the discount
     * @param percentage discount percentage
     */
    public void SetDiscountForCategory(String category, LocalDate startDate, LocalDate endDate, double percentage)
    {
        try
        {
            pf.SetDiscountForCategory(category, startDate, endDate, percentage);
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }

    /**
     * sets a discount for a specific product
     * @param productName the name of the product
     * @param startDate the start date for the discount
     * @param endDate the end date for the discount
     * @param percentage the discount percentage
     */
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

    /**
     * update the product supply quantities and report all the missing items as sold
     * @param productName the product to update
     * @param supplyId the id of the supply, should be pn the barcode ;)
     * @param shelfCount the new quantity on the shelfs
     * @param storageCount the new quantity in the storage
     * @throws Exception if product not found, new quantity is greater than old quantity
     */
    public void reportSales(String productName, int supplyId, int shelfCount, int storageCount) throws Exception
    {
    }
}
