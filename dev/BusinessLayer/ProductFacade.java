package BusinessLayer;

import ServiceLayer.ProductService;

import java.sql.Array;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.List;
import java.util.Map;

public class ProductFacade
{
    private Map<String, List<Product>> productsByCategory;
    private Map<String, Product> productsByName;

    public void AddProduct(String productName, String category, List<String> subCategories, String manufacturer, int sellPrice) throws Exception
    {
        if (!productsByCategory.containsKey(productName))
            productsByCategory.put(category, new ArrayList<>());
        productsByCategory.get(category).add(new Product(productName, subCategories, manufacturer, sellPrice));

        productsByName.put(productName, new Product(productName, subCategories, manufacturer, sellPrice));
    }

    public void SetDiscountForProduct(String productName, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else
        {
            productsByName.get(productName).SetDiscount(new Discount(startDate, endDate, percentage));
        }
    }

    public void SetDiscountForCategory(String productCategory, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        List<Product> products = productsByCategory.get(productCategory);

        if (products == null)
            throw new Exception("No category is named " + productCategory);
        else if (products.isEmpty())
            throw new Exception("Category is empty");
        else for (Product product : products) product.SetDiscount(new Discount(startDate, endDate, percentage));
    }

    public void SetDiscount(String productName, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else productsByName.get(productName).SetDiscount(new Discount(startDate, endDate, percentage));
    }

    //צריך את זה בכלל?
    public int GetShelfQuantity(String productName) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else return productsByName.get(productName).GetShelfQuantity();
    }

    //צריך את זה בכלל?
    public int GetStorageQuantity(String productName) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else return productsByName.get(productName).GetStorageQuantity();
    }

    List<Product> GetAllProducts()
    {
        return new ArrayList<>(productsByName.values());
    }
    List<Product> GetProductsByCategory(String category)
    {
        return productsByCategory.get(category);
    }
    List<Product> GetProductsByCategories(List<String> categories)
    {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++)
        {
            products.addAll(GetProductsByCategory(categories.get(i)));
        }
        return products;
    }

}
