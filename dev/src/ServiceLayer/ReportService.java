package src.ServiceLayer;

import src.DomainLayer.Order;
import src.DomainLayer.Supplier;
import src.DomainLayer.OrderController;
import src.DomainLayer.SupplierController;
import src.DomainLayer.Enums.STATUS;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private static ReportService instance;
    private OrderController orderController;
    private SupplierController supplierController;

    private ReportService() {
        orderController = OrderController.getInstance();
        supplierController = SupplierController.getInstance();
    }

    public static synchronized ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }

    /**
     * Generate a report of all orders within a date range
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return List of orders within the date range
     */
    public List<Order> generateOrdersReport(LocalDate startDate, LocalDate endDate) {
        List<Order> allOrders = orderController.getAllOrders();
        List<Order> ordersInRange = new ArrayList<>();

        for (Order order : allOrders) {
            LocalDate orderDate = order.getDate();
            if (!orderDate.isBefore(startDate) && !orderDate.isAfter(endDate)) {
                ordersInRange.add(order);
            }
        }

        return ordersInRange;
    }

    /**
     * Generate a report of orders by status
     * @param status Status to filter by
     * @return Map of supplier name to their orders with the given status
     */
    public Map<String, List<Order>> generateOrdersByStatusReport(STATUS status) {
        List<Order> allOrders = orderController.getAllOrders();
        Map<String, List<Order>> ordersBySupplier = new HashMap<>();

        for (Order order : allOrders) {
            if (order.getStatus() == status) {
                int supplierId = order.getSupplierId();
                Supplier supplier = supplierController.getSupplierById(supplierId);

                if (supplier != null) {
                    String supplierName = supplier.getName();
                    if (!ordersBySupplier.containsKey(supplierName)) {
                        ordersBySupplier.put(supplierName, new ArrayList<>());
                    }
                    ordersBySupplier.get(supplierName).add(order);
                }
            }
        }

        return ordersBySupplier;
    }

    /**
     * Generate a report of supplier order totals
     * @param startDate Start date of the range
     * @param endDate End date of the range
     * @return Map of supplier ID to total order value
     */
    public Map<Integer, Double> generateSupplierTotalsReport(LocalDate startDate, LocalDate endDate) {
        List<Order> ordersInRange = generateOrdersReport(startDate, endDate);
        Map<Integer, Double> supplierTotals = new HashMap<>();

        for (Order order : ordersInRange) {
            String supplierId = order.getSupplierId();
            double orderTotal = order.getTotalPrice();

            supplierTotals.put(supplierId, supplierTotals.getOrDefault(supplierId, 0.0) + orderTotal);
        }

        return supplierTotals;
    }
}