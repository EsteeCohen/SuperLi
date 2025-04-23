package src.DomainLayer;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class SystemControllerTest {

    @Test
    public void testCreateAgreement_SuccessfulCreation() {
        SystemController controller = SystemController.getInstance();

        String supplierId = "1";
        controller.addSupplierWithDelivery("Supplier1", supplierId, "123456789", "1,2,3", Arrays.asList("John,123456789"));

        int paymentMethod = 0; // Assuming valid index
        int paymentTiming = 1; // Assuming valid index
        LocalDate validFrom = LocalDate.of(2025, 1, 1);
        LocalDate validTo = LocalDate.of(2025, 12, 31);
        Map<Integer, Map<Integer, Integer>> indexProducts = new HashMap<>();

        boolean result = controller.createAgreement(supplierId, paymentMethod, paymentTiming, validFrom, validTo, indexProducts);

        assertTrue(result);
        List<String> agreements = controller.getAgreementsBySupplierAsStrings(supplierId);
        assertEquals(1, agreements.size());
    }

    @Test
    public void testCreateAgreement_FailureDueToInvalidPaymentMethod() {
        SystemController controller = SystemController.getInstance();

        String supplierId = "2";
        controller.addSupplierWithDelivery("Supplier2", supplierId, "987654321", "4,5", Arrays.asList("Doe,987654321"));

        int paymentMethod = -1; // Invalid index
        int paymentTiming = 1; // Valid index
        LocalDate validFrom = LocalDate.of(2025, 1, 1);
        LocalDate validTo = LocalDate.of(2025, 12, 31);
        Map<Integer, Map<Integer, Integer>> indexProducts = new HashMap<>();

        boolean result = controller.createAgreement(supplierId, paymentMethod, paymentTiming, validFrom, validTo, indexProducts);

        assertFalse(result);
        List<String> agreements = controller.getAgreementsBySupplierAsStrings(supplierId);
        assertEquals(0, agreements.size());
    }

    @Test
    public void testCreateAgreement_FailureDueToInvalidSupplier() {
        SystemController controller = SystemController.getInstance();

        String supplierId = "NonExistentSupplier";

        int paymentMethod = 0; // Valid index
        int paymentTiming = 1; // Valid index
        LocalDate validFrom = LocalDate.of(2025, 1, 1);
        LocalDate validTo = LocalDate.of(2025, 12, 31);
        Map<Integer, Map<Integer, Integer>> indexProducts = new HashMap<>();

        boolean result = controller.createAgreement(supplierId, paymentMethod, paymentTiming, validFrom, validTo, indexProducts);

        assertFalse(result);
    }

    @Test
    public void testCreateAgreement_FailureDueToNullDate() {
        SystemController controller = SystemController.getInstance();

        String supplierId = "3";
        controller.addSupplierWithDelivery("Supplier3", supplierId, "111222333", "6,7", Arrays.asList("Jane,111222333"));

        int paymentMethod = 0; // Valid index
        int paymentTiming = 1; // Valid index
        LocalDate validFrom = null; // Invalid (null) validFrom
        LocalDate validTo = LocalDate.of(2025, 12, 31);
        Map<Integer, Map<Integer, Integer>> indexProducts = new HashMap<>();

        boolean result = controller.createAgreement(supplierId, paymentMethod, paymentTiming, validFrom, validTo, indexProducts);

        assertFalse(result);
        List<String> agreements = controller.getAgreementsBySupplierAsStrings(supplierId);
        assertEquals(0, agreements.size());
    }

    @Test
    public void testCreateAgreement_FailureDueToInvalidProductIndex() {
        SystemController controller = SystemController.getInstance();

        String supplierId = "4";
        controller.addSupplierWithDelivery("Supplier4", supplierId, "444555666", "1,5", Arrays.asList("Anne,444555666"));

        int paymentMethod = 0; // Valid index
        int paymentTiming = 1; // Valid index
        LocalDate validFrom = LocalDate.of(2025, 1, 1);
        LocalDate validTo = LocalDate.of(2025, 12, 31);
        Map<Integer, Map<Integer, Integer>> indexProducts = new HashMap<>();
        indexProducts.put(999, new HashMap<>()); // Invalid product index

        boolean result = controller.createAgreement(supplierId, paymentMethod, paymentTiming, validFrom, validTo, indexProducts);

        assertFalse(result);
        List<String> agreements = controller.getAgreementsBySupplierAsStrings(supplierId);
        assertEquals(0, agreements.size());
    }
}