package DomainLayer.Supplier.Repositories;

import DataAccessLayer.DTO.ProductDTO;
import DataAccessLayer.ProductDAOImpl;
import DataAccessLayer.interfacesDAO.ProductDAO;
import DomainLayer.Supplier.Product;

import java.util.ArrayList;
import java.util.List;
import java.sql.SQLException;

public class ProductRepositoryImpl implements ProductRepository {
    private final ProductDAO productDAO = new ProductDAOImpl();

    @Override
    public void add(Product product) throws SQLException {
        ProductDTO productDTO = new ProductDTO(product);
        productDAO.create(productDTO);
    }

    @Override
    public Product getByCatalog(String catalogNumber) throws SQLException {
        ProductDTO productDTO = productDAO.readByCatalogNumber(catalogNumber);
        return productDTO.toDomain();
    }

    @Override
    public List<Product> getAll() throws SQLException {
        List<Product> products = new ArrayList<>();
        for(ProductDTO productDTO : productDAO.readAll()){
            products.add(productDTO.toDomain());
        }
        return products;
    }

    @Override
    public void remove(Product product) throws SQLException {
        productDAO.delete(product.getCatalogNumber());
    }


}