package DomainLayer.Supplier.Repositories;

import DataAccessLayer.*;
import DataAccessLayer.DTO.OrderDTO;
import DataAccessLayer.interfacesDAO.OrderDAO;
import DomainLayer.Supplier.Order;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    private final OrderDAO orderDAO;

    public OrderRepository() throws SQLException {
        this.orderDAO = new OrderDAOImpl();
    }

    public void addOrder(Order order, boolean isPeriodic) {
        orderDAO.create(new OrderDTO(order, isPeriodic));
    }

    public Order getOrderById(int orderId) {
        OrderDTO dto = orderDAO.read(orderId);
        return dto != null ? dto.toDomain() : null;
    }

    public List<Order> getAllOrders() {
        return orderDAO.readAll()
                .stream()
                .map(OrderDTO::toDomain)
                .toList();
    }

    public List<Order> getOrdersBySupplier(String supplierId) {
        return orderDAO.readAllBySupplier(supplierId).stream()
                .map(OrderDTO::toDomain)
                .toList();
    }

    public void updateOrder(Order order) {
        OrderDTO orderDTO = orderDAO.read(order.getOrderId());
        orderDTO.update(order);
        orderDAO.update(orderDTO);
    }

    public void removeOrder(int orderId) {
        orderDAO.delete(orderId);
    }

    public void removeOrdersBySupplier(String supplierId) {
        orderDAO.deleteBySupplier(supplierId);
    }

    public void updateOrderStatus(int orderId, String newStatus) {
        OrderDTO order = orderDAO.read(orderId);
        if (order != null) {
            order.setStatus(newStatus);
            orderDAO.update(order);
        }
    }

    public void addProductToOrder(int orderId, String catalogNumber, int quantity) {
        OrderDTO orderDTO = orderDAO.read(orderId);
        if (orderDTO != null) {
            orderDTO.addProduct(catalogNumber, quantity);
            orderDAO.update(orderDTO);
        }
    }



    public List<Order> getAllOrdersNotPeriodic() {
        return orderDAO.readAllNotPeriodic()
                .stream()
                .map(OrderDTO::toDomain)
                .toList();
    }

    public List<Order> getAllOrdersPeriodic() {
        return orderDAO.readAllPeriodic()
                .stream()
                .map(OrderDTO::toDomain)
                .toList();
    }

    public void updateDeliveryDate(int orderId, LocalDate supplyDate) {
        OrderDTO order = orderDAO.read(orderId);
        if (order != null) {
            order.setSupplyDate(supplyDate);
            orderDAO.update(order);
        }
    }
}
