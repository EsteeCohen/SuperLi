package DataAccessLayer.interfacesDAO;


import DomainLayer.Supplier.Product;
import java.util.List;

public interface ProductDAO {
    void create(Product product);
    Product read(String catalogNumber);
    List<Product> readAll();
    List<Product> readAllBySupplier(String supplierId);
    void update(Product product);
    void delete(String catalogNumber);

    void deleteBySupplier(String supplierId);
}

