package DataAccessLayer.interfacesDAO;
import DataAccessLayer.DTO.SupplyDTO;
import DomainLayer.Inventory.Product;
import java.util.List;

// supply: productName, supplyId, shelfQuantity, storageQuantity, expirationDate, costPrice
public interface SupplyDAO {
    void create(String productName, Product.Supply supply);
    SupplyDTO read(String productName, int supplyId);
    List<SupplyDTO> readAll(String productName);
    void update(String productName, Product.Supply supply);
    void delete(String productName, int supplyId);
}
