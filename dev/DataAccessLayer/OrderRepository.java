package DataAccessLayer;

import DataAccessLayer.*;
import DataAccessLayer.interfacesDAO.OrderDAO;
import DomainLayer.Supplier.Order;

import java.sql.SQLException;
import java.util.List;

public class OrderRepository {
    private final OrderDAO orderDAO;

    public OrderRepository() throws SQLException {
        this.orderDAO = new OrderDAOImpl();
    }

    public void addOrder(Order order) {
        orderDAO.create(order);
    }

    public Order getOrderById(int orderId) {
        return orderDAO.read(orderId);
    }

    public List<Order> getAllOrders() {
        return orderDAO.readAll();
    }

    public List<Order> getOrdersBySupplier(String supplierId) {
        return orderDAO.readAllBySupplier(supplierId);
    }

    public void updateOrder(Order order) {
        orderDAO.update(order);
    }

    public void removeOrder(int orderId) {
        orderDAO.delete(orderId);
    }

    public void removeOrdersBySupplier(String supplierId) {
        orderDAO.deleteBySupplier(supplierId);
    }
}
