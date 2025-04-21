package DomainLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProductFacade
{
    private final Map<String, List<Product>> productsByCategory=new HashMap<>();
    private final Map<String, Product> productsByName=new HashMap<>();

    public void AddProduct(String productName, String category, List<String> subCategories, String manufacturer, int sellPrice) throws Exception
    {
        if(getProduct(productName)!=null)
            throw new Exception("Product with name: "+productName+" already exists!");
        if (!productsByCategory.containsKey(category))
            productsByCategory.put(category, new ArrayList<>());
        Product product=new Product(productName, subCategories, manufacturer, sellPrice);
        productsByCategory.get(category).add(product);

        productsByName.put(productName, product);
    }
    void SetDiscountForProduct(String productName, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
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
        else {
            for (Product product : products)
                product.SetDiscount(new Discount(startDate, endDate, percentage));
        }
    }
    public void SetDiscount(String productName, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else productsByName.get(productName).SetDiscount(new Discount(startDate, endDate, percentage));
    }
    public int GetShelfQuantity(String productName) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else return productsByName.get(productName).GetShelfQuantity();
    }
    public int GetStorageQuantity(String productName) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else return productsByName.get(productName).GetStorageQuantity();
    }

    //for tests!!!!
    Product getProduct(String name)
    {
        return productsByName.get(name);
    }

    Product getProductFromCategory(String name,String category)
    {
        List<Product> products= productsByCategory.get(category);
        if(products==null)
            return null;
        for(Product p:products)
        {
            if(p.getProductName().equals(name))
                return  p;
        }
        return null;
    }
}
