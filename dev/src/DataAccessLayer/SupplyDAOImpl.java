package DataAccessLayer;
import DataAccessLayer.DTO.SupplyDTO;
import DataAccessLayer.interfacesDAO.SupplyDAO;
import DomainLayer.Inventory.Product;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


// supply: productName, supplyId, shelfQuantity, storageQuantity, expirationDate, costPrice
public class SupplyDAOImpl implements SupplyDAO {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String TABLE_NAME="supplies";
    public SupplyDAOImpl() throws SQLException {
    }
    @Override
    public void create(String productName, Product.Supply supply)
    {
        String sql = "INSERT INTO "+ TABLE_NAME+" (product_name, supply_id, shelf_quantity, storage_quantity,expiration_date, cost) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, productName);
            stmt.setInt(2, supply.getSupplyID());
            stmt.setInt(3, supply.getShelfQuantity());
            stmt.setInt(4, supply.getStorageQuantity());
            stmt.setString(5, supply.getExpirationDate().toString());
            stmt.setDouble(6, supply.getCost());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public SupplyDTO read(String productName, int supplyId)
    {
        String sql = "SELECT * FROM " +TABLE_NAME+" WHERE product_name=? AND supply_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, productName);
            stmt.setInt(2, supplyId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return new SupplyDTO(
                        rs.getInt("supply_id"),
                        rs.getInt("shelf_quantity"),
                        rs.getInt("storage_quantity"),
                        LocalDate.parse( rs.getString("expiration_date"),FORMATTER),
                        rs.getDouble(rs.getString("cost"))
                );
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    @Override
    public List<SupplyDTO> readAll(String productName)
    {
        List<SupplyDTO> supplies = new ArrayList<>();
        String sql = "SELECT * FROM "+TABLE_NAME+" WHERE product_name=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, productName);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                supplies.add(new SupplyDTO(
                        rs.getInt("supply_id"),
                        rs.getInt("shelf_quantity"),
                        rs.getInt("storage_quantity"),
                        LocalDate.parse( rs.getString("expiration_date"),FORMATTER),
                        rs.getDouble("cost")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return supplies;
    }
    @Override
    public void update(String productName, Product.Supply supply)
    {
        String sql = "UPDATE "+TABLE_NAME+" SET shelf_quantity=?, storage_quantity=? WHERE product_name=? AND supply_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setInt(1, supply.getShelfQuantity());
            stmt.setInt(2, supply.getStorageQuantity());
            stmt.setString(3, productName);
            stmt.setInt(4, supply.getSupplyID());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void delete(String productName, int supplyId)
    {
        String sql = "DELETE FROM "+TABLE_NAME+" WHERE product_name=? AND supply_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, productName);
            stmt.setInt(2, supplyId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
