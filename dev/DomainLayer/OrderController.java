package DomainLayer;

import DomainLayer.Inventory.ProductFacade;
import DomainLayer.Inventory.ReportFacade;
import DomainLayer.Supplier.*;
import DomainLayer.Supplier.Enums.STATUS;
import DomainLayer.Supplier.Repositories.OrderRepository;
import ServiceLayer.SupplierSystemService;
//import org.junit.jupiter.api.parallel.Execution;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderController {
    /*Dependencies injected via constructor*/
    private final ProductFacade productFacade;
    private final ReportFacade reportFacade;
    private List<Order> orders;
    private int nextOrderId;
    private Map<Integer, Order>  periodicOrders;
    private final int PERIODIC_ORDER_DAYS = 7;
    private final OrderRepository orderRepository;


    private static OrderController instance;

    public static OrderController getInstance() {
        if (instance == null)
            instance = new OrderController();
        return instance;
    }

    public void inistializeOrders(List<Order> orders) {
        int maxId = 0;
        if(orders == null || orders.isEmpty()) return;
        for(Order order : orders) {
            if(order.getSupplyDate().isAfter(LocalDate.now())) {
                this.orders.add(order);
            }
             if (order.getOrderId() > maxId) {
                 maxId = order.getOrderId();
             }
        }
        nextOrderId = maxId + 1;
    }

    /**
     * should be used if decide to load default data later in the run
     * wipes out the previous run in order to allow to safely load default data
     */
    private OrderController() {
        this.productFacade = ProductFacade.getInstance();
        this.reportFacade = ReportFacade.getInstance();
        this.orders = new ArrayList<Order>();
        this.nextOrderId = 0;
        this.periodicOrders = new HashMap<>();
        try {
            this.orderRepository = new OrderRepository();
        } catch (Exception e) {
            throw new RuntimeException("Failed to initialize OrderRepository: " + e.getMessage());
        }
    }

    /**
     * Declare that a specific product is below its minimum
     * stock level (manual or automatic trigger).
     *
     * @param productName  logical product key in inventory
     * @param missingUnits how many units are required to restore
     *                     stock to the defined minimum
     */
    public void reportStockShortage(String productName, int missingUnits) {
        System.out.println("Reporting stock shortage for product: " + productName + ", missing units: " + missingUnits);
        Map<String, Object> supplierANDAgreement = chooseBestSupplierAndAgreement(productName, missingUnits);
        if (supplierANDAgreement.isEmpty()) {
            return;
        }
        String supplierId = supplierANDAgreement.get("supplierId").toString();
        int agreementId = (int) supplierANDAgreement.get("agreementId");
        Order order = checkOpenOrder(supplierId, agreementId);
        if (order == null) {
            createAutomaticShortageOrders(supplierId, agreementId, productName, missingUnits);
        } else {
            updatePeriodicOrders(order, productName, missingUnits);
        }
    }
    public Order checkOpenOrder(String supplierID, int agreement){
        for(Order order : orders){
            if(order.getSupplierId().equals(supplierID) && order.getAgreement().getAgreementId() == agreement){
                return order;
            }
        }
        return null;
    }
    /**
     * Decide which supplier can satisfy the requested quantity
     * for the given product at the best total price, according to
     * current agreements and contract quantities.
     *
     * @param productName      logical product key
     * @param requiredQuantity units to order
     * @return supplierId      identifier of the chosen supplier
     */
    public Map<String, Object> chooseBestSupplierAndAgreement(String productName, int requiredQuantity) {
        Map<String, Object> result = new HashMap<>();
        double bestPrice = Double.MAX_VALUE;
        String bestSupplierId = null;
        int bestAgreementId = -1;

        for (Supplier supplier : SystemController.getInstance().getAllSupplierObjects()) {
            Map<String, Object> supplierData = supplier.getBestPriceAndAgreementForProductName(productName, requiredQuantity);

            if (supplierData.isEmpty() ||
                    !supplierData.containsKey("price") ||
                    !supplierData.containsKey("agreementId")) {
                continue;
            }

            double supplierPrice = (double) supplierData.get("price");
            int agreementId = (int) supplierData.get("agreementId");

            if (agreementId != -1 && supplierPrice < bestPrice) {
                bestPrice = supplierPrice;
                bestSupplierId = supplier.getSupplierId();
                bestAgreementId = agreementId;
            }
        }

        if (bestSupplierId != null && bestAgreementId != -1) {
            result.put("supplierId", bestSupplierId);
            result.put("agreementId", bestAgreementId);
        }

        return result;
    }

    /**
     * Notify the system that a previously issued order has
     * physically arrived.
     *
     * @param actualArrivalDate real arrival date at warehouse
     */
    public void confirmOrderArrival(LocalDate actualArrivalDate) {
        SystemController systemController = SystemController.getInstance();
        try {
            List<Order> ordersArrival = getOrdersArrivingOn(actualArrivalDate);
            if (ordersArrival.isEmpty()) return;
            for (Order order : ordersArrival) {
                Supplier supplier = systemController.getSupplierObjectById(order.getSupplierId());
                Map<String, Integer> items = order.getItems();
                Agreement agreement = supplier.getAgreements().get(order.getAgreement().getAgreementId());

                for (Map.Entry<String, Integer> item : items.entrySet()) {
                    Product product = supplier.getProductNameByCatalogNumber(item.getKey());
                    String productName = product.getProductName();
                    double cost = agreement.calculatePriceWithDiscount(product, item.getValue())/ item.getValue();
                    LocalDate expirationDate = order.getSupplyDate().plusDays(7);
                    productFacade.addSupply(productName, cost, expirationDate, 0, item.getValue());
                    order.setStatus(STATUS.DELIVERED);
                    orderRepository.updateOrderStatus(order.getOrderId(), order.getStatus().toString());
                }
                order.setStatus(STATUS.DELIVERED);
                orders.remove(order);
                if(periodicOrders.containsKey(order.getOrderId())){
                    Order periodicOrder = periodicOrders.get(order.getOrderId());
                    periodicOrder.setDeliveryDate(actualArrivalDate.plusDays(PERIODIC_ORDER_DAYS));
                    orderRepository.updateDeliveryDate(periodicOrder.getOrderId(), periodicOrder.getSupplyDate());
                    Order newPeriodicOrder = new Order(periodicOrder.getOrderId(), periodicOrder.getSupplierId(), periodicOrder.getSupplyDate(), periodicOrder.getContactPerson(), periodicOrder.getAgreement(), periodicOrder.getSupplyDate(), periodicOrder.getItems(), periodicOrder.getStatus(), periodicOrder.getTotalPrice());
                    orders.add(newPeriodicOrder);
                    systemController.addOrder(newPeriodicOrder);
                    orderRepository.addOrder(newPeriodicOrder, false);

                }
            }
        } catch (Exception e) {
            System.out.println (e.toString());
        }
    }

    /**
     * Retrieve every order whose scheduled supply date equals
     * the given date (e.g., "today").
     *
     * @param date a calendar day to check
     * @return list of Order domain entities
     */
    public List<Order> getOrdersArrivingOn(LocalDate date) {
        List<Order> result = new ArrayList<>();
        for (Order order : orders) {
            if (order.getSupplyDate().equals(date)) {
                result.add(order);
            }
        }
        return result;
    }



    /**
     * Automatically raise a purchase order for each product
     * whose projected stock (until next delivery) falls below
     * its minimum level.  Aggregates shortages discovered in
     * the latest Abscence/Damage/Expiry reports.
     */
    public Order createAutomaticShortageOrders(String supplierId, int agreementId, String productName, int requiredQuantity) {
        SystemController systemController = SystemController.getInstance();
        LocalDate orderDate = LocalDate.now();
        Supplier supplier = systemController.getSupplierObjectById(supplierId);
        if (supplier == null) return null;
        LocalDate supplyDate = supplier.getClosestSupplyDate(orderDate);

        int orderId = nextOrderId++;
        Agreement agreement = supplier.getAgreements().get(agreementId);
        Map<String, Integer> items = new HashMap<>();
        String catalogNumber = supplier.findProductByName(productName).getCatalogNumber();
        items.put(catalogNumber, requiredQuantity);
        double totalPrice = getTotalPrice(supplier, items, agreementId);
        ContactPerson contactPerson = supplier.getContactPersons().get(0);
        STATUS status = STATUS.IN_PROCESS;
        Order newOrder = new Order(orderId, supplierId, orderDate, contactPerson, agreement, supplyDate, items, status, totalPrice);
        orders.add(newOrder);
        System.out.println("Order created: " + newOrder);
        systemController.addOrder(newOrder);
        orderRepository.addOrder(newOrder, false);


        return newOrder;

    }

    //Map<String, Integer> products = new HashMap<>(); <nameProduct, quantity>
    public void addPeriodicOrders(String supplierId, int agreementId, Map<String, Integer> products){
        if(products == null || products.isEmpty()) return;
        Order order = null;
        for(Map.Entry<String, Integer> entry : products.entrySet()) {
            String productName = entry.getKey();
            int quantity = entry.getValue();
            if(order == null)
                order = createAutomaticShortageOrders(supplierId, agreementId, productName, quantity);
            else
                order.addItem(productName, quantity);
        }

        Order periodicOrder = new Order(order.getOrderId(), order.getSupplierId(), order.getSupplyDate(), order.getContactPerson(), order.getAgreement(), order.getSupplyDate(), order.getItems(), order.getStatus(), order.getTotalPrice());
        periodicOrders.put(periodicOrder.getOrderId(), periodicOrder);
        orderRepository.addOrder(order, true);
    }

    public void addPeriodicOrders(Order order) {
        if(order == null) return;
        periodicOrders.put(order.getOrderId(), order);
    }

    public double getTotalPrice(Supplier supplier, Map<String, Integer> items, int agreementIndex) {
        if (supplier == null) return -1;

        if (agreementIndex < 0 || agreementIndex >= supplier.getAgreements().size()) return -1;
        Agreement agreement = supplier.getAgreements().get(agreementIndex);

        Map<String, Product> supplierProducts = supplier.getProductCatalog();

        return agreement.calculateTotalPrice(items, supplierProducts);
    }


    /**
     * Generate or refresh standing (periodic) orders one day
     * before each supplier’s fixed delivery day, ensuring that
     * projected stock after the delivery will be above minimum.
     */
    public void updatePeriodicOrders(Order order, String productName, int requiredQuantity) {
        Map<String, Integer> items = new HashMap<>();
        Supplier supplier = SystemController.getInstance().getSupplierObjectById(order.getSupplierId());
        if (supplier == null) return;
        String catalogNumber = supplier.findProductByName(productName).getCatalogNumber();
        items.put(catalogNumber, requiredQuantity);
        order.setItems(items);
        orderRepository.addProductToOrder(order.getOrderId(), productName, requiredQuantity);
    }

    /**
     * Recalculate inventory quantities and release updated
     * reports after an order is confirmed as delivered.
     *
     * @param orderId id of the delivered order
     */
    public void reconcileStockAfterArrival(int orderId)
    {

    }

    /**
     * Helper used internally to build the item‑quantity map
     * for a given shortage batch keyed by catalog number.
     *
     * @param shortages map<productName, missingUnits>
     * @return Map<catalogNumber, quantityPerCatalog>
     */
    private Map<String,Integer> mapShortagesToCatalog(Map<String,Integer> shortages)
    {
        return null;
    }

    public void incrementOrderId() {
        nextOrderId++;
    }

    public int getOrderId() {
        return nextOrderId;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        orders.remove(order);
    }
}



