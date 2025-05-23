package DataAccessLayer.interfacesDAO;


import DataAccessLayer.DTO.ProductDTO;
import java.util.List;

public interface ProductDAO {
    void create(ProductDTO product);
    ProductDTO read(String catalogNumber);
    List<ProductDTO> readAll();
    List<ProductDTO> readAllBySupplier(String supplierId);
    void update(ProductDTO product);
    void delete(String catalogNumber);

    void deleteBySupplier(String supplierId);

    ProductDTO readByCatalogNumber(String catalogNumber);
}

