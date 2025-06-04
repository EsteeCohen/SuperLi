package transportDev.src.main.dtos;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class TransportDTO {
    private int id;
    private LocalDate date;
    private LocalTime time;
    private TruckDTO truck;
    private DriverDTO driver;
    private SiteDTO sourceSite;
    private List<SiteDTO> destinations;
    private double currentWeight;
    private String status; // Using String for easier database storage

    public TransportDTO(int id, LocalDate date, LocalTime time, TruckDTO truck, DriverDTO driver, 
                       SiteDTO sourceSite, List<SiteDTO> destinations, double currentWeight, String status) {
        this.id = id;
        this.date = date;
        this.time = time;
        this.truck = truck;
        this.driver = driver;
        this.sourceSite = sourceSite;
        this.destinations = destinations;
        this.currentWeight = currentWeight;
        this.status = status;
    }

    // Getters
    public int getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public LocalTime getTime() {
        return time;
    }

    public TruckDTO getTruck() {
        return truck;
    }

    public DriverDTO getDriver() {
        return driver;
    }

    public SiteDTO getSourceSite() {
        return sourceSite;
    }

    public List<SiteDTO> getDestinations() {
        return destinations;
    }

    public double getCurrentWeight() {
        return currentWeight;
    }

    public String getStatus() {
        return status;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public void setTruck(TruckDTO truck) {
        this.truck = truck;
    }

    public void setDriver(DriverDTO driver) {
        this.driver = driver;
    }

    public void setSourceSite(SiteDTO sourceSite) {
        this.sourceSite = sourceSite;
    }

    public void setDestinations(List<SiteDTO> destinations) {
        this.destinations = destinations;
    }

    public void setCurrentWeight(double currentWeight) {
        this.currentWeight = currentWeight;
    }

    public void setStatus(String status) {
        this.status = status;
    }
} // :)