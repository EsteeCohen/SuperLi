package DataAccessLayer;

import DataAccessLayer.interfacesDAO.InventoryProductDAO;
import DomainLayer.Inventory.Product;
import DomainLayer.Supplier.Enums.Units;

import java.util.ArrayList;
import java.util.stream.Collectors;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
//product: name, category, [subCategories], manufacturer, shelfLocation, storageLocation, minQuantity, sellPrice, discount
//discount: start, end, percentage #nullable
public class InventoryProductDAOImpl implements InventoryProductDAO {
    final String TABLE_NAME="inventory_products";
    private final Connection connection;
    public InventoryProductDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }
    @Override
    public void create(String category, Product product)
    {
        String sql = "INSERT INTO "+ TABLE_NAME+" (product_name, category, sub_categories, manufacturer ,shelf_location, storage_location, min_quantity, sell_price, discount_start, discount_end, discount_percentage) VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, category);
            stmt.setString(3, product.getSubCategories().toString());
            stmt.setString(4, product.getManufacturer());
            stmt.setString(5, product.getStoreLocation());
            stmt.setString(6, product.getStorageLocation());
            stmt.setInt(7, product.getMinQuantity());
            stmt.setDouble(8, product.getSellPrice());
            stmt.setDate(9, Date.valueOf(product.getDiscountStart()));
            stmt.setDate(10, Date.valueOf( product.getDiscountEnd()));
            stmt.setDouble(11, product.getDisountPercentage());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public Product read(String ProductName)
    {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE product_name=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, ProductName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new DomainLayer.Inventory.Product(
                        rs.getString("product_name"),
                        Arrays.stream(
                                        rs.getString("sub_categories").substring(1, rs.getString("sub_categories").length() - 1).split(","))
                                .map(String::trim)
                                .collect(Collectors.toList()),
                        rs.getString("manufacturer"),
                        rs.getDouble("sell_price"),
                        rs.getString("shelf_location"),
                        rs.getString("storage_location")

                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<Product> readAll()
    {
        List<DomainLayer.Inventory.Product> products = new ArrayList<>();
        String sql = "SELECT * FROM "+TABLE_NAME;
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                products.add(new DomainLayer.Inventory.Product(
                        rs.getString("product_name"),
                        Arrays.stream(
                                        rs.getString("sub_categories").substring(1, rs.getString("sub_categories").length() - 1).split(","))
                                .map(String::trim)
                                .collect(Collectors.toList()),
                        rs.getString("manufacturer"),
                        rs.getDouble("sell_price"),
                        rs.getString("shelf_location"),
                        rs.getString("storage_location")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return products;
    }
    @Override
    public void update(Product product)
    {
        String sql = "UPDATE "+TABLE_NAME+" SET min_quantity=?, discount_start=?, discount_end=?, discount_percentage=? WHERE product_name=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, product.getMinQuantity());
            stmt.setDate(2, Date.valueOf(product.getDiscountStart()));
            stmt.setDate(3,  Date.valueOf(product.getDiscountEnd()));
            stmt.setDouble(4, product.getDisountPercentage());
            stmt.setString(5, product.getProductName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
