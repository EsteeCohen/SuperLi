package PresetationLayer;

import ServiceLayer.SupplierSystemService;
import java.time.LocalDate;
import java.util.*;

public class SystemInitializer {
    public static void initializeSystem(SupplierSystemService service) {
        /* ---------------- Suppliers ---------------- */
        service.addSupplierWithDelivery("Supplier 1", "SUP001", "111222333", "1,3,5", List.of("Ofek, 054318083"));
        service.addSupplierNeedsPickup("Supplier 2", "SUP002", "444555666", "123 Tech Avenue", List.of("Shiri,0522555555"));
        System.out.println("Suppliers initialized.");

        /* ---------------- Products ---------------- */
        service.addProduct("milk", "SUP001", "WM001", 10, 25.0, 1);
        service.addProduct("P2", "SUP001", "WM002", 5, 15.0, 2);
        service.addProduct("P3", "SUP002", "AM001", 8, 30.0, 1);
        service.addProduct("P4", "SUP002", "AM002", 12, 50.0, 2);
        System.out.println("Products initialized.");

        /* ---------------- Supplier 1 agreement & order ---------------- */
        Map<Integer, Map<Integer, Integer>> supplier2Discounts = new HashMap<>();
        supplier2Discounts.put(0, Map.of(5, 3, 10, 6));  // 3% at 5 units, 6% at 10 units
        if (service.createAgreement("SUP001", 0, 2, LocalDate.now().minusDays(5), LocalDate.now().plusDays(30), supplier2Discounts)) {
            if (service.createAgreement("SUP001", 0, 2, LocalDate.now().minusDays(5), LocalDate.now().plusDays(30), supplier2Discounts)) {
                System.out.println("Supplier 1 agreement created with products [0,1]");
                Map<Integer, Integer> Supplier1Items = Map.of(0, 3, 1, 2); // WM001 x3, WM002 x2
                //service.insertOrder("SUP001", LocalDate.now(), LocalDate.now().plusDays(7), Supplier1Items,
                //        "Israel Israeli", "0501234567", 0, 2); // agreement idx 0, IN_PROCESS
                System.out.println("Supplier 1 order initialized.");
            }

            /* ---------------- Supplier 2 agreement & order ---------------- */
            if (service.createAgreement("SUP002", 1, 3, LocalDate.now().minusDays(10), LocalDate.now().plusDays(60), supplier2Discounts)) {
                System.out.println("Supplier 2 agreement created with products [0,1]");
                Map<Integer, Integer> Supplier2Items = Map.of(0, 5); // AM001 x5
                //service.insertOrder("SUP002", LocalDate.now(), LocalDate.now().plusDays(5), Supplier2Items,
                   //     "Jane Smith", "0509876543", 0, 1); // agreement idx 0, DELIVERED
                System.out.println("Supplier 2 order initialized.");
            }

        }
    }
}
