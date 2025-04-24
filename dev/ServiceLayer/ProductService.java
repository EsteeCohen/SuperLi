package ServiceLayer;

import DomainLayer.ProductFacade;

import java.time.LocalDate;
import java.util.List;

class ProductService
{
    private final ProductFacade pf;
    ProductService(ProductFacade pf)
    {
        this.pf=pf;
    }

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
    public String AddProduct(String productName, String category, List<String> subCategories, String manufacturer, double sellPrice, String shelfLocation, String storageLocation) throws Exception
    {
        pf.AddProduct(productName, category, subCategories, manufacturer, sellPrice,shelfLocation,storageLocation);
        return "added product "+productName+" to category "+category;
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
    public String addSupply(String productName, double cost, LocalDate expirationDate,int shelfQuantity,int storageQuantity) throws Exception
    {
        return "added new supply, supply ID: "+String.valueOf(pf.addSupply(productName,cost,expirationDate,shelfQuantity,storageQuantity));
    }

    /**
     * sets a discount for all products in a category
     * @param category the category to put a discount on
     * @param startDate start date of the discount
     * @param endDate end date of the discount
     * @param percentage discount percentage
     */
    public String SetDiscountForCategory(String category, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        pf.SetDiscountForCategory(category, startDate, endDate, percentage);
        return "added discount for category: "+category;
    }

    /**
     * sets a discount for a specific product
     * @param productName the name of the product
     * @param startDate the start date for the discount
     * @param endDate the end date for the discount
     * @param percentage the discount percentage
     */
    public String SetDiscount(String productName, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        pf.SetDiscount(productName, startDate, endDate, percentage);
        return "added discount for product: "+productName;
    }

    /**
     * update the product supply quantities and report all the missing items as sold
     * @param productName the product to update
     * @param supplyId the id of the supply, should be pn the barcode ;)
     * @param shelfCount the new quantity on the shelfs
     * @param storageCount the new quantity in the storage
     * @return the string representing the report
     * @throws Exception if product not found, new quantity is greater than old quantity
     */
    public String reportSales(String productName, int supplyId, int shelfCount, int storageCount) throws Exception
    {
        return pf.updateSoldQuantity(productName,supplyId,shelfCount,storageCount)? "updated" + System.lineSeparator()+ "Alert! "+productName+" quantity has reached below minimal quantity! pls order a new supply ASAP!":"updated";
    }

    /**
     * update the product supply quantities and report all the missing items as broken
     * @param productName the product to update
     * @param supplyId the id of the supply, should be pn the barcode ;)
     * @param shelfCount the new quantity on the shelfs
     * @param storageCount the new quantity in the storage
     * @return the string representing the report
     * @throws Exception if product not found, new quantity is greater than old quantity
     */
    public String reportBroke(String productName, int supplyId, int shelfCount, int storageCount) throws Exception
    {
        return pf.updateBrokenQuantity(productName,supplyId,shelfCount,storageCount)? "updated" + System.lineSeparator()+ "Alert! "+productName+" quantity has reached below minimal quantity! pls order a new supply ASAP!":"updated";
    }
}
