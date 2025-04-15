package BusinessLayer;

import java.util.Dictionary;
import java.util.List;

public class ProductFacade
{
    private Dictionary<String, List<Product>> productsByCategory;
    private Dictionary<String, Product> productsByName;

    void AddProduct(String name, List<String> subCategories, String manufacturer, int shelfQuantity, int storageQuantity, int minQuantity, double costPrice, double salePrice, List<Discount> discountHistory)
    {

    }
    void SetDiscountForProduct(String productName)
    {

    }
    void SetDiscountForCategory(String productCategory)
    {

    }
    int GetQuantity(String productName)
    {
        return 0;
    }
}
