import java.util.Scanner;

import employeeDev.src.presentationLayer.UserInputManager;
import transportDev.src.main.TransportApp;

public class main {
    public static void main(String[] args) {
       System.out.println("=== please pick a system ===");
       System.out.println("1. Employee Management System");
         System.out.println("2. Transport Management System");
         System.out.println("0. Exit");
         System.out.print("Enter your choice: ");
         Scanner scanner = new Scanner(System.in);
         int choice = Integer.parseInt(UserInputManager.getUserInput(scanner));
            switch (choice) {
                case 1:
                    System.out.println("Starting Employee Management System...");
                    employeeDev.adss_v0.main(args);
                    break;
                case 2:
                    System.out.println("Starting Transport Management System...");
                    TransportApp.main(args);
                    break;
                case 0:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
    }
}
