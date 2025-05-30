package DomainLayer.Inventory;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import DataAccessLayer.DTO.InventoryProductDTO;
import DataAccessLayer.DTO.SupplyDTO;
import DataAccessLayer.InventoryProductDAOImpl;
import DataAccessLayer.SupplyDAOImpl;
import DataAccessLayer.interfacesDAO.InventoryProductDAO;
import DataAccessLayer.interfacesDAO.SupplyDAO;
import DomainLayer.OrderController;
import ServiceLayer.ServiceFactory;

public class ProductFacade
{
    private final Map<String, List<Product>> productsByCategory=new HashMap<>();
    private final Map<String, Product> productsByName=new HashMap<>();
    private static ProductFacade instance=null;
    private InventoryProductDAO inventoryProductDAO;
    private SupplyDAO supplyDAO;
    //singelton getter
    public static ProductFacade getInstance()
    {
        if(instance==null)
            instance=new ProductFacade();
        return instance;
    }
    //in order to load new data, we want to flush out the previous data
    public static void flush()
    {
        instance=new ProductFacade();
    }
    private ProductFacade()
    {
        try {
            inventoryProductDAO = new InventoryProductDAOImpl();
            supplyDAO = new SupplyDAOImpl();
            initiaize();
        }
        catch (Exception e)
        {
            System.out.println("Failed to connect to database! ProductFacade is not initialized! "+e.getMessage());
            e.printStackTrace();
            inventoryProductDAO=null;
            supplyDAO=null;
        }
    }

    /**
     * run in the start and load the database into the facade
     */
    private void initiaize() throws Exception
    {
        List<InventoryProductDTO> productsDTO=inventoryProductDAO.readAll();
        for(InventoryProductDTO p:productsDTO)
        {
            //load the product
            addInternalProduct(p.productName(),p.category(),p.subCategories(),p.manufacturer(),p.sellPrice(),p.shelfLocation(),p.storageLocation());
            //if have discount, load it too
            if(p.discountStart()!=null && p.discountEnd()!=null && p.discountPercentage()!=0.0)
                SetDiscount(p.productName(),p.discountStart(),p.discountEnd(),p.discountPercentage());
            getProduct(p.productName()).setMinQuantity(p.minQuantity());

            //load all the supplies of this product
            List<SupplyDTO> suppliesOfProduct= supplyDAO.readAll(p.productName());
            for(SupplyDTO s:suppliesOfProduct)
            {
                addInternalSupplyPreditermentId(p.productName(),s.supplyId(),s.costPrice(),s.expirationDate(),s.shelfQuantity(),s.storageQuantity());
            }
        }
    }

    /**
     * adds a new product to the system and updates the database
     * @param productName the name of the new product
     * @param category the category of the new product
     * @param subCategories the sub cats
     * @param manufacturer manu
     * @param sellPrice the current sell price
     * @throws Exception if product already exists
     */
    public void AddProduct(String productName, String category, List<String> subCategories, String manufacturer, double sellPrice, String shelfLocation, String storageLocation) throws Exception
    {
        Product product=addInternalProduct(productName,category,subCategories,manufacturer,sellPrice,shelfLocation,storageLocation);
        if(inventoryProductDAO!=null)
            inventoryProductDAO.create(category,product);

    }

    /**
     * adds a new product to the system
     * @param productName the name of the new product
     * @param category the category of the new product
     * @param subCategories the sub cats
     * @param manufacturer manu
     * @param sellPrice the current sell price
     * @throws Exception if product already exists
     */
    private Product addInternalProduct(String productName, String category, List<String> subCategories, String manufacturer, double sellPrice, String shelfLocation, String storageLocation) throws Exception
    {
        if(getProduct(productName)!=null)
            throw new Exception("Product with name: "+productName+" already exists!");
        if (!productsByCategory.containsKey(category))
            productsByCategory.put(category, new ArrayList<>());
        Product product=new Product(productName, subCategories, manufacturer, sellPrice,shelfLocation,storageLocation);
        productsByCategory.get(category).add(product);
        productsByName.put(productName, product);
        return product;
    }

