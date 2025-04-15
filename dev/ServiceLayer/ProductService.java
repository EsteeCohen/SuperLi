package ServiceLayer;

import BusinessLayer.ProductFacade;

import java.util.List;

public class ProductService
{
    protected ProductFacade pf;

    public void AddProduct(String name, List<String> subCategories, String manufacturer, int shelfQuantity, int storageQuantity, int minQuantity, double costPrice, double salePrice, int supplyID)
    {

    }
    public void SetDiscountForProduct(String productName)
    {

    }
    public void SetDiscount(String productCategory)
    {

    }
    public int GetQuantity(String productName)
    {
        return 0;
    }
}
