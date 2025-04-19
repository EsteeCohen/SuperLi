package src.main.controllers;

import src.main.entities.Item;
import src.main.entities.Order;
import src.main.services.OrderService; // Import the OrderService class
import src.main.enums.OrderStatus; // Import the OrderStatus enum

import java.time.LocalDate;
import java.util.List;

public class OrderController {
    private OrderService orderService;

    // Constructor
    public OrderController(OrderService orderService){
        this.orderService = orderService;
    }

    // Methods
    public Order createOrder(LocalDate date, String siteId, List<Item> items){
        return orderService.createOrder(date, siteId, items); 
    }

    public List<Order> getAllOrders(){
        return orderService.getAllOrders();
    }
    public Order getOrderById(int id){
        return orderService.getOrderById(id);
    }
    public List<Order> getOrdersByDate(LocalDate date){
        return orderService.getOrdersByDate(date);
    }
    public List<Order> getOrdersByStatus(OrderStatus status){
        return orderService.getOrdersByStatus(status);
    }
    public boolean updateOrderStatus(int id, OrderStatus newStatus){
        return orderService.updateOrderStatus(id, newStatus);
    }
    public boolean assignTransportToOrder(int orderId, int transportId){
        return orderService.assignTransportToOrder(orderId, transportId);
    }
    public boolean removeItems (int orderId, int transportId, List<Item> itemsToRemove){
        return orderService.removeItems (orderId,transportId,itemsToRemove);
    }
    public boolean cancelOrder(int id){
        return orderService.cancelOrder(id);
    }

}
