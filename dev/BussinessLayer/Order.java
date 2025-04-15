package BussinessLayer;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

public class Order {
    private int id;
    private static int idCounter = 1;
    private LocalDate date;
    private Site site;
    private List<Item> items;
    private Transport transport;
    private OrderStatus status;

    public Order(LocalDate date, Site site, List<Item> items){
        this.id = idCounter++;
        setDate(date);
        this.site = site;
        this.items = items;
        this.transport = null; //ברירת מחדל שלא צריך משלוח
        this.status = OrderStatus.CREATED;
    }

//     GETTERS
    public int getId(){
        return id;
    }
    public LocalDate getDate(){
        return date;
    }
    public Site getSite(){
        return site;
    }
    public List<Item> getItems(){
        return items;
    }
    public Transport getTransport(){
        return transport;
    }
    public OrderStatus getStatus(){
        return status;
    }

//    SETTERS
    public void setDate(LocalDate date){
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        this.date = date;
    }
    public void setSite(Site site){
        this.site = site;
    }
    public void setItems(List<Item> items){
        this.items = items;
    }
    public void setTransport(Transport transport){
        this.transport = transport;
    }
    public void setStatus(OrderStatus status){
        this.status = status;
    }

//    METHODS
    public double OrderWeight() {
        double totalWeight = 0;
        for (Item item : items) {
            totalWeight = totalWeight + item.getTotalWeight();
        }
        return totalWeight;
    }

    public double totalWeight() {
        if (transport == null) {
            return OrderWeight();
        }
        return OrderWeight() + transport.getTruck().getWeight();
    }
    public boolean canBeCancelled(){
        return status == OrderStatus.CREATED;
    }


}
