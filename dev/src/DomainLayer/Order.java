package src.DomainLayer;

import src.DomainLayer.Enums.STATUS;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Order {
    private int orderId;
    private String supplierId;
    private LocalDate orderDate;
    private LocalDate supplyDate;
    private ContactPerson contactPerson;
    private Agreement agreement;
    private Map<String, Integer> items;  // <catalogNumber, amount>
    private STATUS status;
    private double totalPrice;


    public Order(int orderId, String supplierId, LocalDate orderDate,ContactPerson contactPerson, Agreement agreement, LocalDate supplyDate, Map<String, Integer> items, STATUS status, double totalPrice) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.supplyDate = supplyDate;
        this.contactPerson = contactPerson;
        this.agreement = agreement;
        this.items = items;
        this.status = status;

        if(this.items == null)
            this.items = new HashMap<String, Integer>();

        this.totalPrice = totalPrice;
    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public String getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(String supplierId) {
        this.supplierId = supplierId;
    }

    public LocalDate getDate() {
        return orderDate;
    }

    public void setDate(LocalDate date) {
        this.orderDate = date;
    }

    public LocalDate getSupplyDate() {
        return supplyDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.supplyDate = deliveryDate;
    }

    public Map<String, Integer> getItems() {
        return items;
    }

    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }

    public void addItem(String catalogNumber, int amount) {
        items.put(catalogNumber, amount);
    }

    public int getAmount(String catalogNumber) {
        return items.getOrDefault(catalogNumber, 0);
    }

    public void removeItem(String catalogNumber) {
        items.remove(catalogNumber);
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public double getTotalPrice() {
        return totalPrice;
    }




    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", supplierId=" + supplierId +
                ", date=" + orderDate +
                ", deliveryDate=" + supplyDate +
                ", items=" + items.size() +
                ", status=" + status +
                '}';
    }

}