package DataAccessLayer.interfacesDAO;
import DataAccessLayer.DTO.InventoryProductDTO;
import DomainLayer.Inventory.Product;

import java.time.LocalDate;
import java.util.List;
//product: name, category, [subCategories], manufacturer, shelfLocation, storageLocation, minQuantity, sellPrice, discount
public interface InventoryProductDAO {
    void create(String category, Product product);
    InventoryProductDTO read(String ProductName);
    List<InventoryProductDTO> readAll();
    void setDiscountForCategory(String category, LocalDate start, LocalDate end, double percentage);
    void update(Product product);
}
