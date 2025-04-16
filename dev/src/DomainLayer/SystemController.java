package src.DomainLayer;

import src.DomainLayer.Enums.*;


import java.io.*;
import java.util.*;
import java.time.LocalDate;


public class SystemController {
    private static SystemController instance;

    // Order Management
    private Map<Integer, Order> orders;
    private int nextOrderId;
    private int nextAgreementId;

    // Supplier Management
    private Map<String, Supplier> suppliers;

    // Product Management
    private Map<String, Map<String, Product>> products; //<supplier ID, <catalogNumber, product>>

    private SystemController() {
        // Initialize order management
        this.orders = new HashMap<>();
        this.nextOrderId = 0;
        this.nextAgreementId = 0;

        // Initialize supplier management
        this.suppliers = new HashMap<>();

        // Initialize product management
        this.products = new HashMap<>();
    }

    public static SystemController getInstance() {
        if (instance == null) {
            instance = new SystemController();
        }
        return instance;
    }

    // ===================== Supplier Management =====================

    public boolean addSupplierWithDelivery(String name, String id, String bankAccount, String deliveryDays) {
        if (suppliers.containsKey(id)) return false;
        List<DaysOfTheWeek> days = parseDeliveryDays(deliveryDays);
        Supplier supplier = new SupplierWithDeliveryDays(name, id, bankAccount, days);
        suppliers.put(id, supplier);
        return true;
    }

    public boolean addSupplierNeedsPickup(String name, String id, String bankAccount, String address) {
        if (suppliers.containsKey(id)) return false;
        Supplier supplier = new SupplierNeedsPickup(name, id, bankAccount, address);
        suppliers.put(id, supplier);
        return true;
    }

    public boolean removeSupplier(String supplierID) {
        if(!suppliers.containsKey(supplierID)) return false;
        suppliers.remove(supplierID);
        products.remove(supplierID);
        return true;
    }

    public boolean updateSupplierField(String id, String field, String value) {
        Supplier supplier = suppliers.get(id);
        if (supplier == null) return false;

        switch (field.toLowerCase()) {
            case "name":
                supplier.setName(value);
                return true;

            case "supplierid":
                if (suppliers.containsKey(value)) return false;

                supplier.setSupplierId(value);
                suppliers.remove(id);
                suppliers.put(value, supplier);

                if (products.containsKey(id)) {
                    Map<String, Product> supplierProducts = products.remove(id);
                    products.put(value, supplierProducts);
                    for(Product product: supplierProducts.values())
                        product.setSupplierId(value);
                }


                return true;

            case "bankaccount":
                supplier.setBankAccount(value);
                return true;

            case "contactperson":
            case "contactpersons":
                supplier.addContactPerson(new ContactPerson(value, "0")); // ניתן לעדכן עם מספר טלפון בעתיד
                return true;

            default:
                return false;
        }
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
        for (String token : input.split(",")) {
            try {
                int dayIndex = Integer.parseInt(token.trim());
                days.add(DaysOfTheWeek.values()[dayIndex - 1]);
            } catch (Exception ignored) {}
        }
        return days;
    }

    // ===================== Product Management =====================

    public boolean addProductWithDiscounts(String supplierId, String catalogNumber, int quantityPerPackage, ArrayList<String> discountInput, double price, int unit) {
        if (!suppliers.containsKey(supplierId)) return false;
        Map<Integer, Double> discountMap = parseDiscountInput(discountInput);
        Product product = new Product(supplierId, catalogNumber, quantityPerPackage, discountMap, price, Units.values()[unit]);

        products.putIfAbsent(supplierId, new HashMap<>());
        if (products.get(supplierId).containsKey(catalogNumber)) return false;

        products.get(supplierId).put(catalogNumber, product);
        return true;
    }

