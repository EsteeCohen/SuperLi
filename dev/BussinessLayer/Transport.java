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

    public Transport(LocalDate date, LocalTime time, Truck truck, Driver driver, Site sourceSite, List<Site> destinations) {
        this.id = idCounter++;
        setDate(date);
        setTime(time);
        setTruck(truck);
        setDriver(driver);
        this.sourceSite = sourceSite;
        setDestinations(destinations);
        this.currentWeight = 0;
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
        this.time = time;
    }
    public void setTruck(Truck truck){
        if(driver!=null) {
            if (!driver.driverCanDrive(truck.getTypeOfLicence())){
                throw new IllegalArgumentException("Driver :" + driver.getName() + " that assign to this transport has no valid license");
            }
        }
        if (this.truck != null) {
            this.truck.available();
        }
        this.truck = truck;
        truck.istaken();
    }
    public void setDriver(Driver driver){
        if (truck != null) {
            if (!driver.driverCanDrive(truck.getTypeOfLicence())) {
                throw new IllegalArgumentException("Driver :" + driver.getName() + " that assign to this transport has no valid license");
            }
        }
        if(!driver.checkIfTheDriverIsAvailable(date)) {
            throw new IllegalArgumentException("Driver " + driver.getName() + " is not available in : " + date);
        }
        if (this.driver != null) {
            this.driver.available();
        }
        this.driver = driver;
        driver.istaken();

    }
    public void setDestinations(List<Site> destinations) {// With zone validation
        if(!areDestinationsValid()){
            throw new IllegalArgumentException("not all sites in the same shipping zone");
        }
        this.destinations = destinations;
    }
    public void setCurrentWeight(double currentWeight) {
        if (isWeightValid()){
            this.currentWeight = currentWeight;
        }
    }
    public void setStatus(TransportStatus status){
        this.status = status;
    }

//    METHODS
    public boolean isWeightValid() {
        return (currentWeight > 0) && (currentWeight < truck.getMaxWeight());
    }

    public boolean areDestinationsValid() {
        zone = destinations[0].getShippingZone();
        for (Site destination : destinations) {
            if (destination.getShippingZone() != zone){
                return false;
            }
        }
        return true;
    }

    public boolean canBeCancelled(){
        return status == TransportStatus.PLANNING;
    }



}
