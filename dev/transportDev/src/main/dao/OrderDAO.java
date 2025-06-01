package src.main.dao;

import src.main.entities.Order;
import src.main.enums.OrderStatus;
import src.main.enums.OrderType;
import java.time.LocalDate;
import java.util.List;

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