    /**
     * sets a discount for all the products in the category
     * @param productCategory the ategory
     * @param startDate discunt start date
     * @param endDate discount end date
     * @param percentage the discount precentage
     * @throws Exception if the category doesnt exist or if its empty
     */
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
            if(inventoryProductDAO!=null)
                inventoryProductDAO.setDiscountForCategory(productCategory,startDate,endDate,percentage);
        }
    }

    /**
     * sets a discount for a single product
     * @param productName name
     * @param startDate discount tart date
     * @param endDate discount end date
     * @param percentage discount precentage
     * @throws Exception if the product doesnt exists
     */
    public void SetDiscount(String productName, LocalDate startDate, LocalDate endDate, double percentage) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else {
            productsByName.get(productName).SetDiscount(new Discount(startDate, endDate, percentage));
            if(inventoryProductDAO!=null)
                inventoryProductDAO.update(productsByName.get(productName));
        }
    }

    /**
     * gets the shelf quantity of a product
     * @param productName name
     * @return the quantiy
     * @throws Exception if product doesnt exists
     */
    public int GetShelfQuantity(String productName) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else return productsByName.get(productName).GetShelfQuantity();
    }

    /**
     * gets the storage quantity of a product
     * @param productName name
     * @return quantity
     * @throws Exception if product doesnt exist
     */
    public int GetStorageQuantity(String productName) throws Exception
    {
        if (!productsByName.containsKey(productName))
            throw new Exception("No product is named " + productName);
        else return productsByName.get(productName).GetStorageQuantity();
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
    public int addSupply(String productName, double cost, LocalDate expirationDate,int shelfQuantity,int storageQuantity) throws Exception
    {
        Product.Supply supply=addInternalSupply(productName,cost,expirationDate,shelfQuantity,storageQuantity);
        if(supplyDAO!=null)
            supplyDAO.create(productName,supply);
        return supply.getSupplyID();
    }

    /**
     * adds a new supply of a product without saving it to the database
     * @param productName the product the supply belongs to
     * @param cost how much did the supermarket payed per item
     * @param expirationDate when does the supply get expired
     * @param shelfQuantity how many are on the shelfs
     * @param storageQuantity how many are in the storage
     * @return the supply
     * @throws Exception if product not found or either of the quantities or cost in negative
     */
    private Product.Supply addInternalSupply(String productName, double cost, LocalDate expirationDate,int shelfQuantity,int storageQuantity) throws Exception
    {
        Product p=getProduct(productName);
        if(p==null)
            throw new Exception("Product "+productName+" does not exist!");

        Product.Supply supply= p.addSupply(cost,expirationDate,shelfQuantity,storageQuantity);

        return supply;
    }

    //add internal supply, except when we read from the database meaning we know the id already
    private void addInternalSupplyPreditermentId(String productName,int supplyId, double cost, LocalDate expirationDate,int shelfQuantity,int storageQuantity) throws Exception
    {
        Product p=getProduct(productName);
        if(p==null)
            throw new Exception("Product "+productName+" does not exist!");

        p.addSupply(supplyId,cost,expirationDate,shelfQuantity,storageQuantity);
    }
    /**
     * update the supply quantity and mark all missing product as sold
     * @param productName the product to update
     * @param supplyId the id of the supply to update
     * @param newShelfQuantity the new quantity on the shelfs
     * @param newStorageQuantity the new quantity in the storage
     * @return is the new total quantity< min quantity
     * @throws Exception if product not found, supply not found, or the new quantity is greater than the old quantity
     */
    public boolean updateSoldQuantity(String productName, int supplyId, int newShelfQuantity, int newStorageQuantity) throws Exception
    {
        Product p=getProduct(productName);
        if(p==null)
            throw new Exception("Product " +productName+" is not found!");
        Product.Supply sup= p.updateSoldQuantity(supplyId,newShelfQuantity,newStorageQuantity);
        if(supplyDAO!=null) {
            if (sup.getShelfQuantity() + sup.getStorageQuantity() == 0)
                supplyDAO.delete(productName, sup.getSupplyID());
            else
                supplyDAO.update(productName, sup);
        }
        p.calcMinQuantity();
        if(inventoryProductDAO!=null)
            inventoryProductDAO.update(p);
        int total=p.GetShelfQuantity()+p.GetStorageQuantity();
        boolean shortage=total<p.getMinQuantity();
        if(shortage)
        {
            OrderController.getInstance().reportStockShortage(productName,p.getMinQuantity()-total);
        }
        return shortage;
    }

    /**
     * update the supply quantity and mark all missing product as broken
     * @param productName the product to update
     * @param supplyId the id of the supply to update
     * @param newShelfQuantity the new quantity on the shelfs
     * @param newStorageQuantity the new quantity in the storage
     * @return is the new total quantity< min quantity
     * @throws Exception if product not found, supply not found, or the new quantity is greater than the old quantity
     */
    public boolean updateBrokenQuantity(String productName, int supplyId, int newShelfQuantity, int newStorageQuantity) throws Exception
    {
        Product p=getProduct(productName);
        if(p==null)
            throw new Exception("Product " +productName+" is not found!");
        Product.Supply sup=p.updateFoundBrokenItems(supplyId,newShelfQuantity,newStorageQuantity);

        if(supplyDAO!=null) {
            if (sup.getShelfQuantity() + sup.getStorageQuantity() == 0)
                supplyDAO.delete(productName, sup.getSupplyID());
            else
                supplyDAO.update(productName, sup);
        }
        int total=p.GetShelfQuantity()+p.GetStorageQuantity();
        boolean shortage=total<p.getMinQuantity();
        if(shortage)
        {
            OrderController.getInstance().reportStockShortage(productName,p.getMinQuantity()-total);
        }
        return shortage;
    }

    /**
     * called every monday and thursday before the inventory man updates the sales
     */
    public void restartSale()
    {
        for(Map.Entry<String,Product> entry:productsByName.entrySet())
        {
            entry.getValue().restartSales();
        }
    }

    //#################################################################################
    //for tests!!!!
    //gets a product by its name
    Product getProduct(String name)
    {
        return productsByName.get(name);
    }

    //gets a product from within a certain category, mainly for tests
    Product getProductFromCategory(String name, String category) {

        List<Product> products=productsByCategory.get(category);
        if(products==null)
            return null;
        for(Product p:products)
        {
            if(p.getProductName().equals(name))
                return p;
        }
        return null;
    }

    public List<Product> GetProductsByCategories(List<String> categories)
    {
        List<Product> products = new ArrayList<>();
        for (int i = 0; i < categories.size(); i++)
        {
            products.addAll(productsByCategory.get(categories.get(i)));
        }
        return products;
    }

    public List<Product> GetProductsByCategory(String category)
    {
        return productsByCategory.get(category);
    }

    public List<Product> GetAllProducts()
    {
        return new ArrayList<>(productsByName.values());
    }
}
