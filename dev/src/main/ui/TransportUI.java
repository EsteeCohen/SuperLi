package src.main.ui;

import java.util.Scanner;

import src.main.controllers.TransportController;

public class TransportUI {
    private TransportController transportController;
    private Scanner scanner;
    
    // Constructor
    public TransportUI(TransportController transportController) {
        this.transportController = transportController;
        this.scanner = new Scanner(System.in);
    }
    
    // Methods
    public void showTransportMenu() {}
    public void createNewTransport() {}
    public void displayAllTransports() {}
    public void displayTransportDetails(String id) {}
    public void updateTransportStatus() {}
    public void changeTruckForTransport() {}
    public void changeDriverForTransport() {}
    public void cancelTransport() {}
    public void viewTransportsByDate() {}
    public void viewTransportsByStatus() {}
    public void viewTransportsByZone() {}
}
