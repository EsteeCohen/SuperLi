package src.DomainLayer;

import src.DomainLayer.Enums.STATUS;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;


public class Order {
    private int orderId;
    private int supplierId;
    private LocalDate orderDate;
    private LocalDate supplyDate;
    private Map<Integer, Integer> items;  // <catalogNumber, amount>
    private STATUS status;


    public Order(int orderId, int supplierId, LocalDate orderDate, LocalDate supplyDate, Map<Integer, Integer> items, STATUS status) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.supplyDate = supplyDate;
        this.items = items;
        this.status = status;

        if(this.items == null)
            this.items = new HashMap<Integer, Integer>();

    }

    // Getters and setters
    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public int getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(int supplierId) {
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

    public Map<Integer, Integer> getItems() {
        return items;
    }

    public void setItems(Map<Integer, Integer> items) {
        this.items = items;
    }

    public void addItem(int catalogNumber, int amount) {
        items.put(catalogNumber, amount);
    }

    public int getAmount(int catalogNumber) {
        return items.getOrDefault(catalogNumber, 0);
    }

    public void removeItem(int catalogNumber) {
        items.remove(catalogNumber);
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }

    public double getTotalPrice() {
        ProductController productController = ProductController.getInstance();
        double totalPrice = 0;
        for (Map.Entry<Integer, Integer> entry : items.entrySet()) {
            Integer catalogNumber = entry.getKey();
            Integer amount = entry.getValue();

            totalPrice += productController.calculateProductPrice(supplierId, catalogNumber, amount);
        }

    }

    @Override
    public String toString() {
        return "Order{" +
                "orderId=" + orderId +
                ", supplierId=" + supplierId +
                ", date=" + orderDate +
                ", deliveryDate=" + supplyDate +
                ", items=" + items.length +
                ", status=" + status +
                '}';
    }

}