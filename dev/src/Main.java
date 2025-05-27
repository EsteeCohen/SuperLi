//package src;

import java.util.Scanner;

import PresetationLayer.*;
import ServiceLayer.*;

public class Main {

    public static void main(String[] args) {
        SupplierSystemService service = SupplierSystemService.getInstance();
        presentation p = presentation.getInstance();
        Scanner scanner = new Scanner(System.in);

        /*System.out.println("Do you want to initialize the system with default data? (Y/N)");
        String choice = scanner.nextLine();

        if (choice.equalsIgnoreCase("Y")) {
            SystemInitializer.initializeSystem(service);
            p.startUp();
            System.out.println("System initialized with default data.");
        } else {
            System.out.println("System started without initial data.");
        }*/

        SupplierSystemUI ui = new SupplierSystemUI();
        ui.start();
    }
}