    public boolean updateProductField(String supplierID, String catalogNumber, String field, String value) {
        if(!products.containsKey(supplierID))
            return false;

        Map<String, Product>  supplierProducts = products.get(supplierID);
            if (supplierProducts.containsKey(catalogNumber)) {
                Product product = supplierProducts.get(catalogNumber);

                switch (field.toLowerCase()) {
                    case "name":
                        product.setProductName(value);
                        return true;

                    case "supplierid":
                        product.setSupplierId(value);
                        return true;

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

                    case "discounts":
                        ArrayList<String> rawList = new ArrayList<>(Arrays.asList(value.split(";")));
                        Map<Integer, Double> discountMap = parseDiscountInput(rawList);
                        product.setDiscountPerPackage(discountMap);
                        return true;

                    default:
                        return false;
                }
            }


        return false; // Product not found
    }


    public List<String> getAllProducts() {
        List<String> all = new ArrayList<>();
        for (Map<String, Product> supplierProducts : products.values()) {
            for (Product p : supplierProducts.values()) {
                all.add(p.toString());
            }
        }
        return all;
    }


    /*public String findProduct(String catalogNumber) {
        int catalogKey = Integer.parseInt(catalogNumber);
        for (Map<Integer, Product> supplierProducts : products.values()) {
            if (supplierProducts.containsKey(catalogKey)) {
                return supplierProducts.get(catalogKey).toString();
            }
        }
        return null;
    }*/

