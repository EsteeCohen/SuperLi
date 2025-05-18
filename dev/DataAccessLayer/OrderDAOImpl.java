package DataAccessLayer;


import DataAccessLayer.interfacesDAO.OrderDAO;
import DomainLayer.Supplier.*;
import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class OrderDAOImpl implements OrderDAO {
    private final Connection connection;

    public OrderDAOImpl() throws SQLException {
        this.connection = DataAccessLayer.DatabaseConnection.getConnection();
    }

    @Override
    public void create(Order order) {
        String sql = "INSERT INTO Orders (order_id, supplier_id, order_date, supply_date, contact_name, contact_phone, agreement_id, status, total_price) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, order.getOrderId());
            stmt.setString(2, order.getSupplierId());
            stmt.setDate(3, Date.valueOf(order.getDate()));
            stmt.setDate(4, Date.valueOf(order.getSupplyDate()));
            stmt.setString(5, order.getContactPerson().getContactName());
            stmt.setString(6, order.getContactPerson().getPhoneNumber());
            stmt.setInt(7, order.getAgreement().getAgreementId());
            stmt.setString(8, order.getStatus().toString());
            stmt.setDouble(9, order.getTotalPrice());
            stmt.executeUpdate();
            insertOrderItems(order.getOrderId(), order.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertOrderItems(int orderId, Map<String, Integer> items) throws SQLException {
        String sql = "INSERT INTO OrderItems (order_id, catalog_number, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String, Integer> entry : items.entrySet()) {
                stmt.setInt(1, orderId);
                stmt.setString(2, entry.getKey());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public Order read(int orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return buildOrderFromResultSet(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private Order buildOrderFromResultSet(ResultSet rs) throws SQLException {
        int orderId = rs.getInt("order_id");
        String supplierId = rs.getString("supplier_id");
        LocalDate orderDate = rs.getDate("order_date").toLocalDate();
        LocalDate supplyDate = rs.getDate("supply_date").toLocalDate();
        String contactName = rs.getString("contact_name");
        String contactPhone = rs.getString("contact_phone");
        int agreementId = rs.getInt("agreement_id");
        String status = rs.getString("status");
        double totalPrice = rs.getDouble("total_price");

        // שליפת אובייקטים נוספים
        Agreement agreement = new Agreement("", agreementId, null, null, null, null); // יש להרחיב כאן לשליפה מלאה
        ContactPerson contact = new ContactPerson(contactName, contactPhone);

        Map<String, Integer> items = getOrderItems(orderId);

        // ייבוא ENUM
        DomainLayer.Supplier.Enums.STATUS enumStatus = DomainLayer.Supplier.Enums.STATUS.valueOf(status);

        return new Order(orderId, supplierId, orderDate, contact, agreement, supplyDate, items, enumStatus, totalPrice);
    }

    private Map<String, Integer> getOrderItems(int orderId) throws SQLException {
        Map<String, Integer> items = new HashMap<>();
        String sql = "SELECT catalog_number, quantity FROM OrderItems WHERE order_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                items.put(rs.getString("catalog_number"), rs.getInt("quantity"));
            }
        }
        return items;
    }

    @Override
    public List<Order> readAll() {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                orders.add(buildOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public List<Order> readAllBySupplier(String supplierId) {
        List<Order> orders = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                orders.add(buildOrderFromResultSet(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return orders;
    }

    @Override
    public void update(Order order) {
        String sql = "UPDATE Orders SET supplier_id=?, order_date=?, supply_date=?, contact_name=?, contact_phone=?, agreement_id=?, status=?, total_price=? WHERE order_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, order.getSupplierId());
            stmt.setDate(2, Date.valueOf(order.getDate()));
            stmt.setDate(3, Date.valueOf(order.getSupplyDate()));
            stmt.setString(4, order.getContactPerson().getContactName());
            stmt.setString(5, order.getContactPerson().getPhoneNumber());
            stmt.setInt(6, order.getAgreement().getAgreementId());
            stmt.setString(7, order.getStatus().toString());
            stmt.setDouble(8, order.getTotalPrice());
            stmt.setInt(9, order.getOrderId());
            stmt.executeUpdate();

            // נעדכן את הפריטים: קודם נמחק את הקיימים ואז נכניס מחדש (פשוט ונקי)
            deleteOrderItems(order.getOrderId());
            insertOrderItems(order.getOrderId(), order.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void deleteOrderItems(int orderId) throws SQLException {
        String sql = "DELETE FROM OrderItems WHERE order_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            stmt.executeUpdate();
        }
    }

    @Override
    public void delete(int orderId) {
        try {
            deleteOrderItems(orderId);
            String sql = "DELETE FROM Orders WHERE order_id=?";
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                stmt.setInt(1, orderId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBySupplier(String supplierId) {
        List<Order> orders = readAllBySupplier(supplierId);
        for (Order order : orders) {
            delete(order.getOrderId());
        }
    }
}
