package src.main.repositories;

import src.main.entities.Order;
import src.main.entities.Item;
import src.main.enums.OrderStatus;
import src.main.enums.OrderType;
import java.time.LocalDate;
import java.util.List;

public interface OrderRepository {
    Order createOrder(LocalDate date, String siteId, List<Item> items, OrderType type);
    Order findById(int id);
    List<Order> findAll();
    List<Order> findByDate(LocalDate date);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByType(OrderType type);
    List<Order> findUnassignedDeliveryOrders();
    List<Order> findOrdersInTransport(int transportId);
    boolean updateOrderStatus(int id, OrderStatus status);
    boolean assignToTransport(int orderId, int transportId);
    boolean cancelOrder(int id);
} 