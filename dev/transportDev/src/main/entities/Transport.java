package transportDev.src.main.entities;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import employeeDev.src.serviceLayer.DriverSL;
import transportDev.src.main.enums.ShippingZone;
import transportDev.src.main.enums.TransportStatus;

public class Transport {
    private final int id;
    private static int idCounter = 1;
    private LocalDate date;
    private LocalTime time;
    private Truck truck;
    private DriverSL driver;
    private final Site sourceSite;
    private List<Site> destinations;
    private double currentWeight;
    private TransportStatus status;

    //----------------- Constructors -------------------
    public Transport(LocalDate date, LocalTime time, Truck truck, DriverSL driver, Site sourceSite, List<Site> destinations) {
        this.id = idCounter++;
        this.status = TransportStatus.PLANNING;
        setDate(date);
        setTime(time);
        setTruck(truck);
        setDriver(driver);
        if (sourceSite == null) {
            throw new IllegalArgumentException("Source site cannot be null");
        }
        this.sourceSite = sourceSite;
        setDestinations(destinations);
        this.currentWeight = 0;
    }

    //----------------- Getters and Setters -------------------
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
    public DriverSL getDriver(){
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

    public void setDate(LocalDate date){
        if (date.isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Date cannot be in the past");
        }
        this.date = date;
    }
    public void setTime(LocalTime time){
        if (date != null && LocalDateTime.of(date, time).isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Time cannot be in the past");
        }
        this.time = time;
    }


    public void setTruck(Truck truck){
        if (truck == null) {
            throw new IllegalArgumentException("Truck cannot be null");
        }
        if(driver!=null) {
            if (!driver.canDrive(truck.getLicenseType())){
                throw new IllegalArgumentException("Driver :" + driver.getFullName() + " that assign to this transport has no valid license");
            }
        }
        if (!canChangeTruck()){
            throw new IllegalArgumentException("Truck can't be changed after planning status");
        }
        if (this.truck != null) {
            this.truck.available();
        }
        this.truck = truck;
        this.truck.unavailable();
    }

    public void setDriver(DriverSL driver){
        if (driver == null) {
            throw new IllegalArgumentException("Driver cannot be null");
        }
        if (truck != null) {
            if (!driver.canDrive(truck.getLicenseType())) {
                throw new IllegalArgumentException("Driver :" + driver.getFullName() + " that assign to this transport has no valid license");
            }
        }
        if (!canChangeDriver()){
            throw new IllegalArgumentException("Driver can't be changed after planning status");
        }
        if(!driver.isAvailableToDrive()) {
            throw new IllegalArgumentException("Driver " + driver.getFullName() + " is not available");
        }
        if (this.driver != null) {
            this.driver.setAvailableToDrive(true);
        }
        this.driver = driver;
        driver.setAvailableToDrive(false);

    }

    public void setDestinations(List<Site> destinations) {// With zone validation
        if(!validateSameRegion(destinations)){
            throw new IllegalArgumentException("not all sites in the same shipping zone");
        }
        this.destinations = destinations;
    }

    public void setCurrentWeight(double currentWeight) {
        if (isWeightValid(currentWeight)){
            this.currentWeight = currentWeight;
        }
        else {
            throw new IllegalArgumentException("new current weight isn't valid");
        }
    }
    public void setStatus(TransportStatus status) {
        if(status != null){
            this.status = status;
        }
    }
    //----------------- methods -------------------
    public double getTotalWeight() {
        if(truck == null){
            throw new IllegalArgumentException("truck cannot be null to calculate total weight");

        }
        return truck.getEmptyWeight() + currentWeight;
    }
    public boolean isWeightValid(double currentWeight){
        return truck != null && truck.canCarryLoad(currentWeight) && currentWeight >= 0;
    }

    public boolean validateSameRegion(List<Site> destinations){
        if (destinations == null || destinations.isEmpty()) {
            throw new IllegalArgumentException("there must be at least one destination");
        }
        ShippingZone zone = sourceSite.getShippingZone();
        for(Site destination : destinations) {
            if (destination.getShippingZone() != zone) {
                return false; // Different shipping zones
            }
        }
        return true; // All destinations are in the same shipping zone
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

    //----------------- toString -------------------
    private String getDestinationsString() {
        if (destinations == null || destinations.isEmpty()) {
            return "אין";
        }

        StringBuilder sb = new StringBuilder();
        for (Site destination : destinations) {
            if (!sb.isEmpty()) {
                sb.append(", ");
            }
            sb.append(destination.getName());
        }
        return sb.toString();
    }
 
    //----------------- equals -------------------
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Transport transport)) return false;
        return this.id == transport.id;
    }
    // :)
}