    public String findProductBySupplierAndCatalog(String supplierId, String catalogNumber) {
        if (!products.containsKey(supplierId)) return null;

        try {
            int catalogKey = Integer.parseInt(catalogNumber);
            Product product = products.get(supplierId).get(catalogKey);
            return (product != null) ? product.toString() : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }



    public boolean removeProduct(String supplierID, String catalogNumber) {
        if(!products.containsKey(supplierID))
            return false;
        Map<String, Product>  supplierProducts = products.get(supplierID);
        if (!supplierProducts.containsKey(catalogNumber))
            return false;
        supplierProducts.remove(catalogNumber);

        return true;
    }

    private Map<Integer, Double> parseDiscountInput(ArrayList<String> input) {
        Map<Integer, Double> map = new HashMap<>();
        if (input == null || input.isEmpty()) return map;
        for(String pair : input)
        {
            String[] parts = pair.trim().split(",");
            if (parts.length == 2) {
                try {
                    int quantity = Integer.parseInt(parts[0].replaceAll("[^\\d]", ""));
                    double discount = Double.parseDouble(parts[1].replaceAll("[^\\d.]", "")) / 100.0;
                    map.put(quantity, discount);
                } catch (Exception ignored) {}
            }
        }
        return map;
    }

    // ===================== Persistence =====================

    public boolean saveDataSafe(String suppliersPath, String productsPath) {
        try (ObjectOutputStream sOut = new ObjectOutputStream(new FileOutputStream(suppliersPath));
             ObjectOutputStream pOut = new ObjectOutputStream(new FileOutputStream(productsPath))) {
            sOut.writeObject(suppliers);
            pOut.writeObject(products);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean loadDataSafe(String suppliersPath, String productsPath) {
        try (ObjectInputStream sIn = new ObjectInputStream(new FileInputStream(suppliersPath));
             ObjectInputStream pIn = new ObjectInputStream(new FileInputStream(productsPath))) {
            suppliers = (Map<String, Supplier>) sIn.readObject();
            products = (Map<String, Map<String, Product>>) pIn.readObject();
            return true;
        } catch (IOException | ClassNotFoundException e) {
            return false;
        }
    }

    // ===================== Reporting =====================

    public String getProductDistributionByCategory() {
        // Stub
        return "Product distribution by category is not implemented yet.";
    }

    public String getSupplierDistributionByLocation() {
        // Stub
        return "Supplier distribution by location is not implemented yet.";
    }

    public String getPriceAnalysis() {
        // Stub
        return "Price analysis is not implemented yet.";
    }

    // ===================== Order Management =====================

    public double getTotalPrice(String supplierId, Map<String, Integer> items) {
        SystemController systemController = SystemController.getInstance();
        double totalPrice = 0;
        for (Map.Entry<String, Integer> entry : items.entrySet()) {
            String catalogNumber = entry.getKey();
            Integer amount = entry.getValue();

            totalPrice += calculateProductPrice(supplierId, catalogNumber, amount);
        }
        return totalPrice;
    }

    /**
     * Calculate final price for a product considering discounts
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @param quantity Quantity to calculate price for
     * @return Calculated price or -1 if product not found
     */
    public double calculateProductPrice(String supplierId, String catalogNumber, int quantity) {
        Product product = getProduct(supplierId, catalogNumber);
        if (product != null) {
            return product.calculatePriceWithDiscount(quantity);
        }
        return -1;
    }

    /**
     * Get product by supplier ID and catalog number
     * @param supplierId ID of the supplier
     * @param catalogNumber Catalog number of the product
     * @return Product object if found, null otherwise
     */
    public Product getProduct(String supplierId, String catalogNumber) {
        if (products.containsKey(supplierId)) {
            Map<String, Product> supplierProducts = products.get(supplierId);
            return supplierProducts.get(catalogNumber);
        }
        return null;
    }

    /**
     * Get all products for a specific supplier
     * @param supplierId ID of the supplier
     * @return List of products for the supplier
     */
    public List<Product> getProductsBySupplier(String supplierId) {
        List<Product> supplierProducts = new ArrayList<>();

        if (products.containsKey(supplierId)) {
            supplierProducts.addAll(products.get(supplierId).values());
        }

        return supplierProducts;
    }

    // Create a new order
    public boolean insertOrder(String supplierId, LocalDate orderDate, LocalDate supplyDate,
                           Map<Integer, Integer> indexQuantityItems,  String contactPresonName, String contactPersonPhone, int agreementIndex, int statusIndex) {
        if (!suppliers.containsKey(supplierId)) return false;

        int orderId = nextOrderId++;
        Agreement agreement = suppliers.get(supplierId).getAgreements().get(agreementIndex);
        Map<String, Integer> items = parseIndexTOCatalogNumber(indexQuantityItems, agreement);
        double totalPrice = getTotalPrice(supplierId, items);
        ContactPerson contactPerson = new ContactPerson(contactPresonName, contactPersonPhone);
        STATUS status = STATUS.values()[statusIndex];
        Order newOrder = new Order(orderId, supplierId, orderDate, contactPerson, agreement, supplyDate, items, status, totalPrice);
        orders.put(orderId, newOrder);
        return true;
    }

    // Update order status
    public boolean updateOrderStatus(int orderId, int newStatus) {
        Order order = orders.get(orderId);
        if (order == null) return false;

        try {
            STATUS status = STATUS.values()[newStatus];
            order.setStatus(status);
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
            if (order.getStatus() == STATUS.values()[status]) {
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
        List<String> productsString = new ArrayList<String>();
        Supplier supplier = suppliers.get(supplierId);
        Agreement agreement = supplier.getAgreements().get(agreementIndex);
        List<Product> products = agreement.getProductCatalog();
        for(Product product : products) {
            productsString.add(product.toString());
        }
        return productsString;
    }


    private Map<String, Integer> parseIndexTOCatalogNumber(Map<Integer, Integer> indexQuantityItems, Agreement agreement){
        Map<String, Integer> result = new HashMap<>();
        List<Product> productList = agreement.getProductCatalog();

        for (Map.Entry<Integer, Integer> entry : indexQuantityItems.entrySet()) {
            int index = entry.getKey();
            int quantity = entry.getValue();

            if (index >= 0 && index < productList.size()) {
                Product product = productList.get(index);
                String catalogNumber = product.getCatalogNumber();
                result.put(catalogNumber, quantity);
            }
        }

        return result;
    }

    //public String getCatalogNumber(String supplierId, int productIndex, String) {}

    /*public int getAgreementIdByIndex(String supplierId, int index) {
        Supplier supplier = suppliers.get(supplierId);
        if (supplier == null) return -1;

        List<Agreement> agreements = supplier.getAgreements();
        if (index < 0 || index >= agreements.size()) return -1;

        return agreements.get(index).getAgreementId();
    }*/

}
