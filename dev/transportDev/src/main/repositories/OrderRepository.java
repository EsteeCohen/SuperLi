package transportDev.src.main.repositories;

import java.time.LocalDate;
import java.util.List;
import transportDev.src.main.entities.Item;
import transportDev.src.main.entities.Order;
import transportDev.src.main.enums.OrderStatus;
import transportDev.src.main.enums.OrderType;

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