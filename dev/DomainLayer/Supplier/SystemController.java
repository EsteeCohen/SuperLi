package DomainLayer.Supplier;

import DomainLayer.OrderController;
import DomainLayer.Supplier.Enums.*;


import java.io.*;
import java.util.*;
import java.time.LocalDate;


public class SystemController {
    private static SystemController instance;

    // Order Management
    private Map<Integer, Order> orders;
    //private int nextOrderId;
    private int nextAgreementId;

    // Supplier Management
    private Map<String, Supplier> suppliers;

    // Product Management

    private SystemController() {
        // Initialize order management
        this.orders = new HashMap<>();
        //this.nextOrderId = 0;
        this.nextAgreementId = 0;

        // Initialize supplier management
        this.suppliers = new HashMap<>();
    }

    public static SystemController getInstance() {
        if (instance == null) {
            instance = new SystemController();
        }
        return instance;
    }

    // ===================== Supplier Management =====================

    public boolean addSupplierWithDelivery(String name, String id, String bankAccount, String deliveryDays,  List<String>  contacts) {
        if (suppliers.containsKey(id)) return false;
        List<DaysOfTheWeek> days = parseDeliveryDays(deliveryDays);

        if (days == null) return false;
        Supplier supplier = new SupplierWithDeliveryDays(name, id, bankAccount, days);
        addAllContacts(supplier, contacts);
        suppliers.put(id, supplier);
        return true;
    }

    public boolean addSupplierNeedsPickup(String name, String id, String bankAccount, String address,  List<String> contacts) {
        if (suppliers.containsKey(id)) return false;
        Supplier supplier = new SupplierNeedsPickup(name, id, bankAccount, address);
        addAllContacts(supplier, contacts);
        suppliers.put(id, supplier);
        return true;
    }

    public boolean removeSupplier(String supplierID) {
        if(!suppliers.containsKey(supplierID)) return false;
        Map<String, Product> products = this.suppliers.get(supplierID).getProductCatalog();
        suppliers.remove(supplierID);
        products.remove(supplierID);
        return true;
    }

    public boolean updateSupplierField(String id, String field, String value) {
        Supplier supplier = suppliers.get(id);
        if (supplier == null) return false;
        Map<String, Product> supplierProducts = supplier.getProductCatalog();

        switch (field.toLowerCase()) {
            case "name":
                supplier.setName(value);
                return true;

            case "supplierid":
                if (suppliers.containsKey(value)) return false;

                supplier.setSupplierId(value);
                suppliers.remove(id);
                suppliers.put(value, supplier);
                // Update the product catalog with the new supplier ID
                for (Product product : supplierProducts.values()) {
                    product.setSupplierId(value);
                }

                return true;
            case "bankaccount":
                supplier.setBankAccount(value);
                return true;
            case "contactpersons":
                return addContactPersonFromString(supplier, value);
            case "deliverydays":
                if (supplier instanceof SupplierWithDeliveryDays) {
                    List<DaysOfTheWeek> days = parseDeliveryDays(value);
                    if (days == null) return false;
                    ((SupplierWithDeliveryDays) supplier).setDeliveryDays(days);
                    return true;
                }
                return false;
            default:
                return false;
        }
    }

    private boolean addContactPersonFromString(Supplier supplier, String value) {
        String[] parts = value.split(",");
        if (parts.length != 2) return false;
        supplier.addContactPerson(new ContactPerson(parts[0].trim(), parts[1].trim()));
        return true;
    }

    public List<String> getAllSuppliers() {
        List<String> all = new ArrayList<>();
        for (Supplier supplier : suppliers.values()) {
            all.add(supplier.toString());
        }
        return all;
    }

    public String getSuppliersRequiringPickupAsString() {
        StringBuilder result = new StringBuilder();
        for (Supplier supplier : suppliers.values()) {
            if (supplier instanceof SupplierNeedsPickup) {
                result.append(supplier.toString()).append("\n");
            }
        }
        return result.toString();
    }

    private List<DaysOfTheWeek> parseDeliveryDays(String input) {
        List<DaysOfTheWeek> days = new ArrayList<>();
        if (input == null || input.isEmpty()) return days;
        for (String token : input.trim().split(",")) {
            try {
                int dayIndex = Integer.parseInt(token.trim());
                if (dayIndex < 1 || dayIndex > 7) return null;
                DaysOfTheWeek day = DaysOfTheWeek.of(dayIndex);
                days.add(DaysOfTheWeek.of(dayIndex));
            }
            catch (NumberFormatException e) {
                return null;
            }

        }
        return days;
    }

