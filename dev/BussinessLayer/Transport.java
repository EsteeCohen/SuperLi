package BussinessLayer;

import java.time.LocalDate;
import java.time.LocalTime;

public class Transport {
    private int id;
    private static int idCounter = 1;
    private LocalDate date;
    private LocalTime time;
    private Truck truck;
    private Driver driver;
    private Site sourceSite;
    private List<Site> destinations;
    private double currentWeight;
    private TransportStatus status;

    public Transport(LocalDate date, LocalTime time, Truck truck, Driver driver, Site sourceSite, List<Site> destinations, double currentWeight) {
        this.id = idCounter++;
        setDate(date);
        setTime(time);
        this.truck = truck;
        setDriver(driver);
        this.sourceSite = sourceSite;
        setDestinations(destinations);
        this.currentWeight = currentWeight;
        this.status = TransportStatus.PLANNING;
    }

//    GETTERS
    public int getId() {
        return id;
    }
    public LocalDate getDate(){
        return date;
    }
    public LocalTime getTime(){
        return time;
    }
    public Truck getTruck(){
        return truck;
    }
    public Driver getDriver(){
        return driver;
    }
    public Site getSourceSite(){
        return sourceSite;
    }
    public List<Site> getDestinations(){
        return destinations;
    }
    public double getCurrentWeight(){
        return currentWeight;
    }
    public TransportStatus getStatus(){
        return status;
    }

//    SETTERS
    public void setDate(LocalDate date){
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        this.date = date;
    }
    public void setTime(LocalTime time){
        if (time.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Time cannot be in the past");
        }
    }
    public void setTruck(Truck truck){ // With license validation להוסיף
        this.truck = truck;
    }
    public void setDriver(Driver driver){
        if(isDriverLicenseValid()) {
            this.driver = driver;
        }
    }
    public void setSourceSite(Site sourceSite){
        this.sourceSite = sourceSite;
    }
    public void setDestinations(List<Site> destinations) {// With zone validation
        if(areDestinationsValid()){
            this.destinations = destinations;
        }
    }
    public void setCurrentWeight(double currentWeight) {// With weight validation
        if (isWeightValid()){
            currentWeight = currentWeight;
        }
    }
    public void setStatus(TransportStatus status){
        this.status = status;
    }

//    METHODS
    public boolean isWeightValid() {
        if (currentWeight < 0 || currentWeight > truck.getMaxWeight()){
            return false;
        }
        return true;
    }
    public boolean isDriverLicenseValid() {

    } // Checks if driver has valid license for truck
    public boolean areDestinationsValid() {
        zone = destinations[0].getShippingZone();
        for (Site destination : destinations) {
            if (destination.getShippingZone() != zone){
                throw new IllegalArgumentException("not all sites in the same shipping zone");
            }
        }
    }

    public boolean canBeCancelled(){
        return status == TransportStatus.PLANNING;
    }



}
