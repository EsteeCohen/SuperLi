package src.main.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import src.main.entities.*;
import src.main.enums.IncidentType;
import src.main.enums.OrderStatus;

public class OrderService {
    private List<Order> orders;
    private SiteService siteService;
    private TransportService transportService;
    private IncidentService incidentService;

    // Constructor
    public OrderService(SiteService siteService, TransportService transportService, IncidentService incidentService){
        this.siteService = siteService;
        this.transportService = transportService;
        this.incidentService = incidentService;
        this.orders = new ArrayList<>();
    }

    // Methods
    public Order createOrder(LocalDate date, String siteId, List<Item> items){
        Site site = siteService.getSiteById(siteId);
        if (site == null){
            throw new IllegalArgumentException("site not found");
        }

        Order order = new Order(date, site, items);
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

    public List<Order> getOrdersInTransport(int transportId){
        List<Order> result = new ArrayList<>();
        for (Order o : this.orders) {
            if (o.getTransport().getId() == transportId)
                result.add(o);
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
            incidentService.reportIncident(transportId, IncidentType.WEIGHT_OVERLOAD,"the weight overload when added this order");
            return false;//1/2/3יש חריגה במשקל צריך לפתןר אותה
        }
        order.setTransport(transport);
        transport.setCurrentWeight(newWeight);
        order.setStatus(OrderStatus.IN_PROGRESS);
        return true;
    }

    public boolean removeItems (int orderId, int transportId, List<Item> itemsToRemove){
        Order order = getOrderById(orderId);
        Transport transport = transportService.getTransportById(transportId);

        if (order == null || !order.getTransport().equals(transport)) {
            throw new IllegalArgumentException("can't remove items from this order");
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

    public boolean removeOrderFromTruck(int orderId) {
        Order order = getOrderById(orderId);
        if (order == null) return false;
        if (order.getTransport() != null) {
            order.getTransport().setCurrentWeight(order.getTransport().getCurrentWeight() - order.OrderWeight());
            order.setTransport(null);
            order.setStatus(OrderStatus.DONE);
            return true;
        }
        return false;
    }

    public boolean cancelOrder(int id){
        Order order = getOrderById(id);
        if(order.canBeCancelled()) {
            boolean result = removeOrderFromTruck(id);
            if (result) {
                order.setStatus(OrderStatus.CANCELLED);
                return true;
            }
        }
        return false;
    }
}

