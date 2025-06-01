package src.main.dao.impl;

import src.main.dao.OrderDAO;
import src.main.entities.Order;
import src.main.enums.OrderStatus;
import src.main.enums.OrderType;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderDAOImpl implements OrderDAO {
    private Map<Integer, Order> orders = new HashMap<>();
    
    @Override
    public void create(Order order) {
        orders.put(order.getId(), order);
    }
    
    @Override
    public Order read(int id) {
        return orders.get(id);
    }
    
    @Override
    public void update(Order order) {
        orders.put(order.getId(), order);
    }
    
    @Override
    public void delete(int id) {
        orders.remove(id);
    }
    
    @Override
    public List<Order> getAll() {
        return new ArrayList<>(orders.values());
    }
    
    @Override
    public List<Order> getByDate(LocalDate date) {
        return orders.values().stream()
            .filter(order -> order.getDate().equals(date))
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Order> getByStatus(OrderStatus status) {
        return orders.values().stream()
            .filter(order -> order.getStatus() == status)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Order> getByType(OrderType type) {
        return orders.values().stream()
            .filter(order -> order.getType() == type)
            .collect(java.util.stream.Collectors.toList());
    }
} 