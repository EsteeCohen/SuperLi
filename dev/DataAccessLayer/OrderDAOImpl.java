package DataAccessLayer;

import DataAccessLayer.DTO.OrderDTO;
import DataAccessLayer.interfacesDAO.OrderDAO;

import java.sql.*;
import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class OrderDAOImpl implements OrderDAO {
    private final Connection connection;

    public OrderDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(OrderDTO dto) {
        String sql = "INSERT INTO Orders " +
                "(order_id, supplier_id, order_date, supply_date, contact_name, contact_phone, agreement_id, status, total_price) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, dto.getOrderId());
            stmt.setString(2, dto.getSupplierId());
            stmt.setDate(3, Date.valueOf(dto.getOrderDate()));
            stmt.setDate(4, Date.valueOf(dto.getSupplyDate()));
            stmt.setString(5, dto.getContactName());
            stmt.setString(6, dto.getContactPhone());
            stmt.setInt(7, dto.getAgreementId());
            stmt.setString(8, dto.getStatus());
            stmt.setDouble(9, dto.getTotalPrice());
            stmt.executeUpdate();
            insertOrderItems(dto.getOrderId(), dto.getItems());
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void insertOrderItems(int orderId, Map<String,Integer> items) throws SQLException {
        String sql = "INSERT INTO OrderItems (order_id, catalog_number, quantity) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            for (Map.Entry<String,Integer> entry : items.entrySet()) {
                stmt.setInt(1, orderId);
                stmt.setString(2, entry.getKey());
                stmt.setInt(3, entry.getValue());
                stmt.addBatch();
            }
            stmt.executeBatch();
        }
    }

    @Override
    public OrderDTO read(int orderId) {
        String sql = "SELECT * FROM Orders WHERE order_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, orderId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return mapResultSetToDTO(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private OrderDTO mapResultSetToDTO(ResultSet rs) throws SQLException {
        int id = rs.getInt("order_id");
        String supplierId = rs.getString("supplier_id");
        LocalDate orderDate = rs.getDate("order_date").toLocalDate();
        LocalDate supplyDate = rs.getDate("supply_date").toLocalDate();
        String contactName = rs.getString("contact_name");
        String contactPhone = rs.getString("contact_phone");
        int agreementId = rs.getInt("agreement_id");
        String status = rs.getString("status");
        double totalPrice = rs.getDouble("total_price");
        Boolean isPeriodic = rs.getBoolean("is_periodic");
        Map<String,Integer> items = getOrderItems(id);

        return new OrderDTO(id,
                supplierId,
                orderDate,
                supplyDate,
                contactName,
                contactPhone,
                agreementId,
                status,
                totalPrice,
                items,
                isPeriodic);
    }

    private Map<String,Integer> getOrderItems(int orderId) throws SQLException {
        Map<String,Integer> items = new HashMap<>();
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
    public List<OrderDTO> readAll() {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders";
        try (Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<OrderDTO> readAllBySupplier(String supplierId) {
        List<OrderDTO> list = new ArrayList<>();
        String sql = "SELECT * FROM Orders WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(mapResultSetToDTO(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public void update(OrderDTO dto) {
        String sql = "UPDATE Orders SET supplier_id=?, order_date=?, supply_date=?, " +
                "contact_name=?, contact_phone=?, agreement_id=?, status=?, total_price=? " +
                "WHERE order_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dto.getSupplierId());
            stmt.setDate(2, Date.valueOf(dto.getOrderDate()));
            stmt.setDate(3, Date.valueOf(dto.getSupplyDate()));
            stmt.setString(4, dto.getContactName());
            stmt.setString(5, dto.getContactPhone());
            stmt.setInt(6, dto.getAgreementId());
            stmt.setString(7, dto.getStatus());
            stmt.setDouble(8, dto.getTotalPrice());
            stmt.setInt(9, dto.getOrderId());
            stmt.executeUpdate();
            // clean and re-insert items
            deleteOrderItems(dto.getOrderId());
            insertOrderItems(dto.getOrderId(), dto.getItems());
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
            try (PreparedStatement stmt = connection.prepareStatement(
                    "DELETE FROM Orders WHERE order_id=?")) {
                stmt.setInt(1, orderId);
                stmt.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteBySupplier(String supplierId) {
        for (OrderDTO dto : readAllBySupplier(supplierId)) {
            delete(dto.getOrderId());
        }
    }

    @Override
    public List<OrderDTO> readAllPeriodic(){
        //TODO: Implement this method
        return null;
    }

    public List<OrderDTO> readAllNotPeriodic(){
        //TODO: Implement this method
        return null;
    }
}
