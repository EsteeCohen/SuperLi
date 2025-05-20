package DataAccessLayer.interfacesDAO;
import DomainLayer.Inventory.Product;
import java.util.List;

public interface SupplyPerProductDAO {
    void create(String ProductName, Product.Supply supply);
    Product.Supply read(String ProductName,int supplyId);
    List<Product.Supply> readAll();
    void update(Product.Supply supply);
    void delete(String ProductName, int supplyId);
}
