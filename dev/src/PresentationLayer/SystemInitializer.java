package src.PresentationLayer;

import src.ServiceLayer.SupplierSystemService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SystemInitializer {
    public static void initializeSystem(SupplierSystemService service) {
        /* ---------------- Suppliers ---------------- */
        service.addSupplierWithDelivery("Supplier 1", "SUP001", "111222333", "1,3,5");
        service.addSupplierNeedsPickup("Supplier 2",  "SUP002", "444555666", "123 Tech Avenue");
        System.out.println("Suppliers initialized.");

        /* ---------------- Products ---------------- */
        service.addProduct("SUP001", "WM001", 10, new ArrayList<>(List.of("10,5%", "20,10%")), 25.0, 0);
        service.addProduct("SUP001", "WM002",  5, new ArrayList<>(List.of("15,7%")),               15.0, 1);
        service.addProduct("SUP002", "AM001",  8, new ArrayList<>(List.of("5,3%",  "10,6%")),     30.0, 0);
        service.addProduct("SUP002", "AM002", 12, new ArrayList<>(),                                  50.0, 1);
        System.out.println("Products initialized.");

        /* ---------------- Supplier 1 agreement & order ---------------- */
        if (service.createAgreement("SUP001", 0, 2, LocalDate.now().minusDays(5), LocalDate.now().plusDays(30), List.of(0,1))) {
            System.out.println("Supplier 1 agreement created with products [0,1]");
            Map<Integer,Integer> Supplier1Items = Map.of(0,3, 1,2); // WM001 x3, WM002 x2
            service.insertOrder("SUP001", LocalDate.now(), LocalDate.now().plusDays(7), Supplier1Items,
                    "John Doe", "0501234567", 0, 1); // agreement idx 0, IN_PROCESS
            System.out.println("Supplier 1 order initialized.");
        }

        /* ---------------- Supplier 2 agreement & order ---------------- */
        if (service.createAgreement("SUP002", 1, 3, LocalDate.now().minusDays(10), LocalDate.now().plusDays(60), List.of(0,1))) {
            System.out.println("🔍 Supplier 2 agreement created with products [0,1]");
            Map<Integer,Integer> Supplier2Items = Map.of(0,5); // AM001 x5
            service.insertOrder("SUP002", LocalDate.now(), LocalDate.now().plusDays(5), Supplier2Items,
                    "Jane Smith", "0509876543", 0, 0); // agreement idx 0, DELIVERED
            System.out.println("✅ Supplier 2 order initialized.");
        }

        System.out.println("System initialization complete.");
    }
}
