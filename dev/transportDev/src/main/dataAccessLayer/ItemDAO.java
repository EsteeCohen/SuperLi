package src.main.dataAccessLayer;

import src.main.dtos.ItemDTO;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ItemDAO {

    private final String DBPath = TransportDBConstants.DB_PATH;
    private final String itemTableName = TransportDBConstants.ITEM_TABLE;

    public ItemDTO getItemById(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + itemTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        String name = rs.getString("name");
                        double weight = rs.getDouble("weight");
                        int quantity = rs.getInt("quantity");
                        String description = rs.getString("description");

                        return new ItemDTO(id, name, weight, quantity, description);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<ItemDTO> getAllItems() {
        List<ItemDTO> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + itemTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    String name = rs.getString("name");
                    double weight = rs.getDouble("weight");
                    int quantity = rs.getInt("quantity");
                    String description = rs.getString("description");

                    items.add(new ItemDTO(id, name, weight, quantity, description));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void insertItem(ItemDTO item) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "INSERT INTO " + itemTableName + 
                          " (id, name, weight, quantity, description) VALUES (?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, item.getId());
                statement.setString(2, item.getName());
                statement.setDouble(3, item.getWeight());
                statement.setInt(4, item.getQuantity());
                statement.setString(5, item.getDescription());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateItem(ItemDTO item) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "UPDATE " + itemTableName + 
                          " SET name = ?, weight = ?, quantity = ?, description = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, item.getName());
                statement.setDouble(2, item.getWeight());
                statement.setInt(3, item.getQuantity());
                statement.setString(4, item.getDescription());
                statement.setInt(5, item.getId());
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteItem(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "DELETE FROM " + itemTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<ItemDTO> getItemsByName(String name) {
        List<ItemDTO> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + itemTableName + " WHERE name LIKE ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, "%" + name + "%");
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String itemName = rs.getString("name");
                        double weight = rs.getDouble("weight");
                        int quantity = rs.getInt("quantity");
                        String description = rs.getString("description");

                        items.add(new ItemDTO(id, itemName, weight, quantity, description));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public List<ItemDTO> getItemsByWeightRange(double minWeight, double maxWeight) {
        List<ItemDTO> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + itemTableName + " WHERE weight BETWEEN ? AND ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setDouble(1, minWeight);
                statement.setDouble(2, maxWeight);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String name = rs.getString("name");
                        double weight = rs.getDouble("weight");
                        int quantity = rs.getInt("quantity");
                        String description = rs.getString("description");

                        items.add(new ItemDTO(id, name, weight, quantity, description));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public void updateItemQuantity(int id, int newQuantity) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "UPDATE " + itemTableName + " SET quantity = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, newQuantity);
                statement.setInt(2, id);
                statement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
} 