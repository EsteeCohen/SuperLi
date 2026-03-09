package transportDev.src.main.services;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import employeeDev.src.serviceLayer.Interfaces.SiteInfoInterface;
import transportDev.src.main.entities.*;
import transportDev.src.main.enums.IncidentType;
import transportDev.src.main.enums.OrderStatus;
import transportDev.src.main.dataAccessLayer.OrderDAO;
import transportDev.src.main.mappers.OrderMapper;
import transportDev.src.main.dtos.OrderDTO;

public class OrderService {
    private OrderDAO orderDAO;
    private SiteInfoInterface siteService;
    private TransportService transportService;
    private IncidentService incidentService;

    // Constructor
    public OrderService(SiteInfoInterface siteService, TransportService transportService, IncidentService incidentService){
        this.orderDAO = new OrderDAO();
        this.siteService = siteService;
        this.transportService = transportService;
        this.incidentService = incidentService;
    }

    // Methods
    public Order createOrder(LocalDate date, String siteId, List<Item> items){
        Site site = siteService.getSiteByName(siteId);
        if (site == null){
            throw new IllegalArgumentException("site not found");
        }

        try {
            Order order = new Order(date, site, items);
            OrderDTO orderDTO = OrderMapper.toDTO(order);
            orderDAO.insertOrder(orderDTO);
            return order;
        } catch (Exception e) {
            System.err.println("Error creating order: " + e.getMessage());
            return null;
        }
    }

    public List<Order> getAllOrders(){
        try {
            List<OrderDTO> orderDTOs = orderDAO.getAllOrders();
            List<Order> orders = new ArrayList<>();
            for (OrderDTO dto : orderDTOs) {
                // Get the required objects for the mapper
                Site site = siteService.getSiteByName(dto.getSourceSite().getName());
                // For now, create empty items list
                List<Item> items = new ArrayList<>();
                
                if (site != null) {
                    Order order = OrderMapper.fromDTO(dto, site, items);
                    orders.add(order);
                }
            }
            return orders;
        } catch (Exception e) {
            System.err.println("Error retrieving orders: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    public Order getOrderById(int id){
        try {
            OrderDTO orderDTO = orderDAO.getOrderById(id);
            if (orderDTO != null) {
                // Get the required objects for the mapper
                Site site = siteService.getSiteByName(orderDTO.getSourceSite().getName());
                // For now, create empty items list
                List<Item> items = new ArrayList<>();
                
                if (site != null) {
                    return OrderMapper.fromDTO(orderDTO, site, items);
                }
            }
            return null;
        } catch (Exception e) {
            System.err.println("Error retrieving order by ID: " + e.getMessage());
            return null;
        }
    }

    public List<Order> getOrdersByDate(LocalDate date){
        List<Order> allOrders = getAllOrders();
        List<Order> result = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getDate().equals(date)) {
                result.add(order);
            }
        }
        return result;
    }

    public List<Order> getOrdersByStatus(OrderStatus status){
        List<Order> allOrders = getAllOrders();
        List<Order> result = new ArrayList<>();
        for (Order order : allOrders) {
            if (order.getStatus() == status) {
                result.add(order);
            }
        }
        return result;
    }

    public List<Order> getOrdersInTransport(int transportId){
        List<Order> allOrders = getAllOrders();
        List<Order> result = new ArrayList<>();
        for (Order o : allOrders) {
            if (o.getTransport() != null && o.getTransport().getId() == transportId)
                result.add(o);
        }
        return result;
    }
    
    public boolean updateOrderStatus(int id, OrderStatus newStatus){
        try {
            Order order = getOrderById(id);
            if (order == null) return false;

            order.setStatus(newStatus);
            OrderDTO orderDTO = OrderMapper.toDTO(order);
            orderDAO.updateOrder(orderDTO);
            return true;
        } catch (Exception e) {
            System.err.println("Error updating order status: " + e.getMessage());
            return false;
        }
    }

    public boolean assignTransportToOrder(int orderId, int transportId){
        try {
            Order order = getOrderById(orderId);
            Transport transport = transportService.getTransportById(transportId);

            if (order == null || transport == null) {
                return false;
            }

            double newWeight = order.OrderWeight() + transport.getCurrentWeight();
            if (newWeight > transport.getTruck().getMaxWeight()) {
                incidentService.reportIncident(transportId, IncidentType.WEIGHT_OVERLOAD,
                    "Weight overload when attempting to add order #" + orderId + 
                    ". Required: " + newWeight + "kg, Max capacity: " + transport.getTruck().getMaxWeight() + "kg");
                return false;
            }
            
            order.setTransport(transport);
            transport.setCurrentWeight(newWeight);
            order.setStatus(OrderStatus.IN_PROGRESS);
            
            // Update both order and transport in database
            OrderDTO orderDTO = OrderMapper.toDTO(order);
            orderDAO.updateOrder(orderDTO);
            transportService.updateTransportStatus(transportId, transport.getStatus());
            
            return true;
        } catch (Exception e) {
            System.err.println("Error assigning transport to order: " + e.getMessage());
            return false;
        }
    }

    public boolean removeItems(int orderId, int transportId, List<Item> itemsToRemove){
        try {
            Order order = getOrderById(orderId);
            Transport transport = transportService.getTransportById(transportId);

            if (order == null || !order.getTransport().equals(transport)) {
                throw new IllegalArgumentException("can't remove items from this order");
            }

            List<Item> orderItems = new ArrayList<>(order.getItems());
            double removedWeight = 0;

            for (Item itemToRemove : itemsToRemove) {
                if (orderItems.contains(itemToRemove)) {
                    orderItems.remove(itemToRemove);
                    removedWeight += itemToRemove.getTotalWeight();
                }
            }
            
            order.setItems(orderItems);
            transport.setCurrentWeight(transport.getCurrentWeight() - removedWeight);
            
            // Update order in database
            OrderDTO orderDTO = OrderMapper.toDTO(order);
            orderDAO.updateOrder(orderDTO);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error removing items from order: " + e.getMessage());
            return false;
        }
    }

    public boolean removeOrderFromTruck(int orderId) {
        try {
            Order order = getOrderById(orderId);
            if (order == null) return false;
            
            if (order.getTransport() != null) {
                order.getTransport().setCurrentWeight(order.getTransport().getCurrentWeight() - order.OrderWeight());
                order.setTransport(null);
                order.setStatus(OrderStatus.CREATED);

                // Update order in database
                OrderDTO orderDTO = OrderMapper.toDTO(order);
                orderDAO.updateOrder(orderDTO);
                
                return true;
            }
            return false;
        } catch (Exception e) {
            System.err.println("Error removing order from truck: " + e.getMessage());
            return false;
        }
    }

    public boolean cancelOrder(int id){
        try {
            Order order = getOrderById(id);
            if (order == null || !order.canBeCancelled())
                return false;
                
            order.setStatus(OrderStatus.CANCELLED);
            
            // Update order in database
            OrderDTO orderDTO = OrderMapper.toDTO(order);
            orderDAO.updateOrder(orderDTO);
            
            return true;
        } catch (Exception e) {
            System.err.println("Error cancelling order: " + e.getMessage());
            return false;
        }
    }
}

