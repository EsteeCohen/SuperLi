package transportDev.src.main.repositories.impl;

import transportDev.src.main.repositories.OrderRepository;
import transportDev.src.main.dao.OrderDAO;
import transportDev.src.main.dao.SiteDAO;
import transportDev.src.main.entities.Order;
import transportDev.src.main.entities.Item;
import transportDev.src.main.entities.Site;
import transportDev.src.main.enums.OrderStatus;
import transportDev.src.main.enums.OrderType;
import java.time.LocalDate;
import java.util.List;

public class OrderRepositoryImpl implements OrderRepository {
    private OrderDAO orderDAO;
    private SiteDAO siteDAO;
    
    public OrderRepositoryImpl(OrderDAO orderDAO, SiteDAO siteDAO) {
        this.orderDAO = orderDAO;
        this.siteDAO = siteDAO;
    }
    
    @Override
    public Order createOrder(LocalDate date, String siteId, List<Item> items, OrderType type) {
        Site site = siteDAO.read(siteId);
        if (site == null) return null;
        
        Order order = new Order(date, site, items, type);
        orderDAO.create(order);
        return order;
    }
    
    @Override
    public Order findById(int id) {
        return orderDAO.read(id);
    }
    
    @Override
    public List<Order> findAll() {
        return orderDAO.getAll();
    }
    
    @Override
    public List<Order> findByDate(LocalDate date) {
        return orderDAO.getByDate(date);
    }
    
    @Override
    public List<Order> findByStatus(OrderStatus status) {
        return orderDAO.getByStatus(status);
    }
    
    @Override
    public List<Order> findByType(OrderType type) {
        return orderDAO.getByType(type);
    }
    
    @Override
    public List<Order> findUnassignedDeliveryOrders() {
        return orderDAO.getAll().stream()
            .filter(order -> order.getType() == OrderType.DELIVERY)
            .filter(order -> order.getTransport() == null)
            .filter(order -> order.getStatus() != OrderStatus.CANCELLED)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public List<Order> findOrdersInTransport(int transportId) {
        return orderDAO.getAll().stream()
            .filter(order -> order.getTransport() != null && order.getTransport().getId() == transportId)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public boolean updateOrderStatus(int id, OrderStatus status) {
        Order order = orderDAO.read(id);
        if (order == null) return false;
        
        order.setStatus(status);
        orderDAO.update(order);
        return true;
    }
    
    @Override
    public boolean assignToTransport(int orderId, int transportId) {
        Order order = orderDAO.read(orderId);
        if (order == null) return false;
        
        // Transport assignment logic would go here
        // This is simplified for the basic implementation
        return true;
    }
    
    @Override
    public boolean cancelOrder(int id) {
        Order order = orderDAO.read(id);
        if (order == null || !order.canBeCancelled()) return false;
        
        order.setStatus(OrderStatus.CANCELLED);
        orderDAO.update(order);
        return true;
    }
} 