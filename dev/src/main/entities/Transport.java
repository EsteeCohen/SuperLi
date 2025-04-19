package src.main.entities;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ArrayList;

import src.main.enums.TransportStatus;

public class Transport {
    private String id;
    private LocalDate date;
    private LocalTime time;
    private Truck truck;
    private Driver driver;
    private Site sourceSite;
    private List<Site> destinations;
    private double currentWeight;
    private TransportStatus status;

    //----------------- Constructors -------------------
    public Transport(String id, LocalDate date, LocalTime time, Truck truck, Driver driver, Site sourceSite, List<Site> destinations, double currentWeight, TransportStatus status) {
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
    //----------------- Getters and Setters -------------------
    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public LocalDate getDate() {
        return date;
    }
    public void setDate(LocalDate date) {
        this.date = date;
    }
    public LocalTime getTime() {
        return time;
    }
    public void setTime(LocalTime time) {
        this.time = time;
    }
    public Truck getTruck() {
        return truck;
    }
    public void setTruck(Truck truck) {
        if(truck != null){
            this.truck = truck;
        }
    }
    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        if(driver != null){
            this.driver = driver;
        }
    }
    public Site getSourceSite() {
        return sourceSite;
    }
    public void setSourceSite(Site sourceSite) {
        if(sourceSite != null){
            this.sourceSite = sourceSite;
        }
    }
    public List<Site> getDestinations() {
        return new ArrayList<>(destinations);
    }
    public void setDestinations(List<Site> destinations) {
        this.destinations = destinations;
    }
    public double getCurrentWeight() {
        return currentWeight;
    }
    public void setCurrentWeight(double currentWeight) {
        if(currentWeight >= 0){
            this.currentWeight = currentWeight;
        }
    }
    public TransportStatus getStatus() {
        return status;
    }
    public void setStatus(TransportStatus status) {
        if(status != null){
            this.status = status;
        }
    }
    public double getTotalWeight() {
        return truck != null ? truck.getEmptyWeight() + currentWeight : 0;
    }
    //----------------- methods -------------------
    public boolean isWeightVaild(){
        return truck != null && truck.canCarryLoad(currentWeight);
    }
    public boolean isDriverLicenseValid(){
        return driver != null && driver.canDrive(truck) && truck != null;
    }
    public boolean validateSameRegion(){
        if (sourceSite == null || destinations == null || destinations.isEmpty()) {
            return false;
        }
        
        for(Site destination : destinations) {
            if (destination.getShippingZone() != sourceSite.getShippingZone()) {
                return false; // Different shipping zones
            }
        }
        return true; // All destinations are in the same shipping zone as the source site
    }
    public boolean validateTransport() {
        return isWeightVaild() && isDriverLicenseValid() && validateSameRegion();
    }
    public boolean canBeCancelled() {
        return status == TransportStatus.PLANNING;
    }
    public boolean canChangeDriver() {
        return status == TransportStatus.PLANNING;
    }
    public boolean canChangeTruck() {
        return status == TransportStatus.PLANNING;
    }
    public void cancelTransport() {
        if (canBeCancelled()) {
            status = TransportStatus.CANCELLED;
        }
    }
    //----------------- toString -------------------
    private String getDestinationsString() {
        if (destinations == null || destinations.isEmpty()) {
            return "אין";
        }

        StringBuilder sb = new StringBuilder();
        for (Site destination : destinations) {
            if (sb.length() > 0) {
                sb.append(", ");
            }
            sb.append(destination.getName());
        }
        return sb.toString();
    }
    @Override
    public String toString(){
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

        return "מזהה הובלה: " + id + 
               ", תאריך: " + (date != null ? date.format(dateFormatter) : "לא צוין") + 
               ", שעה: " + (time != null ? time.format(timeFormatter) : "לא צוינה") + 
               "\nמשאית: " + (truck != null ? truck.getRegNumber() + " (" + truck.getModel() + ")" : "לא צוינה") + 
               "\nנהג: " + (driver != null ? driver.getName() + " (ת.ז: " + driver.getId() + ")" : "לא צוין") + 
               "\nמקור: " + (sourceSite != null ? sourceSite.getName() : "לא צוין") + 
               "\nיעדים: " + getDestinationsString() + 
               "\nמשקל מטען: " + currentWeight + " ק\"ג" +
               "\nסטטוס: " + status;
    }    
    //----------------- equals -------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transport)) return false;

        Transport transport = (Transport) o;

        return id.equals(transport.id);
    }

}
