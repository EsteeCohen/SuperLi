package src.main.entities;

import java.time.LocalDate;
import java.util.List;
import src.main.enums.OrderStatus;


public class Order {
    private final int id;
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

    public Order(int id, LocalDate date, Site site, List<Item> items){
        this.id = id;
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
    public Site getDestinationSite(){
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

    public boolean canBeCancelled(){
        return status == OrderStatus.CREATED;
    }

    //----------------- toString -------------------
    private String getItemsString() {
        if (items == null || items.isEmpty()) {
            return "אין פריטים";
        }

        StringBuilder sb = new StringBuilder();
        for (Item item : items) {
            sb.append(", ").append(item.toString()).append("\n");
        }
        return sb.toString();
    }

    @Override
    public String toString() {
        return "מזהה הזמנה: " + id +
                ", תאריך: " + (date != null ? date.toString() : "לא צוין") +
                ", אתר: " + (site != null ? site.getName() : "לא צוין") +
                ", סטטוס: " + status +
                "\nמשקל כולל: " + OrderWeight() + " ק\"ג" +
                (transport != null ? "\nמקושרת להובלה: #" + transport.getId() : "\nללא הובלה") +
                "\nפריטים:\n" + getItemsString();
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Order order)) return false;
        return this.id == order.id;
    }

}
