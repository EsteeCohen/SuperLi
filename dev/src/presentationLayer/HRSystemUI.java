import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class HRSystemUI {

    private HRSystemUIService supplierSystem;
    private final Scanner scanner;

    public HRSystemUI() {
        this.supplierSystem = HRSystemUIService.getInstance();
        this.scanner = new Scanner(System.in);
    }

    public void start() {
        System.out.println("Welcome to the Human Resource System!");
        
        UserRole role = selectRole();

        boolean isRunning = true;
        while (isRunning) {

            displayMenuForRole(role);
            int choice = getUserChoice();

            isRunning = handleChoice(role, choice);
        }
        scanner.close();
    }

    private UserRole login() {

        System.out.print("Enter your ID: ");
        String id = scanner.nextLine();
        System.out.print("Enter your password: ");
        String password = scanner.nextLine();

        boolean success = HRSystemUIService.login(id, password);
        if (success) {
            System.out.println("Login successful!");
            // ניתוב למסך הבא לפי תפקיד המשתמש
        } else {
            System.out.println("Login failed. Please try again.");
            return selectRole(); // Recursive call to select role again
        }

        System.out.println("Select your role:");
        System.out.println("1. Purchasing Manager");
        System.out.println("2. Delivery Manager");
        //System.out.println("3. General Manager");
        System.out.println("3. Regular Employee");
        System.out.print("Your choice: ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1: return UserRole.PURCHASING_MANAGER;
            case 2: return UserRole.DELIVERY_MANAGER;
            //case 3: return UserRole.GENERAL_MANAGER;
            case 3: return UserRole.REGULAR_EMPLOYEE;
            default:
                System.out.println("Invalid choice.");
                return selectRole(); // Recursive call to select role again
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
            /*case GENERAL_MANAGER:
                displayGeneralManagerMenu();
                break;*/
            case REGULAR_EMPLOYEE:
                displayRegularEmployeeMenu();
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
        System.out.println("10. Insert order");
        System.out.println("11. Update order status");
        System.out.println("12. View all orders");
        System.out.println("13. View order by ID");
        System.out.println("14. View orders by status");
        System.out.println("15. View orders by supplier");
        System.out.println("0. Exit");
        // Call appropriate functions here
    }

    private void displayDeliveryManagerMenu() {
        System.out.println("\n=== Delivery Manager Menu ===");
        System.out.println("1. View suppliers requiring pickup");
        //System.out.println("2. Mark supplier as picked up");
        System.out.println("0. Exit");
    }

    /*private void displayGeneralManagerMenu() {
        System.out.println("\n=== General Manager Menu ===");
        System.out.println("1. Load data from files");
        System.out.println("2. Save data to files");
        System.out.println("3. Analyze reports");
        System.out.println("4. View all system data");
        System.out.println("0. Exit");
    }*/

    private void displayRegularEmployeeMenu() {
        System.out.println("\n=== Regular Employee Menu ===");
        System.out.println("1. View all products");
        System.out.println("2. Search product by catalog number and supplier ID");
        System.out.println("0. Exit");
    }

    private int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Invalid value will cause an error message in the main menu
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
            /*case GENERAL_MANAGER:
                handleGeneralManagerChoice(choice);
                break;*/
            case REGULAR_EMPLOYEE:
                handleRegularEmployeeChoice(choice);
                break;
        }
        return true;
    }

    private void handlePurchasingManagerChoice(int choice) {
        switch (choice) {
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
                insertOrder();
                break;
            case 11:
                updateOrderStatus();
                break;
            case 12:
                viewAllOrders();
                break;
            case 13:
                viewOrderById();
                break;
            case 14:
                viewOrdersByStatus();
                break;
            case 15:
                viewOrdersBySupplier();
                break;

            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    private void handleDeliveryManagerChoice(int choice) {
        switch (choice) {
            case 1:
                viewSuppliersRequiringPickup();
                break;
            /*case 2:
                markSupplierAsPickedUp();
                break;*/
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    /*private void handleGeneralManagerChoice(int choice) {
        switch (choice) {
            case 1:
                loadDataFromFiles();
                break;
            case 2:
                saveDataToFiles();
                break;
            case 3:
                analyzeReports();
                break;
            case 4:
                viewAllSystemData();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }*/

    private void handleRegularEmployeeChoice(int choice) {
        switch (choice) {
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
        System.out.print("Enter supplier ID: ");
        return scanner.nextLine();
    }

    // Purchasing Manager Functions
    private void addNewSupplier() {
        boolean result = false;
        System.out.println("\n--- Add New Supplier ---");

        String supplierID = getSupplierIdFromUser();

        System.out.print("Enter supplier name: ");
        String name = scanner.nextLine();

        System.out.print("Enter bank account number: ");
        String bankAccount = scanner.nextLine();
        int type;
        do {
            System.out.print("Enter supplier type:\n 1. Supplier with delivery days\n 2. Supplier needs pickup\n");
            type = getUserChoice();
        } while (type != 1 && type != 2);

        if(type == 1) {
            System.out.print("Enter delivery days:\n1 - Sunday\n2 - Monday\n3 - Tuesday\n4 - Wednesday\n5 - Thursday\n6 - Friday\n7 - Saturday\n");
            System.out.print("Enter delivery days (comma-separated): ");
            String deliveryDaysInput = scanner.nextLine();
            result = supplierSystem.addSupplierWithDelivery(name, supplierID, bankAccount, deliveryDaysInput);
        } else {
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            result = supplierSystem.addSupplierNeedsPickup(name, supplierID, bankAccount, address);
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

        boolean result = supplierSystem.removeSupplier(supplierID);

        if (result) {
            System.out.println("Supplier removed successfully!");
        } else {
            System.out.println("Failed to remove Supplier. Check if supplier ID is correct.");
        }
    }

    private void addNewProduct() {
        System.out.println("\n--- Add New Product ---");

        String supplierId = getSupplierIdFromUser();

        System.out.print("Enter product catalog number: ");
        String id = scanner.nextLine();

        System.out.print("Enter product quantity per package: ");
        int quantityPerPackage;
        try {
            quantityPerPackage = Integer.parseInt(scanner.nextLine());
            if (quantityPerPackage <= 0) {
                System.out.println("Quantity must be greater than 0. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid quantity. Operation cancelled.");
            return;
        }

        System.out.println("Enter product discounts (optional). Format: amount,discount (e.g. 10,5%)");
        System.out.println("Enter -1 to finish entering discounts:");
        ArrayList<String> listDiscount = new ArrayList<>();
        while (true) {
            String input = scanner.nextLine().trim();
            if (input.equals("-1")) break;
            listDiscount.add(input);
        }

        System.out.print("Enter product price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
            if (price <= 0) {
                System.out.println("Price must be greater than 0. Operation cancelled.");
                return;
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Operation cancelled.");
            return;
        }

        System.out.print("Enter Unit.\n1 - Unit\n2 - KG");
        int unit = getUserChoice();



        boolean result = supplierSystem.addProduct(supplierId, id, quantityPerPackage, listDiscount, price, unit);

        if (result) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Failed to add product. Check if supplier exists and product ID is unique.");
        }
    }

    private void removeProduct() {
        System.out.println("\n--- Remove Product ---");

        String supplierId = getSupplierIdFromUser();

        System.out.print("Enter product catalog number: ");
        String catalogNumber = scanner.nextLine();

        boolean result = supplierSystem.removeProduct(supplierId, catalogNumber);

        if (result) {
            System.out.println("Product removed successfully!");
        } else {
            System.out.println("Failed to remove product. Check if supplier ID and catalog number are correct.");
        }
    }

    private void updateSupplier() {
        System.out.println("\n--- Update Supplier ---");

        System.out.print("Enter supplier ID to update: ");
        String id = scanner.nextLine();

        System.out.println("Select field to update:");
        System.out.println("1. Name");
        System.out.println("2. Supplier ID");
        System.out.println("3. Bank Account");
        System.out.println("4. Contact Person");

        int choice = getUserChoice();
        String fieldToUpdate;

        switch (choice) {
            case 1:
                fieldToUpdate = "name";
                break;
            case 2:
                fieldToUpdate = "supplierId";
                break;
            case 3:
                fieldToUpdate = "bankAccount";
                break;
            case 4:
                fieldToUpdate = "contactPersons";
                break;
            default:
                System.out.println("Invalid choice. Operation cancelled.");
                return;
        }

        System.out.print("Enter new value: ");
        String newValue = scanner.nextLine();

        boolean result = supplierSystem.updateSupplier(id, fieldToUpdate, newValue);

        if (result) {
            System.out.println("Supplier updated successfully!");
        } else {
            System.out.println("Failed to update supplier. Check if supplier ID exists.");
        }
    }

    private void updateProduct() {
        System.out.println("\n--- Update Product ---");

        System.out.print("Enter product catalog number to update: ");
        String catalogNumber = scanner.nextLine();

        System.out.print("Enter supplier ID to update: ");
        String supplierID = scanner.nextLine();

        System.out.println("Select field to update:");
        System.out.println("1. Name");
        System.out.println("2. Supplier ID");
        System.out.println("3. Quantity Per Package");
        System.out.println("4. Discounts (format: amount,discount)");
        System.out.println("5. Price");
        System.out.println("6. Units");
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

            case 2:
                fieldToUpdate = "supplierId";
                System.out.print("Enter new supplier ID: ");
                newValue = scanner.nextLine();
                break;

            case 3:
                fieldToUpdate = "quantityPerPackage";
                System.out.print("Enter new quantity per package: ");
                newValue = scanner.nextLine();
                break;

            case 4:
                fieldToUpdate = "discounts";
                System.out.println("Enter new discounts (format: amount,discount). Enter -1 to finish:");
                ArrayList<String> discountList = new ArrayList<>();
                while (true) {
                    String input = scanner.nextLine().trim();
                    if (input.equals("-1")) break;
                    discountList.add(input);
                }
                newValue = String.join(";", discountList); // נעבד את זה בתוך SystemController
                break;

            case 5:
                fieldToUpdate = "price";
                System.out.print("Enter new price: ");
                newValue = scanner.nextLine();
                break;

            case 6:
                fieldToUpdate = "units";
                System.out.println("Enter new unit:");
                System.out.println("1 - Unit");
                System.out.println("2 - KG");
                int unitChoice = getUserChoice();
                if (unitChoice == 1) newValue = "Unit";
                else if (unitChoice == 2) newValue = "KG";
                else {
                    System.out.println("Invalid unit. Operation cancelled.");
                    return;
                }
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

    private void viewAllSuppliers() {
        System.out.println("\n--- All Suppliers ---");
        for (String s : supplierSystem.getAllSuppliers()) {
            System.out.println(s);
        }
    }

    private void viewAllProducts() {
        System.out.println("\n--- All Products ---");
        for (String p : supplierSystem.getAllProducts()) {
            System.out.println(p);
        }
    }

    // Add the new order-related methods:

    private void insertOrder() {
        System.out.println("\n--- Insert Existing Order ---");

        viewAllSuppliers();

        String supplierId = getSupplierIdFromUser();

        int agreementIndex = selectAgreement(supplierId);

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
        while (agreementIndex< 0 || numberOfAgreements < agreementIndex) {
            if (agreements.isEmpty()) {
                System.out.println("No agreements found for supplier ID: " + supplierId);
            } else {
                System.out.println("Agreements for supplier ID " + supplierId + ":");
                int index = 0;
                for (String agreement : agreements) {
                    System.out.println(index + " - " + agreement);
                    index++;
                }
            }
            agreementIndex = getUserChoice();
            if (agreementIndex < 0 || numberOfAgreements < agreementIndex) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }
        return agreementIndex;
    }

    private Map<Integer,Integer> selectProducts(int numProducts) {
        Map<Integer, Integer> IndexProducts = new HashMap<>();
        //List<Integer> quantitiesList = new ArrayList<>();
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
        System.out.print("Enter supply date (YYYY-MM-DD): ");
        LocalDate supplyDate;
        try {
            supplyDate = LocalDate.parse(scanner.nextLine());
        } catch (Exception e) {
            System.out.println("Invalid date format. Setting supply date to 7 days from now.");
            supplyDate = orderDate.plusDays(7);
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


        int status = -1;
        while (status < 1  || 3 < status) {
            System.out.println("Select new status:");
            System.out.println("1. DELIVERED");
            System.out.println("2. IN_PROCESS");
            System.out.println("3. CANCELLED");
            status = getUserChoice();

            if(status < 1 || 3 < status) {
                System.out.println("Invalid choice. Please enter a valid number.");
            }
        }

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

    /*private void viewAgreementsBySupplier(String supplierId) {
        System.out.println("\n--- View Agreements by Supplier ---");

        System.out.println("\n--- Enter the serial number of the desired agreement ---");

        List<String> agreements = supplierSystem.getAgreementsBySupplier(supplierId);

        int numberOfAgreements = agreements.size();
        int agreementIndex = -1;
        while (agreementIndex< 0 || numberOfAgreements < agreementIndex) {
            if (agreements.isEmpty()) {
                System.out.println("No agreements found for supplier ID: " + supplierId);
            } else {
                System.out.println("Agreements for supplier ID " + supplierId + ":");
                int index = 0;
                for (String agreement : agreements) {
                    System.out.println(index + " - " + agreement);
                    index++;
                }
            }
            agreementIndex = getUserChoice();
            if (agreementIndex < 0 || numberOfAgreements < agreementIndex) {
                System.out.println("Invalid input. Please enter a valid number.");
            }
        }

        List<String> products = supplierSystem.getProductsByAgreement(supplierId,agreementIndex);
        for(String product: products){
            System.out.println(product);
        }

    }*/


    // Delivery Manager Functions
    private void viewSuppliersRequiringPickup() {
        System.out.println("\n--- Suppliers Requiring Pickup ---");
        String suppliers = supplierSystem.getSuppliersRequiringPickup();
        System.out.println(suppliers);
    }


    /* General Manager Functions
    private void loadDataFromFiles() {
        System.out.println("\n--- Load Data From Files ---");

        System.out.print("Enter file path for suppliers: ");
        String suppliersPath = scanner.nextLine();

        System.out.print("Enter file path for products: ");
        String productsPath = scanner.nextLine();

        boolean result = supplierSystem.loadDataFromFiles(suppliersPath, productsPath);

        if (result) {
            System.out.println("Data loaded successfully!");
        } else {
            System.out.println("Failed to load data. Check file paths and formats.");
        }
    }

    private void saveDataToFiles() {
        System.out.println("\n--- Save Data To Files ---");

        System.out.print("Enter file path for suppliers: ");
        String suppliersPath = scanner.nextLine();

        System.out.print("Enter file path for products: ");
        String productsPath = scanner.nextLine();

        boolean result = supplierSystem.saveDataToFiles(suppliersPath, productsPath);

        if (result) {
            System.out.println("Data saved successfully!");
        } else {
            System.out.println("Failed to save data. Check file paths and permissions.");
        }
    }

    private void analyzeReports() {
        System.out.println("\n--- Analyze Reports ---");
        System.out.println("1. Product Distribution by Category");
        System.out.println("2. Supplier Distribution by Location");
        System.out.println("3. Price Analysis");

        int choice = getUserChoice();

        switch (choice) {
            case 1:
                String categoryReport = supplierSystem.getProductDistributionByCategory();
                System.out.println(categoryReport);
                break;
            case 2:
                String locationReport = supplierSystem.getSupplierDistributionByLocation();
                System.out.println(locationReport);
                break;
            case 3:
                String priceReport = supplierSystem.getPriceAnalysis();
                System.out.println(priceReport);
                break;
            default:
                System.out.println("Invalid choice.");
                break;
        }
    }

    private void viewAllSystemData() {
        System.out.println("\n--- All System Data ---");
        System.out.println("=== Suppliers ===");
        viewAllSuppliers();
        System.out.println("\n=== Products ===");
        viewAllProducts();
    }*/

    // Regular Employee Functions
    private void searchProductById() {
        System.out.println("\n--- Search Product by catalog number and supplier ID ---");
        System.out.print("Enter catalog number: ");
        String catalogNum = scanner.nextLine();

        String supplierId = getSupplierIdFromUser();

        String productInfo = supplierSystem.getProductBySupplierAndCatalog(supplierId, catalogNum);

        if (productInfo != null && !productInfo.isEmpty()) {
            System.out.println(productInfo);
        } else {
            System.out.println("Product not found.");
        }
    }

}
