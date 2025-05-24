package src.main.controllers;

import src.main.services.*;
import src.main.entities.*;
import src.main.enums.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public class FacadeController {
    private final IncidentService incidentService;
    private final DriverService driverService;
    private final UserService userService;
    private final SiteService siteService;
    private final OrderService orderService;
    private final TruckService truckService;
    private final TransportService transportService;

    public FacadeController(
            IncidentService incidentService,
            DriverService driverService,
            UserService userService,
            SiteService siteService,
            OrderService orderService,
            TruckService truckService,
            TransportService transportService) {
        this.incidentService = incidentService;
        this.driverService = driverService;
        this.userService = userService;
        this.siteService = siteService;
        this.orderService = orderService;
        this.truckService = truckService;
        this.transportService = transportService;
    }

    // User Management Methods
    public boolean addUser(String id, String username, String password, String fullName, String role) {
        try {
            UserRole userRole = UserRole.valueOf(role);
            return userService.addUser(id, username, password, fullName, userRole);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean updateUser(String id, String username, String password, String fullName, String role) {
        try {
            UserRole userRole = UserRole.valueOf(role);
            return userService.updateUser(id, username, password, fullName, userRole);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean deactivateUser(String id) {
        return userService.deactivateUser(id);
    }

    public String login(String username, String password) {
        User user = userService.authenticateUser(username, password);
        if (user == null) {
            return null;
        }
        return userService.createSession(user.getId());
    }

    public boolean logout(String sessionId) {
        return userService.logout(sessionId);
    }

    public boolean isAuthorized(String sessionId, String operation, String resourceType) {
        return userService.hasPermission(sessionId, operation, resourceType);
    }

    public User getCurrentUser(String sessionId) {
        return userService.getUserBySessionId(sessionId);
    }

    public List<User> getAllUsers(String sessionId) {
        return userService.getAllUsers();
    }

    // Driver Management Methods
    public Driver addDriver(String id, String name, String phone, String licenseType) {
        try {
            LicenseType license = LicenseType.valueOf(licenseType.toUpperCase());
            return driverService.addDriver(id, name, phone, license);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public Driver getDriverById(String id) {
        return driverService.getDriverById(id);
    }

    public List<Driver> getDriversByLicenseType(String licenseType) {
        try {
            LicenseType license = LicenseType.valueOf(licenseType.toUpperCase());
            return driverService.getDriversByLicenseType(license);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Driver> getAllDrivers() {
        return driverService.getAllDrivers();
    }

    // Site Management Methods
    public Site addSite(String id, String name, String address, String contactPhone, String contactName, String zone) {
        try {
            ShippingZone shippingZone = ShippingZone.valueOf(zone);
            return siteService.addSite(id, name, address, contactPhone, contactName, shippingZone);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public boolean deleteSite(String id) {
        return siteService.deleteSite(id);
    }

    public Site getSiteById(String id) {
        return siteService.getSiteById(id);
    }

    public List<Site> getAllSites() {
        return siteService.getAllSites();
    }

    public List<Site> getSitesByZone(String zone) {
        try {
            ShippingZone shippingZone = ShippingZone.valueOf(zone);
            return siteService.getSitesByZone(shippingZone);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    // Truck Management Methods
    public Truck addTruck(String regNumber, String model, String emptyWeight, String maxWeight, String licenseType) {
        try {
            double emptyWeightVal = Double.parseDouble(emptyWeight);
            double maxWeightVal = Double.parseDouble(maxWeight);
            LicenseType license = LicenseType.valueOf(licenseType);
            return truckService.addTruck(regNumber, model, emptyWeightVal, maxWeightVal, license);
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    public List<Truck> getAllTrucks() {
        return truckService.getAllTrucks();
    }

    public Truck getTruckById(String id) {
        return truckService.getTruckById(id);
    }

    public boolean isTruckAvailable(String truckId) {
        return transportService.isTruckAvailable(truckId);
    }

    public boolean isDriverAvailable(String driverId) {
        return transportService.isDriverAvailable(driverId);
    }

    // Order Management Methods
    public Order createOrder(LocalDate date, String siteId, List<Item> items) {
        return orderService.createOrder(date, siteId, items);
    }

    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    public Order getOrderById(int id) {
        return orderService.getOrderById(id);
    }

    public List<Order> getOrdersByDate(LocalDate date) {
        return orderService.getOrdersByDate(date);
    }

    public List<Order> getOrdersByStatus(OrderStatus status) {
        return orderService.getOrdersByStatus(status);
    }

    public boolean updateOrderStatus(int id, OrderStatus newStatus) {
        return orderService.updateOrderStatus(id, newStatus);
    }

    public boolean assignTransportToOrder(int orderId, int transportId) {
        return orderService.assignTransportToOrder(orderId, transportId);
    }

    public boolean removeItems(int orderId, int transportId, List<Item> itemsToRemove) {
        return orderService.removeItems(orderId, transportId, itemsToRemove);
    }

    public boolean cancelOrder(int id) {
        return orderService.cancelOrder(id);
    }

    // Transport Management Methods
    public Transport createTransport(LocalDate date, LocalTime time, String truckId, String driverId, String sourceId, List<String> destinationIds) {
        return transportService.createTransport(date, time, truckId, driverId, sourceId, destinationIds);
    }

    public List<Transport> getAllTransports() {
        return transportService.getAllTransports();
    }

    public Transport getTransportById(int id) {
        return transportService.getTransportById(id);
    }

    public List<Transport> getTransportsByDate(LocalDate date) {
        return transportService.getTransportsByDate(date);
    }

    public List<Transport> getTransportsByStatus(TransportStatus status) {
        return transportService.getTransportsByStatus(status);
    }

    public List<Transport> getTransportsByZone(ShippingZone zone) {
        return transportService.getTransportsByZone(zone);
    }

    public boolean updateTransportStatus(int id, TransportStatus newStatus) {
        return transportService.updateTransportStatus(id, newStatus);
    }

    public boolean changeTruck(int transportId, String truckId) {
        return transportService.changeTruck(transportId, truckId);
    }

    public boolean changeDriver(int transportId, String driverId) {
        return transportService.changeDriver(transportId, driverId);
    }

    public boolean addDestination(int transportId, String siteId) {
        return transportService.addDestination(transportId, siteId);
    }

    // Incident Management Methods
    public boolean reportIncident(int transportId, String type, String description) {
        try {
            IncidentType incidentType = IncidentType.valueOf(type);
            return incidentService.reportIncident(transportId, incidentType, description);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean updateIncidentStatus(String incidentId, String status) {
        try {
            IncidentStatus incidentStatus = IncidentStatus.valueOf(status);
            return incidentService.updateIncidentStatus(incidentId, incidentStatus);
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean setResolution(String incidentId, String description, String resolutionType) {
        return incidentService.setResolution(incidentId, description);
    }

    public List<Incident> getActiveIncidents() {
        return incidentService.getActiveIncidents();
    }

    // User information methods
    public String getCurrentUserName(String sessionId) {
        User user = getCurrentUser(sessionId);
        return user != null ? user.getFullName() : "Unknown User";
    }
    
    public String getCurrentUserRole(String sessionId) {
        User user = getCurrentUser(sessionId);
        return user != null ? user.getRole().toString() : "Unknown Role";
    }
    
    public boolean changePassword(String sessionId, String currentPassword, String newPassword) {
        User user = getCurrentUser(sessionId);
        if (user == null) return false;
        
        // Verify current password (simplified - in real app should be hashed)
        if (!user.getPassword().equals(currentPassword)) {
            return false;
        }
        
        // Update password
        return userService.updateUserPassword(user.getId(), newPassword);
    }
} 