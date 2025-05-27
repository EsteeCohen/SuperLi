package DataAccessLayer;

import DataAccessLayer.DTO.InventoryProductDTO;
import DataAccessLayer.interfacesDAO.InventoryProductDAO;
import DomainLayer.Inventory.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.sql.*;
import java.util.Arrays;
import java.util.List;
//product: name, category, [subCategories], manufacturer, shelfLocation, storageLocation, minQuantity, sellPrice, discount
//discount: start, end, percentage #nullable
public class InventoryProductDAOImpl implements InventoryProductDAO {
    final String TABLE_NAME="inventoryProducts";
    public InventoryProductDAOImpl() throws SQLException {
    }
    @Override
    public void create(String category, Product product)
    {
        String sql = "INSERT INTO "+ TABLE_NAME+" (product_name, category, sub_categories, manufacturer ,shelf_location, storage_location, min_quantity, sell_price, discount_start, discount_end, discount_percentage) VALUES (?, ?, ?, ?, ?, ?,?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, product.getProductName());
            stmt.setString(2, category);
            stmt.setString(3, product.getSubCategories().toString());
            stmt.setString(4, product.getManufacturer());
            stmt.setString(5, product.getStoreLocation());
            stmt.setString(6, product.getStorageLocation());
            stmt.setInt(7, product.getMinQuantity());
            stmt.setDouble(8, product.getSellPrice());
            if(product.getDiscountStart()!=null)
                stmt.setDate(9, Date.valueOf(product.getDiscountStart()));
            else stmt.setNull(9, java.sql.Types.DATE);
            if(product.getDiscountEnd()!=null)
                stmt.setDate(10, Date.valueOf( product.getDiscountEnd()));
            else stmt.setNull(10, java.sql.Types.DATE);
            if(product.getDisountPercentage()!=null)
            stmt.setDouble(11, product.getDisountPercentage());
            else stmt.setNull(11, Types.DOUBLE);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public InventoryProductDTO read(String ProductName)
    {
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE product_name=?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setString(1, ProductName);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new InventoryProductDTO(
                        rs.getString("product_name"),
                        rs.getString("category"),
                        Arrays.stream(
                                        rs.getString("sub_categories").substring(1, rs.getString("sub_categories").length() - 1).split(","))
                                .map(String::trim)
                                .collect(Collectors.toList()),
                        rs.getString("manufacturer"),
                        rs.getString("shelf_location"),
                        rs.getString("storage_location"),
                        rs.getInt("min_quantity"),
                        rs.getDouble("sell_price"),
                        rs.getDate("discount_start")==null? null:rs.getDate("discount_start").toLocalDate(),
                        rs.getDate("discount_end")==null? null:rs.getDate("discount_end").toLocalDate(),
                        rs.getDouble("discount_percentage")
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<InventoryProductDTO> readAll()
    {
        List<InventoryProductDTO> products = new ArrayList<>();
        String sql = "SELECT * FROM "+TABLE_NAME;
        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                products.add(new InventoryProductDTO(
                        rs.getString("product_name"),
                        rs.getString("category"),
                        Arrays.stream(
                                        rs.getString("sub_categories").substring(1, rs.getString("sub_categories").length() - 1).split(","))
                                .map(String::trim)
                                .collect(Collectors.toList()),
                        rs.getString("manufacturer"),
                        rs.getString("shelf_location"),
                        rs.getString("storage_location"),
                        rs.getInt("min_quantity"),
                        rs.getDouble("sell_price"),
                        rs.getDate("discount_start")==null? null:rs.getDate("discount_start").toLocalDate(),
                        rs.getDate("discount_end")==null? null:rs.getDate("discount_end").toLocalDate(),
                        rs.getDouble("discount_percentage")
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
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setInt(1, product.getMinQuantity());
            if(product.getDiscountStart()!=null)
                stmt.setDate(2, Date.valueOf(product.getDiscountStart()));
            else stmt.setNull(2, java.sql.Types.DATE);
            if(product.getDiscountEnd()!=null)
                stmt.setDate(3, Date.valueOf( product.getDiscountEnd()));
            else stmt.setNull(3, java.sql.Types.DATE);
            if(product.getDisountPercentage()!=null)
                stmt.setDouble(4, product.getDisountPercentage());
            else stmt.setNull(4, Types.DOUBLE);
            stmt.executeUpdate();
            stmt.setString(5, product.getProductName());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void setDiscountForCategory(String category, LocalDate start, LocalDate end, double percentage)
    {
        String sql = "UPDATE "+TABLE_NAME+" SET discount_start=?, discount_end=?, discount_percentage=? WHERE category=?";
        try (PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            stmt.setDate(1, Date.valueOf(start));
            stmt.setDate(2,  Date.valueOf(end));
            stmt.setDouble(3, percentage);
            stmt.setString(4, category);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