    // ===================== Product Management =====================


    public boolean updateProductField(String supplierID, String catalogNumber, String field, String value) {
        if(!suppliers.containsKey(supplierID))
            return false;

        Map<String, Product> supplierProducts = this.suppliers.get(supplierID).getProductCatalog();

            if (supplierProducts.containsKey(catalogNumber)) {
                Product product = supplierProducts.get(catalogNumber);

                switch (field.toLowerCase()) {
                    case "name":
                        product.setProductName(value);
                        return true;

                    //case "supplierid":
                    //    product.setSupplierId(value);
                    //    return true;

                    case "quantityperpackage":
                        try {
                            int quantity = Integer.parseInt(value);
                            product.setQuantityPerPackage(quantity);
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }

                    case "price":
                        try {
                            double price = Double.parseDouble(value);
                            product.setPrice(price);
                            return true;
                        } catch (NumberFormatException e) {
                            return false;
                        }

                    case "units":
                        try {
                            Units unit = Units.values()[Integer.parseInt(value)];
                            product.setUnits(unit);
                            return true;
                        } catch (IllegalArgumentException e) {
                            return false;
                        }

                    default:
                        return false;
                }
            }


        return false; // Product not found
    }

    public List<String> getAllProducts() {
        List<String> all = new ArrayList<>();
        for(Supplier supplier : suppliers.values()) {
            for(Product p : supplier.getProductCatalog().values()) {
                all.add(p.toString());
            }
        }
        return all;
    }


    public String findProductBySupplierAndCatalog(String supplierId, String catalogNumber) {
        if (!suppliers.containsKey(supplierId)) return null;

        Map<String, Product> supplierProducts = suppliers.get(supplierId).getProductCatalog();
        Product product = supplierProducts.get(catalogNumber);
        return (product != null) ? product.toString() : null;
    }


    public boolean removeProduct(String supplierID, String catalogNumber) {
        if(!suppliers.containsKey(supplierID))
            return false;
        Map<String, Product>  supplierProducts = this.suppliers.get(supplierID).getProductCatalog();
        if (!supplierProducts.containsKey(catalogNumber))
            return false;
        supplierProducts.remove(catalogNumber);

        return true;
    }


    // ===================== Order Management =====================

    public List<String> getProductsBySupplier(String supplierId) {
        List<String> supplierProducts = new ArrayList<>();

        if (suppliers.containsKey(supplierId)) {
            List<Product> productsList = new ArrayList<>(suppliers.get(supplierId).getProductCatalog().values());
            for (Product product : productsList) {
                supplierProducts.add(product.toString());
            }
        }

        return supplierProducts;
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, int newStatus) {
        Order order = orders.get(orderId);
        if (order == null) return false;

        try {
            STATUS status = STATUS.values()[newStatus-1];
            order.setStatus(status);
            if(order.getStatus() == STATUS.IN_PROCESS && order.getSupplyDate().isAfter(LocalDate.now())) {
                OrderController.getInstance().addOrder(order);
            } else if(order.getStatus() == STATUS.DELIVERED || order.getStatus() == STATUS.CANCELLED){
                OrderController.getInstance().removeOrder(order);
            }
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public List<String> getAgreementsBySupplierAsStrings(String supplierId) {
        List<String> result = new ArrayList<>();
        Supplier supplier = suppliers.get(supplierId);
        if (supplier != null) {
            for (Agreement agreement : supplier.getAgreements()) {
                result.add(agreement.toString());
            }
        }
        return result;
    }


    // Get all orders
    public List<String> getAllOrders() {
        List<String> orderStrings = new ArrayList<>();
        for (Order order : orders.values()) {
            orderStrings.add(order.toString());
        }
        return orderStrings;
    }

    // Get order by ID
    public String getOrderById(int orderId) {
        Order order = orders.get(orderId);
        return order != null ? order.toString() : null;
    }

    // Get orders by status
    public List<String> getOrdersByStatus(int status) {
        List<String> filteredOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getStatus() == STATUS.values()[status - 1]) {
                filteredOrders.add(order.toString());
            }
        }
        return filteredOrders;
    }

