package transportDev.src.main.dao;

import java.time.LocalDate;
import java.util.List;
import transportDev.src.main.entities.Order;
import transportDev.src.main.enums.OrderStatus;
import transportDev.src.main.enums.OrderType;

public interface OrderDAO {
    void create(Order order);
    Order read(int id);
    void update(Order order);
    void delete(int id);
    List<Order> getAll();
    List<Order> getByDate(LocalDate date);
    List<Order> getByStatus(OrderStatus status);
    List<Order> getByType(OrderType type);
} 