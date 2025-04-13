package ServiceLayer;

import BussinessLayer.Item;
import BussinessLayer.Order;
import BussinessLayer.Transport;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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

        order.setTransport(transport);
        return true;
    }
    public boolean cancelOrder(int id){
        Order order = getOrderById(id);
        if (order == null) return false;

        if(order.canBeCancelled()){
            order.setStatus(OrderStatus.CANCELLED);
            return true;
        }
        return false;
    }

}