    // Get orders by supplier
    public List<String> getOrdersBySupplier(String supplierId) {
        List<String> filteredOrders = new ArrayList<>();
        for (Order order : orders.values()) {
            if (order.getSupplierId().equals(supplierId)) {
                filteredOrders.add(order.toString());
            }
        }
        return filteredOrders;
    }

    //Helper functions

    public boolean checkIfSupplierExists(String supplierId) {
        return suppliers.containsKey(supplierId);
    }


    public List<String> getProductsByAgreement(String supplierId, int agreementIndex) {
        Supplier supplier = suppliers.get(supplierId);
        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        Map<String, Map<Integer, Integer>> productCatalog = agreement.getProductCatalog();
        List<String> Keys = new ArrayList<>(productCatalog.keySet());
        List<String> products = new ArrayList<>();
        for(String key : Keys) {
            products.add(supplier.getProductFromCatalog(key).toString());
        }

        return products;
    }


    // parse Map<Index, Quantity> to Map<CatalogNumber, Quantity>
    private Map<String, Integer> parseIndexTOCatalogNumber(Map<Integer, Integer> indexQuantityItems, Agreement agreement){
        Map<String, Integer> result = new HashMap<>();
        Map<String, Map<Integer,Integer>> productCatalog = agreement.getProductCatalog();
        for(int index : indexQuantityItems.keySet()) {
            if (index < 0 || index >= productCatalog.size()) continue;
            String catalogNumber = productCatalog.keySet().toArray(new String[0])[index];
            result.put(catalogNumber, indexQuantityItems.get(index));
        }

        return result;
    }

    // Get all available payment methods as strings
    public List<String> getPaymentMethods() {
        List<String> methods = new ArrayList<>();
        for (PaymentMethod method : PaymentMethod.values()) {
            methods.add(method.toString());
        }
        return methods;
    }

    // Get all available payment timings as strings
    public List<String> getPaymentTimings() {
        List<String> timings = new ArrayList<>();
        for (PaymentTiming timing : PaymentTiming.values()) {
            timings.add(timing.toString());
        }
        return timings;
    }

    public boolean updateAgreementProducts(String supplierId, int agreementIndex, Map<Integer, Map<Integer,Integer>> indexProducts) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return false;

        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        if (agreement == null) return false;


