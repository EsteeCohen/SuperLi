package src.PresentationLayer;

import src.ServiceLayer.SupplierSystem;

import java.util.Map;
import java.util.Scanner;

public class SupplierSystemUI {
    private SupplierSystem supplierSystem;
    private Scanner scanner;

    public SupplierSystemUI() {
        this.supplierSystem = SupplierSystem.getInstance();
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
        System.out.println("3. General Manager");
        System.out.println("4. Regular Employee");
        System.out.print("Your choice: ");

        int choice = Integer.parseInt(scanner.nextLine());

        switch (choice) {
            case 1: return UserRole.PURCHASING_MANAGER;
            case 2: return UserRole.DELIVERY_MANAGER;
            case 3: return UserRole.GENERAL_MANAGER;
            case 4: return UserRole.REGULAR_EMPLOYEE;
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
            case GENERAL_MANAGER:
                displayGeneralManagerMenu();
                break;
            case REGULAR_EMPLOYEE:
                displayRegularEmployeeMenu();
                break;
        }
    }

    private void displayPurchasingManagerMenu() {
        System.out.println("\n=== Purchasing Manager Menu ===");
        System.out.println("1. Add new supplier");
        System.out.println("2. Add new product");
        System.out.println("3. Update supplier");
        System.out.println("4. Update product");
        System.out.println("5. View all suppliers");
        System.out.println("6. View all products");
        System.out.println("0. Exit");
        // Call appropriate functions here
    }

    private void displayDeliveryManagerMenu() {
        System.out.println("\n=== Delivery Manager Menu ===");
        System.out.println("1. View suppliers requiring pickup");
        System.out.println("2. Mark supplier as picked up");
        System.out.println("0. Exit");
    }

    private void displayGeneralManagerMenu() {
        System.out.println("\n=== General Manager Menu ===");
        System.out.println("1. Load data from files");
        System.out.println("2. Save data to files");
        System.out.println("3. Analyze reports");
        System.out.println("4. View all system data");
        System.out.println("0. Exit");
    }

    private void displayRegularEmployeeMenu() {
        System.out.println("\n=== Regular Employee Menu ===");
        System.out.println("1. View all products");
        System.out.println("2. Search product by ID");
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
            case GENERAL_MANAGER:
                handleGeneralManagerChoice(choice);
                break;
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
                updateSupplier();
                break;
            case 4:
                updateProduct();
                break;
            case 5:
                viewAllSuppliers();
                break;
            case 6:
                viewAllProducts();
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
            case 2:
                markSupplierAsPickedUp();
                break;
            default:
                System.out.println("Invalid option. Please try again.");
                break;
        }
    }

    private void handleGeneralManagerChoice(int choice) {
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
    }

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

    // Purchasing Manager Functions
    private void addNewSupplier() {
        boolean result = false;
        System.out.println("\n--- Add New Supplier ---");

        System.out.print("Enter supplier ID: ");
        String id = scanner.nextLine();

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
            result = supplierSystem.addSupplierWithDelivery(name, id, bankAccount, deliveryDaysInput);
        } else {
            System.out.print("Enter address: ");
            String address = scanner.nextLine();
            result = supplierSystem.addSupplierNeedsPickup(name, id, bankAccount, address);
        }

        // Check if the supplier was added successfully
        if (result) {
            System.out.println("Supplier added successfully!");
        } else {
            System.out.println("Failed to add supplier. Supplier ID may already exist.");
        }
    }


    public Product(String supplierId, int catalogNumber, int quantityPerPackage,
                   Map<Integer, Double> discountPerPackage, double price)
    private void addNewProduct() {
        System.out.println("\n--- Add New Product ---");

        System.out.print("Enter supplier ID: ");
        String supplierId = scanner.nextLine();

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

        System.out.print("Enter product discount (optional, press Enter to skip):(amount, discount), comma-separated\n");
        String discountInput = scanner.nextLine();

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



        boolean result = supplierSystem.addProduct(supplierId, id, quantityPerPackage, discountInput, price);

        if (result) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Failed to add product. Check if supplier exists and product ID is unique.");
        }
    }

    private void updateSupplier() {
        System.out.println("\n--- Update Supplier ---");

        System.out.print("Enter supplier ID to update: ");
        String id = scanner.nextLine();

        System.out.println("Select field to update:");
        System.out.println("1. Name");
        System.out.println("2. Address");
        System.out.println("3. Contact Person");
        System.out.println("4. Phone");

        int choice = getUserChoice();
        String fieldToUpdate;

        switch (choice) {
            case 1:
                fieldToUpdate = "name";
                break;
            case 2:
                fieldToUpdate = "address";
                break;
            case 3:
                fieldToUpdate = "contactPerson";
                break;
            case 4:
                fieldToUpdate = "phone";
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

        System.out.print("Enter product ID to update: ");
        String id = scanner.nextLine();

        System.out.println("Select field to update:");
        System.out.println("1. Name");
        System.out.println("2. Category");
        System.out.println("3. Price");

        int choice = getUserChoice();
        String fieldToUpdate;

        switch (choice) {
            case 1:
                fieldToUpdate = "name";
                System.out.print("Enter new name: ");
                String newName = scanner.nextLine();
                supplierSystem.updateProduct(id, fieldToUpdate, newName);
                break;
            case 2:
                fieldToUpdate = "category";
                System.out.print("Enter new category: ");
                String newCategory = scanner.nextLine();
                supplierSystem.updateProduct(id, fieldToUpdate, newCategory);
                break;
            case 3:
                fieldToUpdate = "price";
                System.out.print("Enter new price: ");
                try {
                    double newPrice = Double.parseDouble(scanner.nextLine());
                    supplierSystem.updateProduct(id, fieldToUpdate, String.valueOf(newPrice));
                } catch (NumberFormatException e) {
                    System.out.println("Invalid price format. Operation cancelled.");
                    return;
                }
                break;
            default:
                System.out.println("Invalid choice. Operation cancelled.");
                return;
        }

        System.out.println("Product updated successfully!");
    }

    private void viewAllSuppliers() {
        System.out.println("\n--- All Suppliers ---");
        String suppliers = supplierSystem.getAllSuppliers();
        System.out.println(suppliers);
    }

    private void viewAllProducts() {
        System.out.println("\n--- All Products ---");
        String products = supplierSystem.getAllProducts();
        System.out.println(products);
    }

    // Delivery Manager Functions
    private void viewSuppliersRequiringPickup() {
        System.out.println("\n--- Suppliers Requiring Pickup ---");
        String suppliers = supplierSystem.getSuppliersRequiringPickup();
        System.out.println(suppliers);
    }

    private void markSupplierAsPickedUp() {
        System.out.println("\n--- Mark Supplier as Picked Up ---");

        System.out.print("Enter supplier ID: ");
        String id = scanner.nextLine();

        boolean result = supplierSystem.markSupplierAsPickedUp(id);

        if (result) {
            System.out.println("Supplier marked as picked up successfully!");
        } else {
            System.out.println("Failed to mark supplier. Check if supplier ID exists.");
        }
    }

    // General Manager Functions
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
    }

    // Regular Employee Functions
    private void searchProductById() {
        System.out.println("\n--- Search Product by ID ---");

        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        String product = supplierSystem.getProductById(id);

        if (product != null && !product.isEmpty()) {
            System.out.println(product);
        } else {
            System.out.println("Product not found.");
        }
    }

    public static void main(String[] args) {
        SupplierSystemUI ui = new SupplierSystemUI();
        ui.start();
    }
}

