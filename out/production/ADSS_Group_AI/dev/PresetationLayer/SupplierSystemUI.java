package PresetationLayer;

import DomainLayer.Supplier.ContactPerson;
import DomainLayer.TimeController;
import ServiceLayer.SupplierSystemService;

import java.util.ArrayList;
import java.util.*;
import java.util.Map;
import java.util.Scanner;
import java.time.LocalDate;


public class SupplierSystemUI {
    private SupplierSystemService supplierSystem;
    private final Scanner scanner;

    public SupplierSystemUI() {
        this.supplierSystem = SupplierSystemService.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Supplier Management System!");

        UserRole role = selectRole();

        boolean isRunning = true;
        while (isRunning) {

            displayMenuForRole(role);
            int choice = getUserChoice();

            isRunning = handleChoice(role, choice);
        }
        scanner.close();
    }

    private UserRole selectRole() {
        System.out.println("Select your role:");
        System.out.println("1. Purchasing Manager");
        System.out.println("2. Delivery Manager");
        System.out.println("3. Regular Employee");
        System.out.println("4. Inventory Manager");
        System.out.print("Your choice: ");

        int choice = getUserChoice();
        while (choice < 1 || choice > 4) {
            System.out.println("Invalid choice. Please try again.");
            System.out.print("Your choice: ");
            choice = getUserChoice();
        }

        switch (choice) {
            case 1: return UserRole.PURCHASING_MANAGER;
            case 2: return UserRole.DELIVERY_MANAGER;
            case 3: return UserRole.REGULAR_EMPLOYEE;
            case 4: return UserRole.INVENTORY_MANAGER;
            default:
                System.out.println("Invalid choice.");
                return selectRole(); // Recursive call to select a role again
        }
    }

    private void displayMenuForRole(UserRole role) {
        switch (role) {
            case PURCHASING_MANAGER:
                displayPurchasingManagerMenu();
                break;
            case DELIVERY_MANAGER:
                displayDeliveryManagerMenu();
                break;
            case REGULAR_EMPLOYEE:
                displayRegularEmployeeMenu();
                break;
            case INVENTORY_MANAGER:
                presentation p = presentation.getInstance();
                p.run();
                break;
            default:
                System.out.println("in default.");
                break;
        }
    }

    private void displayPurchasingManagerMenu() {
        System.out.println("\n=== Purchasing Manager Menu ===");
        System.out.println("1. Add new supplier");
        System.out.println("2. Add new product");
        System.out.println("3. Remove supplier");
        System.out.println("4. Remove product");
        System.out.println("5. Update supplier");
        System.out.println("6. Update product");
        System.out.println("7. View all suppliers");
        System.out.println("8. View all products");
        System.out.println("9. Search product by catalog number and supplier ID");
        System.out.println("10. Add new agreement");
        System.out.println("11. Update agreement");
        System.out.println("12. Remove agreement");
        System.out.println("13. View agreements by supplier");
        System.out.println("14. Insert order");
        System.out.println("15. Update order status");
        System.out.println("16. View all orders");
        System.out.println("17. View order by ID");
        System.out.println("18. View orders by status");
        System.out.println("19. View orders by supplier");
        System.out.println("20. View supplier details By supplier ID");
        System.out.println("21. Create periodic order");
        System.out.println("0. Exit");
        System.out.println("press -1 for the next day");

    }

    private void displayDeliveryManagerMenu() {
        System.out.println("\n=== Delivery Manager Menu ===");
        System.out.println("1. View suppliers requiring pickup");
        System.out.println("2. Update order status");
        System.out.println("0. Exit");
        System.out.println("press -1 for the next day");

    }

