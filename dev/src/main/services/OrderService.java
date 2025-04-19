package src.main.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import BussinessLayer.Transport;
import src.main.entities.Item;
import src.main.entities.Order;
import src.main.entities.Site;
import src.main.enums.OrderStatus;
import src.main.services.SiteService;

public class OrderService {
    private List<Order> orders;
    private SiteService siteService;
    private TransportService transportService;

    // Constructor
    public OrderService(SiteService siteService, TransportService transportService){
        this.siteService = siteService;
        this.transportService = transportService;
        this.orders = new ArrayList<>();
    }

    // Methods
    public Order createOrder(LocalDate date, String siteId, List<Item> items){
        Site site = siteService.getSiteById(siteId);
        if (site == null)
            return null;

        List<Item> itemsInOrder = new ArrayList<>();
        for (Item item : items) {
            if (item.getQuantity() in site && ((itemQuantity - item.getQuantity()) > 0)){ //אפשר לעשות MAP לכל אתר עם המוצרים שיש בו וכמות
                itemsInOrder.add(item);
                site.updateItemQuantity(---);
            }
        }
        Order order = new Order(date, site, itemsInOrder);
        orders.add(order);
        return order;
    }

    public List<Order> getAllOrders(){
        return new ArrayList<>(orders);
    }

    public Order getOrderById(int id){
        for(Order order : orders){
            if(order.getId() == id){
                return order;
            }
        }
        return null;
    }

    public List<Order> getOrdersByDate(LocalDate date){
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getDate().equals(date)) {
                result.add(order);
            }
        }
        return result;
    }

    public List<Order> getOrdersByStatus(OrderStatus status){
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }
        return result;
    }
    public boolean updateOrderStatus(int id, OrderStatus newStatus){
        Order order = getOrderById(id);
        if (order == null) return false;

        order.setStatus(newStatus);
        return true;
    }

    public boolean assignTransportToOrder(int orderId, int transportId){
        Order order = getOrderById(orderId);
        Transport transport = transportService.getTransportById(transportId);

        if (order == null || transport == null) {
            return false;
        }

        double newWeight = order.OrderWeight() + transport.getCurrentWeight();
        if (newWeight > transport.getTruck().getMaxWeight()) {
            return false;//1/2/3יש חריגה במשקל צריך לפתןר אותה
        }
        order.setTransport(transport);
        transport.setCurrentWeight(newWeight);
        return true;
    }

    public boolean removeItems (int orderId, int transportId, List<Item> itemsToRemove){//וגם לעדכן בMAP של האתר את הפריטים
        Order order = getOrderById(orderId);
        Transport transport = transportService.getTransportById(transportId);

        if (order == null || transport == null || !order.getTransport().equals(transport)) {\
            return false;
        }

        List<Item> orderItems = order.getItems();
        double removedWeight = 0;

        for (Item itemToRemove : itemsToRemove) {
            if (orderItems.contains(itemToRemove)) {
                orderItems.remove(itemToRemove);
                removedWeight += itemToRemove.getWeight();
            }
        }
        order.setItems(orderItems);
        transport.setCurrentWeight(transport.getCurrentWeight() - removedWeight);
        return true;





    }

    public boolean cancelOrder(int id){
        Order order = getOrderById(id);
        if (order == null) return false;

        if(order.canBeCancelled()){
            if (order.getTransport()!=null){
                order.setTransport(null);
                transport.setCurrentWeight(transport.getCurrentWeight() - order.OrderWeight());
            }
            order.setStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }
}
