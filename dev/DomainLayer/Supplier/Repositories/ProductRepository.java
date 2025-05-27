package DomainLayer.Supplier.Repositories;
import DomainLayer.Supplier.Product;

import java.util.List;
import java.sql.SQLException;

public interface ProductRepository {
    void add(Product product) throws SQLException;
    Product getByCatalog(String catalogNumber) throws SQLException;
    List<Product> getAll() throws SQLException;

    void remove(Product product) throws  SQLException;
}