    private void displayRegularEmployeeMenu() {
        System.out.println("\n=== Regular Employee Menu ===");
        System.out.println("1. View all products");
        System.out.println("2. Search product by catalog number and supplier ID");
        System.out.println("0. Exit");
        System.out.println("press -1 for the next day");

    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private boolean handleChoice(UserRole role, int choice) {
        if (choice == 0) {
            System.out.println("Exiting the system. Goodbye!");
            return false;
        }
        if (choice < 0) {
            System.out.println("Invalid choice. Please try again.");
            return true;
        }

        switch (role) {
            case PURCHASING_MANAGER:
                handlePurchasingManagerChoice(choice);
                break;
            case DELIVERY_MANAGER:
                handleDeliveryManagerChoice(choice);
                break;
            case REGULAR_EMPLOYEE:
                handleRegularEmployeeChoice(choice);
                break;
            //case INVENTORY_MANAGER:
            //    presentation p = presentation.getInstance();
              //  p.run();
            //    break;
        }
        return true;
    }

    private void handlePurchasingManagerChoice(int choice) {
        switch (choice) {
            case -1:
                TimeController.NextDay();
            case 1:
                addNewSupplier();
                break;
            case 2:
                addNewProduct();
                break;
            case 3:
                removeSupplier();
                break;
            case 4:
                removeProduct();
                break;
            case 5:
                updateSupplier();
                break;
            case 6:
                updateProduct();
                break;
            case 7:
                viewAllSuppliers();
                break;
            case 8:
                viewAllProducts();
                break;
            case 9:
                searchProductById();
                break;
            case 10:
                createAgreement();
                break;
            case 11:
                updateAgreement();
                break;
            case 12:
                removeAgreement();
                break;
            case 13:
                viewAgreementsBySupplier();
                break;
            case 14:
                insertOrder();
                break;
            case 15:
                updateOrderStatus();
                break;
            case 16:
                viewAllOrders();
                break;
            case 17:
                viewOrderById();
                break;
            case 18:
                viewOrdersByStatus();
                break;
            case 19:
                viewOrdersBySupplier();
                break;
            case 20:
                viewSupplierByID();
                break;
            case 21:
                createPeriodicOrder();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    private void handleDeliveryManagerChoice(int choice) {
        switch (choice) {
            case -1:
                TimeController.NextDay();
            case 1:
                viewSuppliersRequiringPickup();
                break;
            case 2:
                updateOrderStatus();
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }


    private void handleRegularEmployeeChoice(int choice) {
        switch (choice) {
            case -1:
                TimeController.NextDay();
            case 1:
                viewAllProducts();
                break;
            case 2:
                searchProductById();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }


    private String getSupplierIdFromUser() {
        boolean hasSuppliers = viewAllSuppliers();
        if (!hasSuppliers)
            return "";
        System.out.print("Enter supplier ID: ");
        return scanner.nextLine();
    }

    // Purchasing Manager Functions
    private void addNewSupplier() {
        boolean result = false;
        System.out.println("\n--- Add New Supplier ---");


        System.out.print("Enter supplier ID: ");
        String supplierID = scanner.nextLine();

        System.out.print("Enter supplier name: ");
        String name = scanner.nextLine();

        System.out.print("Enter bank account number: ");
        String bankAccount = scanner.nextLine();

        List<String> contacts = new ArrayList<>();
        System.out.println("You must enter at least one contact person.");

        do {
            System.out.print("Enter contact person name: ");
            String personName = scanner.nextLine();

            System.out.print("Enter contact person phone number: ");
            String phone = scanner.nextLine();
            while (!phone.matches("^0\\d{9}$")) {
                System.out.println("Invalid phone number. Please enter a 10-digit number starting with 0.");
                System.out.print("Enter contact person phone number: ");
                phone = scanner.nextLine();
            }


            contacts.add(name + ", " + phone);

            System.out.print("Add another contact person? (Y/N): ");
        } while (scanner.nextLine().equalsIgnoreCase("Y"));

        int type;
        do {
            System.out.print("Enter supplier type:\n 1. Supplier with delivery days\n 2. Supplier needs pickup\n");
            type = getUserChoice();
        } while (type != 1 && type != 2);

        if(type == 1) {
            while (!result) {
                System.out.print("Enter delivery days:\n1 - Sunday\n2 - Monday\n3 - Tuesday\n4 - Wednesday\n5 - Thursday\n6 - Friday\n7 - Saturday\n");
                System.out.print("Enter delivery days (comma-separated): ");
                String deliveryDaysInput = scanner.nextLine();
                result = supplierSystem.addSupplierWithDelivery(name, supplierID, bankAccount, deliveryDaysInput, contacts);
                if(!result) {
                    System.out.println("Invalid input. Please try again.");
                }
            }
        } else {
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            result = supplierSystem.addSupplierNeedsPickup(name, supplierID, bankAccount, address, contacts);
        }

        // Check if the supplier was added successfully
        if (result) {
            System.out.println("Supplier added successfully!");
        } else {
            System.out.println("Failed to add supplier. Supplier ID may already exist.");
        }
    }

    private void removeSupplier() {
        System.out.println("\n--- Remove Supplier ---");

        String supplierID = getSupplierIdFromUser();
        if (supplierID.equals("")) return;

        boolean result = supplierSystem.removeSupplier(supplierID);

        if (result) {
            System.out.println("Supplier removed successfully!");
        } else {
            System.out.println("Failed to remove Supplier. Supplier does not exits.");
        }
    }

    private void addNewProduct() {
        System.out.println("\n--- Add New Product ---");
        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;
        System.out.print("Enter product name: ");
        String name = scanner.nextLine();


        System.out.print("Enter product catalog number: ");
        String id = scanner.nextLine();

        int quantityPerPackage = -1;
        while(quantityPerPackage < 0) {
            System.out.print("Enter product quantity per package: ");
            quantityPerPackage = getUserChoice();
            if (quantityPerPackage <= 0)
                System.out.println("Invalid quantity.");

        }

        double price = -1;
        while (price < 0) {
            System.out.print("Enter product price: ");
            try {
                price = Double.parseDouble(scanner.nextLine());
                if (price <= 0)
                    System.out.println("Price must be greater than 0. Operation cancelled.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Operation cancelled.");
            }
        }

        int unit = -1;
        while(unit < 0 || unit > 2) {
            System.out.println("Enter Unit.\n1 - Unit\n2 - KG");
            unit = getUserChoice();
            if (unit != 1 && unit != 2) {
                System.out.println("Invalid unit. Try again.");
            }
        }



        boolean result = supplierSystem.addProduct(name, supplierId, id, quantityPerPackage, price, unit);

        if (result) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Failed to add product. Check if supplier exists and product ID is unique.");
        }
    }

    private void removeProduct() {
        System.out.println("\n--- Remove Product ---");

        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        System.out.print("Enter product catalog number: ");
        String catalogNumber = scanner.nextLine();

        boolean result = supplierSystem.removeProduct(supplierId, catalogNumber);

        if (result) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Failed to remove product. Supplier ID or product does not exist.");
        }
    }

    private void updateSupplier() {
        System.out.println("\n--- Update Supplier ---");

        String id = getSupplierIdFromUser();
        if (id.equals("")) return;

        System.out.println("Select the field you want to update. Please note: the new value will fully replace the existing one.");
        System.out.println("1. Name");
        System.out.println("2. Supplier ID");
        System.out.println("3. Bank Account");
        System.out.println("4. Contact Person");

        int choice = getUserChoice();
        String fieldToUpdate;
        String newValue;

        switch (choice) {
            case 1:
                fieldToUpdate = "name";
                System.out.print("Enter new name: ");
                newValue = scanner.nextLine();
                break;
            case 2:
                fieldToUpdate = "supplierId";
                System.out.print("Enter new supplier ID: ");
                newValue = scanner.nextLine();
                break;
            case 3:
                fieldToUpdate = "bankAccount";
                System.out.print("Enter new bank account number: ");
                newValue = scanner.nextLine();
                break;
            case 4:
                fieldToUpdate = "contactPersons";

                    System.out.print("Enter new contact person name: ");
                    String personName = scanner.nextLine();

                    System.out.print("Enter contact person phone number: ");
                    String phone = scanner.nextLine();
                    while (!phone.matches("^0\\d{9}$")) {
                        System.out.println("Invalid phone number. Please enter a 10-digit number starting with 0.");
                        System.out.print("Enter contact person phone number: ");
                        phone = scanner.nextLine();
                    }

                    newValue = personName + "," + phone;

                break;
            default:
                System.out.println("Invalid choice. Operation cancelled.");
                return;
        }



        boolean result = supplierSystem.updateSupplier(id, fieldToUpdate, newValue);

        if (result) {
            System.out.println("Supplier updated successfully!");
        } else {
            System.out.println("Failed to update supplier. Check if supplier ID exists.");
        }
    }

    private void updateProduct() {
        System.out.println("\n--- Update Product ---");

        String supplierID = getSupplierIdFromUser();
        if (supplierID.equals("")) return;

        System.out.print("Enter product catalog number to update: ");
        String catalogNumber = scanner.nextLine();

        System.out.println("Select the field you want to update. Please note: the new value will fully replace the existing one.");
        System.out.println("1. Name");
        //System.out.println("2. Supplier ID");
        System.out.println("2. Quantity Per Package");
        System.out.println("3. Price");
        System.out.println("4. Units");
        System.out.print("Your choice: ");

        int choice = getUserChoice();
        String fieldToUpdate;
        String newValue;

        switch (choice) {
            case 1:
                fieldToUpdate = "name";
                System.out.print("Enter new product name: ");
                newValue = scanner.nextLine();
                break;

            /*case 2:
                fieldToUpdate = "supplierId";
                System.out.print("Enter new supplier ID: ");
                newValue = scanner.nextLine();
                break;*/

            case 2:
                fieldToUpdate = "quantityPerPackage";
                System.out.print("Enter new quantity per package: ");
                newValue = scanner.nextLine();
                break;

            /*case 4:
                fieldToUpdate = "discounts";
                System.out.println("Enter new discounts (format: amount,discount). Enter -1 to finish:");
                ArrayList<String> discountList = new ArrayList<>();
                while (true) {
                    String input = scanner.nextLine().trim();
                    if (input.equals("-1")) break;
                    discountList.add(input.substring(0, input.length()-1));
                }
                newValue = String.join(";", discountList); // נעבד את זה בתוך SystemController
                break;*/

            case 3:
                fieldToUpdate = "price";
                System.out.print("Enter new price: ");
                newValue = scanner.nextLine();
                break;

            case 4:
                fieldToUpdate = "unit";
                int unit = -1;
                while(unit < 0 || unit > 2) {
                    System.out.println("Enter Unit.\n1 - Unit\n2 - KG");
                    unit = getUserChoice();
                    if (unit != 1 && unit != 2) {
                        System.out.println("Invalid unit. Try again.");
                    }
                }
                if (unit == 1) newValue = "Unit";
                else newValue = "KG";

                break;

            default:
                System.out.println("Invalid choice. Operation cancelled.");
                return;
        }

        boolean success = supplierSystem.updateProduct(supplierID, catalogNumber, fieldToUpdate, newValue);

        if (success) {
            System.out.println("Product updated successfully!");
        } else {
            System.out.println("Failed to update product. Please check your input.");
        }
    }

    private void viewSupplierByID(){
        System.out.println("\n--- View Supplier By ID ---");
        System.out.print("Enter supplier ID to view: ");
        String supplierID = scanner.nextLine();
        String supplierInfo = supplierSystem.getSupplierById(supplierID);
        if (supplierInfo == null)
            System.out.println("Supplier does not exist.");
        else System.out.println(supplierInfo);

    }

    private boolean viewAllSuppliers() {
        if (supplierSystem.getAllSuppliers().isEmpty()) {
            System.out.println("No suppliers found.");
            return false;
        }
        System.out.println("\n--- All Suppliers ---");
        for (String s : supplierSystem.getAllSuppliers()) {
            System.out.println(s);
        }
        return true;
    }

    private void viewAllProducts() {
        System.out.println("\n--- All Products ---");
        for (String p : supplierSystem.getAllProducts()) {
            System.out.println(p);
        }
    }

    private void createPeriodicOrder(){
        System.out.println("\n--- Add Periodic Order ---");

        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        int agreementIndex = selectAgreement(supplierId);
        if (agreementIndex < 0) return;

        // Step 3: Show products and collect items for order
        List<String> productsList = supplierSystem.getProductsByAgreement(supplierId, agreementIndex);

        if (productsList.isEmpty()) {
            System.out.println("No products found for this agreement. Operation cancelled.");
            return;
        }

        // Display available products
        System.out.println("\n--- Available Products ---");
        for (int i = 0; i < productsList.size(); i++) {
            System.out.println(i + " - " + productsList.get(i));
        }

        Map<String, Integer> productsToOrder = new HashMap<>();
        while (true) {
            System.out.print("Enter product name to add to the order (leave empty to finish): ");
            String productName = scanner.nextLine().trim();
            if (productName.isEmpty()) break;
            boolean productFound = supplierSystem.isProductNameExistsInAgreement(supplierId, agreementIndex, productName);
            if (!productFound) {
                System.out.println("Product not found. Try again.");
                continue;
            }
            System.out.print("Enter quantity for this product: ");
            int quantity;
            try {
                quantity = Integer.parseInt(scanner.nextLine().trim());
                if (quantity <= 0) {
                    System.out.println("Quantity must be positive.");
                    continue;
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid quantity.");
                continue;
            }
            productsToOrder.put(productName, quantity);
        }
        if (productsToOrder.isEmpty()) {
            System.out.println("No products selected. Operation cancelled.");
            return;
        }
        supplierSystem.addPeriodicOrders(supplierId, agreementIndex, productsToOrder);
        System.out.println("Periodic order has been created successfully.");
    }

    // order-related methods:
    private void insertOrder() {
        System.out.println("\n--- Insert Existing Order ---");


        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        int agreementIndex = selectAgreement(supplierId);
        if (agreementIndex < 0) return;

        // Step 3: Show products and collect items for order
        List<String> productsList = supplierSystem.getProductsByAgreement(supplierId, agreementIndex);

        if (productsList.isEmpty()) {
            System.out.println("No products found for this agreement. Operation cancelled.");
            return;
        }

        // Display available products
        System.out.println("\n--- Available Products ---");
        for (int i = 0; i < productsList.size(); i++) {
            System.out.println(i + " - " + productsList.get(i));
        }

        // Collect items for order
       Map<Integer,Integer> IndexProducts = selectProducts(productsList.size());
        if (IndexProducts == null) {
            return;
        }

        // Step 4: Enter contact person details
        System.out.println("\n--- Enter Contact Person Details ---");
        System.out.print("Enter contact person name: ");
        String contactPersonName = scanner.nextLine();

        System.out.print("Enter contact person phone: ");
        String contactPersonPhone = scanner.nextLine();

        // Step 5: Enter dates
        LocalDate orderDate = selectOrderDate();
        LocalDate supplyDate = selectSupplyDate(orderDate);


        // Step 6: Select order status
        int status = selectStatus();

        // Step 7: Create the order
        boolean orderCreated = supplierSystem.insertOrder(
                supplierId,
                orderDate,
                supplyDate,
                IndexProducts,
                contactPersonName,
                contactPersonPhone,
                agreementIndex,
                status
        );

        if (orderCreated) {
            System.out.println("Order created successfully!");
        } else {
            System.out.println("Failed to create order. Please check your input.");
        }

    }

    private int selectAgreement(String supplierId) {
        System.out.println("\n--- Enter the serial number of the desired agreement ---");

        List<String> agreements = supplierSystem.getAgreementsBySupplier(supplierId);

        int numberOfAgreements = agreements.size();
        int agreementIndex = -1;
        if (agreements.isEmpty()) {
            System.out.println("No agreements found for supplier ID: " + supplierId);
            return agreementIndex;
        }
        //print agreement
        System.out.println("Agreements for supplier ID " + supplierId + ":");
        int index = 0;
        for (String agreement : agreements) {
            System.out.println(index + " - " + agreement);
            index++;
        }
        while (agreementIndex< 0 || index<= agreementIndex) {
            System.out.print("Enter the serial number: ");
            agreementIndex = getUserChoice();
            if (agreementIndex < 0 || numberOfAgreements <= agreementIndex) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return agreementIndex;
    }

    private Map<Integer,Integer> selectProducts(int numProducts) {
        Map<Integer, Integer> IndexProducts = new HashMap<>();
        System.out.println("\nEnter product index and quantity (Enter -1 to finish):");

        int productIndex = 0;
        while (productIndex != -1) {
            System.out.print("Enter product index (-1 to finish): ");
            try {
                productIndex = Integer.parseInt(scanner.nextLine());

                if (productIndex == -1) {
                    // User finished product selection
                    break;
                }
                //IndexProducts.add(productIndex);
                if (productIndex < 0 || productIndex >= numProducts) {
                    System.out.println("Invalid product index. Please try again.");
                    continue;
                }

                System.out.print("Enter quantity: ");
                int quantity;
                try {
                    quantity = Integer.parseInt(scanner.nextLine());
                    if (quantity <= 0) {
                        System.out.println("Quantity must be greater than 0. Please try again.");
                        continue;
                    }
                    //quantitiesList.add(quantity);
                } catch (NumberFormatException e) {
                    System.out.println("Invalid quantity. Please try again.");
                    continue;
                }

                IndexProducts.put(productIndex, quantity);

                System.out.println("Product added to order. Current items: " + IndexProducts.size());
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        if (IndexProducts.isEmpty()) {
            return null;
        }
        return IndexProducts;
    }

    private LocalDate selectOrderDate() {
        System.out.println("\n--- Enter Order Dates ---");
        LocalDate orderDate = LocalDate.now(); // Default to current date

        System.out.println("Do you want to use current date (" + orderDate + ") as order date? (Y/N)");
        String dateChoice = scanner.nextLine();

        if (dateChoice.equalsIgnoreCase("N")) {
            boolean flag = false;
            while (!flag) {
                System.out.print("Enter order date (YYYY-MM-DD): ");
                try {
                    orderDate = LocalDate.parse(scanner.nextLine());
                    flag = true;
                } catch (Exception e) {
                    System.out.println("Invalid date format. Using current date.");
                    flag = false;
                }
            }
        }
        return orderDate;
    }

    private LocalDate selectSupplyDate(LocalDate orderDate) {
        LocalDate supplyDate = null;
        boolean flag = false;
        while (!flag) {
            System.out.print("Enter supply date (YYYY-MM-DD): ");
            try {
                supplyDate = LocalDate.parse(scanner.nextLine());
                if (supplyDate.isBefore(orderDate)) {
                    System.out.println("Supply date is before order date.");
                }else{
                    flag = true;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. please enter a valid date.");
            }
        }
        return supplyDate;
    }

    private int selectStatus(){
        System.out.println("\n--- Select Order Status ---");
        System.out.println("1. DELIVERED");
        System.out.println("2. IN_PROCESS");
        System.out.println("3. CANCELLED");

        int status = getUserChoice();
        while (status < 1 || status > 3) {
            System.out.println("Invalid choice. Please select a valid status (1-3):");
            status = getUserChoice();
        }
        return status;
    }

    private void updateOrderStatus() {
        System.out.println("\n--- Update Order Status ---");
        int orderId = getOrderIdFromUser();
        String orderInfo = supplierSystem.getOrderById(orderId);
        if (orderInfo == null) {
            System.out.println("Order not found. Operation cancelled.");
            return;
        }

        System.out.println("Current order details:");
        System.out.println(orderInfo);
        int newStatus = selectStatus();

        boolean success = supplierSystem.updateOrderStatus(orderId, newStatus);

        if (success) {
            System.out.println("Order status updated successfully!");
        } else {
            System.out.println("Failed to update order status.");
        }
    }

    private void viewAllOrders() {
        System.out.println("\n--- All Orders ---");
        List<String> orders = supplierSystem.getAllOrders();

        if (orders.isEmpty()) {
            System.out.println("No orders found.");
        } else {
            for (String order : orders) {
                System.out.println(order);
            }
        }
    }

    private void viewOrderById() {
        System.out.println("\n--- View Order by ID ---");

        String orderInfo = supplierSystem.getOrderById(getOrderIdFromUser());
        if (orderInfo != null) {
            System.out.println(orderInfo);
        } else {
            System.out.println("Order not found.");
        }
    }

    private int getOrderIdFromUser(){
        System.out.print("Enter order ID: ");
        int orderId = -1;
        try {
            orderId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid order ID. Please enter a valid order ID.");
            getOrderIdFromUser();
        }

        return orderId;
    }

    private void viewOrdersByStatus() {
        System.out.println("\n--- View Orders by Status ---");

        System.out.println("Select status to view:");
        System.out.println("1. DELIVERED (done orders)");
        System.out.println("2. IN_PROCESS (orders in progress)");
        System.out.println("3. CANCELLED (cancelled orders)");

        int status = getUserChoice();

        List<String> orders = supplierSystem.getOrdersByStatus(status);

        if (orders.isEmpty()) {
            System.out.println("No orders found with status: " + status);
        } else {
            System.out.println("Orders with status " + status + ":");
            for (String order : orders) {
                System.out.println(order);
            }
        }
    }

    private void viewOrdersBySupplier() {
        System.out.println("\n--- View Orders by Supplier ---");

        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        // Check if supplier exists
        boolean supplierExists = supplierSystem.checkIfSupplierExists(supplierId);

        if (!supplierExists) {
            System.out.println("Supplier not found. Operation cancelled.");
            return;
        }

        List<String> orders = supplierSystem.getOrdersBySupplier(supplierId);

        if (orders.isEmpty()) {
            System.out.println("No orders found for supplier ID: " + supplierId);
        } else {
            System.out.println("Orders for supplier ID " + supplierId + ":");
            for (String order : orders) {
                System.out.println(order);
            }
        }
    }

    private void viewAgreementsBySupplier() {
         System.out.println("\n--- Agreements by Supplier ---");
        // Get supplier ID
        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        // Check if supplier exists
        boolean supplierExists = supplierSystem.checkIfSupplierExists(supplierId);
        if (!supplierExists) {
            System.out.println("Supplier not found. Operation cancelled.");
            return;
        }
        showAgreementBySupplier(supplierId);
    }

    //Agreement Functions
    private void createAgreement() {
        System.out.println("\n--- Create New Agreement ---");

        // Step 1: Select supplier

        String supplierId = getSupplierIdFromUser();
        if (supplierId == "") return;
        if (!supplierSystem.checkIfSupplierExists(supplierId)) {
            System.out.println("Supplier not found. Operation cancelled.");
            return;
        }

        // Step 2: Select payment method and timing
        int paymentMethodIndex = getPaymentMethodsFromUser();
        int paymentTimingIndex = getPaymentTimingFromUser();

        // Step 3: Set agreement validity dates
        System.out.println("\n--- Set Agreement Validity Period ---");
        LocalDate validFrom = getValidFrom();
        LocalDate validTo = getValidTo(validFrom);


        // Step 4: Show available products and let user select them

        //Set<Integer> IndexProducts =new HashSet<Integer>(updateAgreementProducts(supplierId));
        Map<Integer, Map<Integer,Integer>> IndexProducts = updateAgreementProducts(supplierId);

        // Step 6: Create the agreement
        boolean agreementCreated = supplierSystem.createAgreement(
                supplierId,
                paymentMethodIndex,
                paymentTimingIndex,
                validFrom,
                validTo,
                IndexProducts
        );

        if (agreementCreated) {
            System.out.println("Agreement created successfully!");
        } else {
            System.out.println("Failed to create agreement. Please check your input.");
        }
    }

    private void updateAgreement() {
        System.out.println("\n--- Update Agreement ---");

        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        int agreementIndex = -1;
        int size = showAgreementBySupplier(supplierId);
        if (size == 0) return;
        do {
            System.out.println("Select serial number of agreement to update: (0 to "+ size +")");
            agreementIndex = getUserChoice();
        }while(agreementIndex < 0 || agreementIndex >= size);

        System.out.println("Select the field you want to update. Please note: the new value will fully replace the existing one.");
        System.out.println("1. Payment Method");
        System.out.println("2. Payment Timing");
        System.out.println("3. Valid From");
        System.out.println("4. Valid To");
        System.out.println("5. Products");
        System.out.print("Your choice: ");

        int choice = getUserChoice();
        boolean success = false;
        switch (choice) {
            case 1:
                int paymentMethodIndex = getPaymentMethodsFromUser();
                success = supplierSystem.updatePaymentMethods(supplierId, agreementIndex, paymentMethodIndex);
                break;
            case 2:
                int paymentTimingIndex = getPaymentTimingFromUser();
                success = supplierSystem.updatePaymentTiming(supplierId, agreementIndex, paymentTimingIndex);
                break;
            case 3:
                LocalDate validFrom = getValidFrom();
                success = supplierSystem.updateValidFrom(supplierId, agreementIndex, validFrom);
                break;
            case 4:
                LocalDate validTo = getValidTo(supplierSystem.getValidFromOfAgreement(supplierId, agreementIndex));
                success = supplierSystem.updateValidTo(supplierId, agreementIndex, validTo);
                break;
            case 5:
                System.out.println("\n--- Update Agreement Products ---");
                Map<Integer, Map<Integer,Integer>> IndexProducts = updateAgreementProducts(supplierId);
                if (IndexProducts == null) {
                    success = false;
                }else {
                    success = supplierSystem.updateAgreementProducts(supplierId, agreementIndex, IndexProducts);
                }
                break;
            default:
                System.out.println("Invalid choice. Operation cancelled.");
                return;
        }

        if (success) {
            System.out.println("Agreement updated successfully!");
        } else {
            System.out.println("Failed to update agreement.");
        }

    }

    private void removeAgreement() {
        System.out.println("\n--- Remove Agreement ---");

        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        if(showAgreementBySupplier(supplierId) == 0) return;

        System.out.print("Enter agreement ID: ");
        int agreementIndex = getUserChoice();

        boolean result = supplierSystem.removeAgreement(supplierId, agreementIndex);

        if (result) {
            System.out.println("Agreement removed successfully!");
        } else {
            System.out.println("Failed to remove agreement. Supplier ID or agreement does not exist.");
        }
    }

    // Delivery Manager Function
    private void viewSuppliersRequiringPickup() {
        System.out.println("\n--- Suppliers Requiring Pickup ---");
        String suppliers = supplierSystem.getSuppliersRequiringPickup();
        System.out.println(suppliers);
    }


    // Regular Employee Functions
    private void searchProductById() {
        System.out.println("\n--- Search Product by catalog number and supplier ID ---");
        System.out.print("Enter catalog number: ");
        String catalogNum = scanner.nextLine();

        String supplierId = getSupplierIdFromUser();
        if (supplierId.equals("")) return;

        String productInfo = supplierSystem.getProductBySupplierAndCatalog(supplierId, catalogNum);

        if (productInfo != null && !productInfo.isEmpty()) {
            System.out.println(productInfo);
        } else {
            System.out.println("Product not found.");
        }
    }

    //helper Functions
    private LocalDate getValidTo(LocalDate validFrom) {
        LocalDate validTo = null;
        boolean flag = false;
        do {
            try {
                System.out.println("Enter valid to date (YYYY-MM-DD): ");
                System.out.println("Enter 0 to initialize the date to one year after the 'valid from' date.");
                String ans = scanner.nextLine();
                if (ans.equals("0"))
                    validTo = validFrom.plusYears(1);
                else
                    validTo = LocalDate.parse(ans);
                flag = true;
                if(validFrom.isAfter(validTo))
                {
                    System.out.println("The date you entered is before the 'from' date.");
                    flag = false;
                }
            } catch (Exception e) {
                System.out.println("Invalid date format. Setting valid from date to current date.");
            }
        } while (!flag);
        return validTo;
    }

    private LocalDate getValidFrom() {

        LocalDate validFrom = null;
        boolean flag = false;
        do {
            try {
                System.out.println("Enter valid from date (YYYY-MM-DD): ");
                System.out.println("Enter 0 to initialize the date to today.");
                String ans = scanner.nextLine();
                if (ans.equals("0"))
                    validFrom = LocalDate.now();
                else
                    validFrom = LocalDate.parse(ans);
                flag = true;
            } catch (Exception e) {
                System.out.println("Invalid date format. Setting valid from date to current date.");
            }
        } while (!flag);
        return validFrom;
    }

    private Map<Integer, Map<Integer,Integer>>  updateAgreementProducts(String supplierId) {

        Map<Integer, Map<Integer,Integer>> productsMap = new HashMap<>();//<INDEX, <PRODUCT_QUANTITY, DISCOUNT>>
        List<String> products = supplierSystem.getProductsBySupplier(supplierId);

        if (products.isEmpty()) {
            System.out.println("No products found for this supplier. Operation cancelled.");
            return new HashMap<>();
        }

        // Display available products
        System.out.println("\n--- Available Products ---");
        for (int i = 0; i < products.size(); i++) {
            System.out.println(i + " - " + products.get(i));
        }

        // Collect items for order
        List<Integer> IndexProducts = new ArrayList<>();
        int productIndex = 0;
        while (productIndex != -1) {
            System.out.print("Enter product index (-1 to finish): ");
            try {
                productIndex = Integer.parseInt(scanner.nextLine());

                if (productIndex == -1) break;
                if (productIndex < 0 || productIndex >= products.size()) {
                    System.out.println("Invalid product index. Please try again.");
                }
                else {
                    Map<Integer,Integer> contractQuantities = getContractQuantitiesFromUser();
                    productsMap.put(productIndex, contractQuantities);
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        return productsMap;
    }

    private Map<Integer,Integer> getContractQuantitiesFromUser (){
        Map<Integer,Integer> contractQuantities = new HashMap<>();
        System.out.println("Enter discounts for quantity if available (contract quantities):");
        System.out.println("Enter -1 to skip or finish entering discounts.");
        while (true) {
            System.out.print("Enter quantity for discount (-1 to finish): ");
            int discountQuantity = getUserChoice();

            if (discountQuantity == -1) {
                break;
            }

            if (discountQuantity <= 0) {
                System.out.println("Quantity must be greater than 0. Please try again.");
                continue;
            }

            System.out.print("Enter discount percentage for quantity " + discountQuantity + ": ");
            int discountPercentage = getUserChoice();

            if (discountPercentage < 0 || discountPercentage > 100) {
                System.out.println("Discount percentage must be between 0 and 100. Please try again.");
                continue;
            }
            contractQuantities.put(discountQuantity, discountPercentage);
        }
        return contractQuantities;
    }

    private int getPaymentMethodsFromUser() {
        List<String> paymentMethods = supplierSystem.getPaymentMethods();
        System.out.println("\n--- Available Payment Methods ---");
        for (int i = 0; i < paymentMethods.size(); i++) {
            System.out.println(i + " - " + paymentMethods.get(i));
        }

        int selection = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter payment method number (0-" + (paymentMethods.size() - 1) + "): ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection >= 0 && selection < paymentMethods.size()) {
                    validInput = true;
                } else {
                    System.out.println("Invalid selection. Please enter a number between 0 and "
                            + (paymentMethods.size() - 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        return selection;
    }

    private int getPaymentTimingFromUser() {
        List<String> paymentTimings = supplierSystem.getPaymentTimings();
        System.out.println("\n--- Available Payment Timings ---");
        for (int i = 0; i < paymentTimings.size(); i++) {
            System.out.println(i + " - " + paymentTimings.get(i));
        }

        int selection = -1;
        boolean validInput = false;

        while (!validInput) {
            System.out.print("Enter payment timing number (0-" + (paymentTimings.size() - 1) + "): ");
            try {
                selection = Integer.parseInt(scanner.nextLine());
                if (selection >= 0 && selection < paymentTimings.size()) {
                    validInput = true;
                } else {
                    System.out.println("Invalid selection. Please enter a number between 0 and "
                            + (paymentTimings.size() - 1));
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number.");
            }
        }

        return selection;
    }


    private int showAgreementBySupplier(String supplierId){
        List<String> agreements = supplierSystem.getAgreementsBySupplier(supplierId);

        if (agreements.isEmpty()) {
            System.out.println("No agreements found for supplier ID \"" + supplierId +"\"");
            return 0;
        }

        System.out.println("Agreements for supplier ID " + supplierId + ":");
        int index = 0;
        for (String agreement : agreements) {
            System.out.println(index + " - " + agreement);
            index++;
        }
        return agreements.size();
    }


}

