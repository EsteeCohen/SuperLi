package transportDev.src.main.dtos;

import java.time.LocalDate;
import java.util.List;

public class OrderDTO {
    private int id;
    private LocalDate orderDate;
    private String status;
    private SiteDTO sourceSite;
    private SiteDTO destinationSite;
    private List<ItemDTO> items;
    private double totalWeight;

    public OrderDTO(int id, LocalDate orderDate, String status, SiteDTO sourceSite, 
                   SiteDTO destinationSite, List<ItemDTO> items, double totalWeight) {
        this.id = id;
        this.orderDate = orderDate;
        this.status = status;
        this.sourceSite = sourceSite;
        this.destinationSite = destinationSite;
        this.items = items;
        this.totalWeight = totalWeight;
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDate getOrderDate() {
        return orderDate;
    }

    public String getStatus() {
        return status;
    }

    public SiteDTO getSourceSite() {
        return sourceSite;
    }

    public SiteDTO getDestinationSite() {
        return destinationSite;
    }

    public List<ItemDTO> getItems() {
        return items;
    }

    public double getTotalWeight() {
        return totalWeight;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setSourceSite(SiteDTO sourceSite) {
        this.sourceSite = sourceSite;
    }

    public void setDestinationSite(SiteDTO destinationSite) {
        this.destinationSite = destinationSite;
    }

    public void setItems(List<ItemDTO> items) {
        this.items = items;
    }

    public void setTotalWeight(double totalWeight) {
        this.totalWeight = totalWeight;
    } // :)
} 