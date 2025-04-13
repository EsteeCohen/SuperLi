package src.DomainLayer;

import src.DomainLayer.Enums.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

// System initialization class
class SystemInitializer {
    private SupplierService supplierService;
    private OrderService orderService;

    public SystemInitializer() {
        supplierService = SupplierService.getInstance();
        orderService = OrderService.getInstance();
    }

    public void initializeSystem() {
        // Create suppliers
        Supplier supplier1 = supplierService.createSupplier("Tnuva Dairy", 123456789);
        Supplier supplier2 = supplierService.createSupplier("Strauss Group", 987654321);
        Supplier supplier3 = supplierService.createSupplier("Osem-Nestle", 456789123);

        // Add contact persons
        supplierService.addContactPersonToSupplier(supplier1.getSupplierId(), "Moshe Cohen", "moshe@tnuva.com", 541234567);
        supplierService.addContactPersonToSupplier(supplier1.getSupplierId(), "Sarah Levi", "sarah@tnuva.com", 541234568);
        supplierService.addContactPersonToSupplier(supplier2.getSupplierId(), "David Miller", "david@strauss.com", 541234569);
        supplierService.addContactPersonToSupplier(supplier3.getSupplierId(), "Rachel Green", "rachel@osem.com", 541234570);

        // Create agreements
        // Tnuva agreement
        Agreement agreement1 = new Agreement(
                supplier1.getSupplierId(),
                PaymentMethod.CASH,
                LocalDate.now().minusMonths(1),
                LocalDate.now().plusYears(1),
                false // Not delivered by company
        );
        agreement1.addDeliveryDay(DaysOfTheWeek.SUNDAY);
        agreement1.addDeliveryDay(DaysOfTheWeek.WEDNESDAY);

        // Add products to Tnuva agreement
        Product milk = new Product(supplier1.getSupplierId(), 10001, 1, 10, 0.05, 5.90);
        Product cheese = new Product(supplier1.getSupplierId(), 10002, 2, 5, 0.03, 12.50);
        Product yogurt = new Product(supplier1.getSupplierId(), 10003, 3, 20, 0.1, 4.50);

        agreement1.addProduct(milk);
        agreement1.addProduct(cheese);
        agreement1.addProduct(yogurt);

        supplierService.addAgreementToSupplier(supplier1.getSupplierId(), agreement1);

        // Strauss agreement
        Agreement agreement2 = new Agreement(supplier2.getSupplierId(), PaymentTerms.CASH_ON_DELIVERY,
                now, now.plusMonths(6), true);
        agreement2.addDeliveryDay(DaysOfTheWeek.MONDAY);
        agreement2.addDeliveryDay(DaysOfTheWeek.THURSDAY);

        // Add products to Strauss agreement
        Product chocolate = new Product(supplier2.getSupplierId(), 2001, 2001, 8.90);
        chocolate.addQuantityDiscount(20, 0.06);  // 6% discount for 20+ units

        Product coffee = new Product(supplier2.getSupplierId(), 2002, 2002, 24.90);
        coffee.addQuantityDiscount(5, 0.05);   // 5% discount for 5+ units
        coffee.addQuantityDiscount(10, 0.12);  // 12% discount for 10+ units

        agreement2.addProduct(chocolate);
        agreement2.addProduct(coffee);

        supplierService.addAgreement(supplier2.getSupplierId(), agreement2);

        // Osem agreement
        Agreement agreement3 = new Agreement(supplier3.getSupplierId(), PaymentTerms.CASH_ON_DELIVERY,
                now, now.plusMonths(9), false);
        agreement3.addDeliveryDay(DaysOfTheWeek.TUESDAY);
        agreement3.addDeliveryDay(DaysOfTheWeek.FRIDAY);

        // Add products to Osem agreement
        Product pasta = new Product(supplier3.getSupplierId(), 3001, 3001, 6.50);
        pasta.addQuantityDiscount(10, 0.04);  // 4% discount for 10+ units
        pasta.addQuantityDiscount(30, 0.10);  // 10% discount for 30+ units

        Product snacks = new Product(supplier3.getSupplierId(), 3002, 3002, 9.90);
        snacks.addQuantityDiscount(15, 0.05);  // 5% discount for 15+ units

        Product canned = new Product(supplier3.getSupplierId(), 3003, 3003, 11.20);
        canned.addQuantityDiscount(12, 0.07);  // 7% discount for 12+ units

        agreement3.addProduct(pasta);
        agreement3.addProduct(snacks);
        agreement3.addProduct(canned);

        supplierService.addAgreement(supplier3.getSupplierId(), agreement3);

        // Create some orders
        Map<Integer, Integer> orderItems1 = new HashMap<>();
        orderItems1.put(milk.getProductId(), 30);  // 30 units of milk
        orderItems1.put(cheese.getProductId(), 15); // 15 units of cheese
        Order order1 = orderService.createOrder(supplier1.getSupplierId(), orderItems1, now.plusDays(3));

        Map<Integer, Integer> orderItems2 = new HashMap<>();
        orderItems2.put(chocolate.getProductId(), 25);  // 25 units of chocolate
        orderItems2.put(coffee.getProductId(), 8);      // 8 units of coffee
        Order order2 = orderService.createOrder(supplier2.getSupplierId(), orderItems2, now.plusDays(2));

        Map<Integer, Integer> orderItems3 = new HashMap<>();
        orderItems3.put(pasta.getProductId(), 40);  // 40 units of pasta
        orderItems3.put(snacks.getProductId(), 20); // 20 units of snacks
        orderItems3.put(canned.getProductId(), 15); // 15 units of canned goods
        Order order3 = orderService.createOrder(supplier3.getSupplierId(), orderItems3, now.plusDays(4));

        // Update order statuses
        orderService.updateOrderStatus(order1.getOrderId(), STATUS.IN_PROCESS);
        orderService.updateOrderStatus(order2.getOrderId(), STATUS.DONE);
    }
}