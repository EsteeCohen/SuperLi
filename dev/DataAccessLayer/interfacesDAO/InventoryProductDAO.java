package DataAccessLayer.interfacesDAO;
import DomainLayer.Inventory.Product;

import java.util.List;
//product: name, category, [subCategories], manufacturer, shelfLocation, storageLocation, minQuantity, sellPrice, category, discount
public interface InventoryProductDAO {
    void create(String category, String ProductName);
    Product read(String ProductName);
    List<Product> readAll();
    void update(Product product);
}