        Map<String, Map<Integer, Integer>> productDiscounts = parseOrderItems(indexProducts, supplier);
        agreement.setProductCatalog(productDiscounts);
        return true;
    }

    public boolean updatePaymentMethods(String supplierId, int agreementIndex, int paymentMethodIndex) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return false;

        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        if (agreement == null) return false;

        try {
            PaymentMethod paymentMethod = PaymentMethod.values()[paymentMethodIndex];
            agreement.setPaymentMethod(paymentMethod);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean updatePaymentTiming(String supplierId, int agreementIndex, int paymentTimingIndex) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return false;

        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        if (agreement == null) return false;

        try {
            PaymentTiming paymentTiming = PaymentTiming.values()[paymentTimingIndex];
            agreement.setPaymentTiming(paymentTiming);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean updateValidFrom(String supplierId, int agreementIndex, LocalDate validFrom) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return false;

        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        if (agreement == null) return false;

        agreement.setValidFrom(validFrom);
        return true;
    }

    public boolean updateValidTo(String supplierId, int agreementIndex, LocalDate validTo) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return false;

        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        if (agreement == null) return false;

        agreement.setValidTo(validTo);
        return true;
    }

    public boolean createAgreement(String supplierId, int paymentMethod, int paymentTiming, LocalDate validFrom, LocalDate validTo, Map<Integer, Map<Integer,Integer>> IndexProducts) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return false;

        try {
            PaymentMethod method = PaymentMethod.values()[paymentMethod];
            PaymentTiming timing = PaymentTiming.values()[paymentTiming];
            Agreement agreement = new Agreement(supplierId, nextAgreementId, method, timing, validFrom, validTo);
            supplier.addAgreement(agreement);
            int serialNumber = supplier.getAgreementSerialNumber(nextAgreementId);
            updateAgreementProducts(supplierId, serialNumber, IndexProducts);
            nextAgreementId++;
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public boolean removeAgreement(String supplierId, int agreementIndex) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return false;

        List<Agreement> agreements = supplier.getAgreements();
        if (agreementIndex < 0 || agreementIndex >= agreements.size()) return false;

        agreements.remove(agreementIndex);
        return true;
    }

    public String getSupplierById(String supplierId) {
        Supplier supplier =  suppliers.get(supplierId);
        if(supplier == null) return null;
        return supplier.toString();
    }

    public Supplier getSupplierObjectById(String supplierId) {
        return suppliers.get(supplierId);
    }

    public LocalDate getValidFromOfAgreement(String supplierId, int agreementIndex) {
        LocalDate validFrom = null;
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return null;
        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        return agreement.getValidFrom();
    }

    private void addAllContacts(Supplier supplier, List<String> contactInputs) {
        for (String input : contactInputs) {
            String[] parts = input.split(",");
            if (parts.length == 2) {
                String contactName = parts[0].trim();
                String phone = parts[1].trim();
                if (!contactName.isEmpty() && !phone.isEmpty()) {
                    supplier.addContactPerson(new ContactPerson(contactName, phone));
                }
            }
        }
    }



    public boolean addProductWithDiscounts(String name, String supplierId, String catalogNumber,
                                           int quantityPerPackage, double price, int unit) {
        if (!suppliers.containsKey(supplierId)) return false;
        Supplier supplier = suppliers.get(supplierId);
        Product product = new Product(name, supplierId, catalogNumber, quantityPerPackage, price, Units.values()[unit-1]);

        return supplier.addProductToCatalog(product);
    }

    public double getTotalPrice(String supplierId, Map<String, Integer> items, int agreementIndex) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return -1;

        if (agreementIndex < 0 || agreementIndex >= supplier.getAgreements().size()) return -1;
        Agreement agreement = supplier.getAgreements().get(agreementIndex);

        Map<String, Product> supplierProducts = supplier.getProductCatalog();

        return agreement.calculateTotalPrice(items, supplierProducts);
    }

    // parse order items from the UI, from index to catalog number
    public Map<String, Map<Integer,Integer>> parseOrderItems(Map<Integer, Map<Integer, Integer>> items, Supplier supplier) {
        Map<String, Map<Integer, Integer>> parsedItems = new HashMap<>();

        for (Map.Entry<Integer, Map<Integer, Integer>> entry : items.entrySet()) {
            int index = entry.getKey();
            Map<Integer, Integer> quantities = entry.getValue();

            String catalogNumber = supplier.getCatalogNumberByIndex(index);
            parsedItems.put(catalogNumber, quantities);
        }
        return parsedItems;
    }


    public List<Supplier> getAllSupplierObjects() {
        return new ArrayList<>(suppliers.values());
    }


    // Create a new order
    public boolean insertOrder(String supplierId, LocalDate orderDate, LocalDate supplyDate,
                               Map<Integer, Integer> indexQuantityItems,  String contactPresonName, String contactPersonPhone, int agreementIndex, int statusIndex) {
        if (!suppliers.containsKey(supplierId)) return false;
        OrderController orderController = OrderController.getInstance();
        int orderId = orderController.getOrderId();
        orderController.incrementOrderId();

        Agreement agreement = suppliers.get(supplierId).getAgreements().get(agreementIndex);
        Map<String, Integer> items = parseIndexTOCatalogNumber(indexQuantityItems, agreement);
        double totalPrice = getTotalPrice(supplierId, items, agreementIndex);
        ContactPerson contactPerson = new ContactPerson(contactPresonName, contactPersonPhone);
        STATUS status = STATUS.values()[statusIndex -1];
        Order newOrder = new Order(orderId, supplierId, orderDate, contactPerson, agreement, supplyDate, items, status, totalPrice);
        orders.put(orderId, newOrder);
        if(newOrder.getStatus() == STATUS.IN_PROCESS){
            OrderController.getInstance().addOrder(newOrder);
        }
        return true;
    }

    public boolean isProductNameExistsInAgreement(String supplierId, int agreementIndex, String productName) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) {
            return false;
        }
        List<Agreement> agreements = supplier.getAgreements();
        if (agreementIndex < 0 || agreementIndex >= agreements.size()) {
            return false;
        }
        Agreement agreement = agreements.get(agreementIndex);

        Map<String, Map<Integer, Integer>> productCatalog = agreement.getProductCatalog();
        for (String catalogNumber : productCatalog.keySet()) {
            Product product = supplier.getProductFromCatalog(catalogNumber);
            if (product != null && product.getProductName() != null &&
                    product.getProductName().equalsIgnoreCase(productName.trim())) {
                return true;
            }
        }
        return false;
    }

    public void addOrder(Order order) {
        orders.put(order.getOrderId(), order);
    }


}
