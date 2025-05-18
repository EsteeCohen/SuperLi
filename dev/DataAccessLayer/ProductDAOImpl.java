package DataAccessLayer;
// DAO/ProductDAOImpl.java

import DataAccessLayer.interfacesDAO.ProductDAO;
import DomainLayer.Supplier.Enums.Units;
import DomainLayer.Supplier.Product;
import DataAccessLayer.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ProductDAOImpl implements ProductDAO {
    private final Connection connection;

    public ProductDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(Product product) {
        String sql = "INSERT INTO Products (catalog_number, supplier_id, product_name, quantity_per_package, price_per_package, units) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getCatalogNumber());
            stmt.setString(2, product.getSupplierId());
            stmt.setString(3, product.getProductName());
            stmt.setInt(4, product.getQuantityPerPackage());
            stmt.setDouble(5, product.getPrice());
            stmt.setString(6, product.getUnits().toString());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Product read(String catalogNumber) {
        String sql = "SELECT * FROM Products WHERE catalog_number=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, catalogNumber);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new Product(
                        rs.getString("product_name"),
                        rs.getString("supplier_id"),
                        rs.getString("catalog_number"),
                        rs.getInt("quantity_per_package"),
                        rs.getDouble("price_per_package"),
                        // suppose Units is an enum
                        Units.valueOf(rs.getString("units"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Product> readAll() {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("product_name"),
                        rs.getString("supplier_id"),
                        rs.getString("catalog_number"),
                        rs.getInt("quantity_per_package"),
                        rs.getDouble("price_per_package"),
                        Units.valueOf(rs.getString("units"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public List<Product> readAllBySupplier(String supplierId) {
        List<Product> products = new ArrayList<>();
        String sql = "SELECT * FROM Products WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                products.add(new Product(
                        rs.getString("product_name"),
                        rs.getString("supplier_id"),
                        rs.getString("catalog_number"),
                        rs.getInt("quantity_per_package"),
                        rs.getDouble("price_per_package"),
                        Units.valueOf(rs.getString("units"))
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }

    @Override
    public void update(Product product) {
        String sql = "UPDATE Products SET product_name=?, supplier_id=?, quantity_per_package=?, price_per_package=?, units=? WHERE catalog_number=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, product.getSupplierId());
            stmt.setInt(3, product.getQuantityPerPackage());
            stmt.setDouble(4, product.getPrice());
            stmt.setString(5, product.getUnits().toString());
            stmt.setString(6, product.getCatalogNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(String catalogNumber) {
        String sql = "DELETE FROM Products WHERE catalog_number=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, catalogNumber);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteBySupplier(String supplierId) {
        String sql = "DELETE FROM products WHERE supplier_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
