package DataAccessLayer.interfacesDAO;
import DomainLayer.Inventory.Product;

import java.util.List;
//product: name, shelfQuantity, storageQuantity, shelfLocation, storageLocation,
//         latestsSales?, minQuantity?, sellPrice, latestSupplyId?, category, discount
public interface InventoryProductDAO {
    void create(String category, String ProductName);
    List<String> read(String ProductName);
    List<String> readAll();
    void update(Product product);
}
