package transportDev.src.main.dataAccessLayer;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import transportDev.src.main.dtos.ItemDTO;
import transportDev.src.main.dtos.OrderDTO;
import transportDev.src.main.dtos.SiteDTO;

public class OrderDAO {

    private final String DBPath = TransportDBConstants.DB_PATH;
    private final String orderTableName = TransportDBConstants.ORDER_TABLE;
    private final String orderItemsTableName = TransportDBConstants.ORDER_ITEMS_TABLE;
    private final SiteDAO siteDAO;
    private final ItemDAO itemDAO;

    public OrderDAO() {
        this.siteDAO = new SiteDAO();
        this.itemDAO = new ItemDAO();
    }

    public OrderDTO getOrderById(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + orderTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                try (ResultSet rs = statement.executeQuery()) {
                    if (rs.next()) {
                        LocalDate orderDate = LocalDate.parse(rs.getString("order_date"));
                        String status = rs.getString("status");
                        String sourceSiteName = rs.getString("source_site_name");
                        String destinationSiteName = rs.getString("destination_site_name");
                        double totalWeight = rs.getDouble("total_weight");

                        SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                        SiteDTO destinationSite = siteDAO.getSiteByName(destinationSiteName);
                        List<ItemDTO> items = getItemsForOrder(id);

                        return new OrderDTO(id, orderDate, status, sourceSite, destinationSite, items, totalWeight);
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public List<OrderDTO> getAllOrders() {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + orderTableName;
            try (PreparedStatement statement = conn.prepareStatement(query);
                 ResultSet rs = statement.executeQuery()) {
                while (rs.next()) {
                    int id = rs.getInt("id");
                    LocalDate orderDate = LocalDate.parse(rs.getString("order_date"));
                    String status = rs.getString("status");
                    String sourceSiteName = rs.getString("source_site_name");
                    String destinationSiteName = rs.getString("destination_site_name");
                    double totalWeight = rs.getDouble("total_weight");

                    SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                    SiteDTO destinationSite = siteDAO.getSiteByName(destinationSiteName);
                    List<ItemDTO> items = getItemsForOrder(id);

                    orders.add(new OrderDTO(id, orderDate, status, sourceSite, destinationSite, items, totalWeight));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public void insertOrder(OrderDTO order) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            String query = "INSERT INTO " + orderTableName + 
                          " (id, order_date, status, source_site_name, destination_site_name, total_weight) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, order.getId());
                statement.setString(2, order.getOrderDate().toString());
                statement.setString(3, order.getStatus());
                statement.setString(4, order.getSourceSite().getName());
                statement.setString(5, order.getDestinationSite().getName());
                statement.setDouble(6, order.getTotalWeight());
                statement.executeUpdate();
            }

            // Insert items
            insertItemsForOrder(conn, order.getId(), order.getItems());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void updateOrder(OrderDTO order) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            String query = "UPDATE " + orderTableName + 
                          " SET order_date = ?, status = ?, source_site_name = ?, destination_site_name = ?, total_weight = ? WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, order.getOrderDate().toString());
                statement.setString(2, order.getStatus());
                statement.setString(3, order.getSourceSite().getName());
                statement.setString(4, order.getDestinationSite().getName());
                statement.setDouble(5, order.getTotalWeight());
                statement.setInt(6, order.getId());
                statement.executeUpdate();
            }

            // Update items
            deleteItemsForOrder(conn, order.getId());
            insertItemsForOrder(conn, order.getId(), order.getItems());
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void deleteOrder(int id) {
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            conn.setAutoCommit(false);
            
            // Delete items first
            deleteItemsForOrder(conn, id);
            
            String query = "DELETE FROM " + orderTableName + " WHERE id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, id);
                statement.executeUpdate();
            }
            
            conn.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<OrderDTO> getOrdersByStatus(String status) {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + orderTableName + " WHERE status = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, status);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        LocalDate orderDate = LocalDate.parse(rs.getString("order_date"));
                        String sourceSiteName = rs.getString("source_site_name");
                        String destinationSiteName = rs.getString("destination_site_name");
                        double totalWeight = rs.getDouble("total_weight");

                        SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                        SiteDTO destinationSite = siteDAO.getSiteByName(destinationSiteName);
                        List<ItemDTO> items = getItemsForOrder(id);

                        orders.add(new OrderDTO(id, orderDate, status, sourceSite, destinationSite, items, totalWeight));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    public List<OrderDTO> getOrdersByDate(LocalDate date) {
        List<OrderDTO> orders = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT * FROM " + orderTableName + " WHERE order_date = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setString(1, date.toString());
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int id = rs.getInt("id");
                        String status = rs.getString("status");
                        String sourceSiteName = rs.getString("source_site_name");
                        String destinationSiteName = rs.getString("destination_site_name");
                        double totalWeight = rs.getDouble("total_weight");

                        SiteDTO sourceSite = siteDAO.getSiteByName(sourceSiteName);
                        SiteDTO destinationSite = siteDAO.getSiteByName(destinationSiteName);
                        List<ItemDTO> items = getItemsForOrder(id);

                        orders.add(new OrderDTO(id, date, status, sourceSite, destinationSite, items, totalWeight));
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    private List<ItemDTO> getItemsForOrder(int orderId) {
        List<ItemDTO> items = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection("jdbc:sqlite:" + DBPath)) {
            String query = "SELECT item_id, quantity FROM " + orderItemsTableName + " WHERE order_id = ?";
            try (PreparedStatement statement = conn.prepareStatement(query)) {
                statement.setInt(1, orderId);
                try (ResultSet rs = statement.executeQuery()) {
                    while (rs.next()) {
                        int itemId = rs.getInt("item_id");
                        int quantity = rs.getInt("quantity");
                        ItemDTO item = itemDAO.getItemById(itemId);
                        if (item != null) {
                            // Update quantity to reflect the order quantity
                            item.setQuantity(quantity);
                            items.add(item);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    private void insertItemsForOrder(Connection conn, int orderId, List<ItemDTO> items) throws SQLException {
        String query = "INSERT INTO " + orderItemsTableName + " (order_id, item_id, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            for (ItemDTO item : items) {
                statement.setInt(1, orderId);
                statement.setInt(2, item.getId());
                statement.setInt(3, item.getQuantity());
                statement.executeUpdate();
            }
        }
    }

    private void deleteItemsForOrder(Connection conn, int orderId) throws SQLException {
        String query = "DELETE FROM " + orderItemsTableName + " WHERE order_id = ?";
        try (PreparedStatement statement = conn.prepareStatement(query)) {
            statement.setInt(1, orderId);
            statement.executeUpdate();
        }
    }
} // :)