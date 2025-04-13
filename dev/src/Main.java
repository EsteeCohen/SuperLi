package src;

import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public class Main {
    private static SupplierSystem supplierSystem;
    private static Scanner scanner;

    public static void main(String[] args) {
        scanner = new Scanner(System.in);
        supplierSystem = new SupplierSystem(); // System initialized without initial data

        System.out.println("Welcome to the Supplier Management System!");

        boolean isRunning = true;
        while (isRunning) {
            displayMenu();
            int choice = getUserChoice();

            switch (choice) {
                case 1:
                    loadDataFromFiles();
                    break;
                case 2:
                    addNewSupplier();
                    break;
                case 3:
                    addNewProduct();
                    break;
                case 4:
                    updateSupplierDetails();
                    break;
                case 5:
                    updateProductDetails();
                    break;
                case 6:
                    displayAllSuppliers();
                    break;
                case 7:
                    displayAllProducts();
                    break;
                case 8:
                    searchSupplier();
                    break;
                case 9:
                    searchProduct();
                    break;
                case 10:
                    removeSupplier();
                    break;
                case 11:
                    removeProduct();
                    break;
                case 12:
                    saveDataToFiles();
                    break;
                case 0:
                    isRunning = false;
                    System.out.println("Thank you for using the Supplier Management System!");
                    break;
                default:
                    System.out.println("Please select a valid option from the menu.");
            }
        }
        scanner.close();
    }

    private static void displayMenu() {
        System.out.println("\n=== Supplier Management System Menu ===");
        System.out.println("1. Load data from files");
        System.out.println("2. Add new supplier");
        System.out.println("3. Add new product");
        System.out.println("4. Update supplier details");
        System.out.println("5. Update product details");
        System.out.println("6. Display all suppliers");
        System.out.println("7. Display all products");
        System.out.println("8. Search supplier");
        System.out.println("9. Search product");
        System.out.println("10. Remove supplier");
        System.out.println("11. Remove product");
        System.out.println("12. Save data to files");
        System.out.println("0. Exit");
        System.out.print("Choose an option: ");
    }

    private static int getUserChoice() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1; // Invalid value will cause an error message in the main menu
        }
    }

    private static void loadDataFromFiles() {
        System.out.println("Loading data from files...");

        // Check if data files exist
        File suppliersFile = new File("suppliers.dat");
        File productsFile = new File("products.dat");

        if (!suppliersFile.exists() || !productsFile.exists()) {
            System.out.println("At least one of the data files does not exist. Data not loaded.");
            return;
        }

        try {
            supplierSystem.loadData("suppliers.dat", "products.dat");
            System.out.println("Data loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading data: " + e.getMessage());
        }
    }

    private static void saveDataToFiles() {
        System.out.println("Saving data to files...");
        try {
            supplierSystem.saveData("suppliers.dat", "products.dat");
            System.out.println("Data saved successfully!");
        } catch (IOException e) {
            System.out.println("Error saving data: " + e.getMessage());
        }
    }

    private static void addNewSupplier() {
        System.out.println("\n=== Add New Supplier ===");
        System.out.print("Enter supplier ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter supplier name: ");
        String name = scanner.nextLine();

        System.out.print("Enter address: ");
        String address = scanner.nextLine();

        System.out.print("Enter phone: ");
        String phone = scanner.nextLine();

        Supplier newSupplier = new Supplier(id, name, address, phone);
        boolean success = supplierSystem.addSupplier(newSupplier);

        if (success) {
            System.out.println("Supplier added successfully!");
        } else {
            System.out.println("Error adding supplier. A supplier with this ID may already exist.");
        }
    }

    private static void addNewProduct() {
        System.out.println("\n=== Add New Product ===");
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        System.out.print("Enter product name: ");
        String name = scanner.nextLine();

        System.out.print("Enter price: ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid price. Product addition failed.");
            return;
        }

        System.out.print("Enter supplier ID: ");
        String supplierId = scanner.nextLine();

        Product newProduct = new Product(id, name, price, supplierId);
        boolean success = supplierSystem.addProduct(newProduct);

        if (success) {
            System.out.println("Product added successfully!");
        } else {
            System.out.println("Error adding product. A product with this ID may already exist or the supplier does not exist.");
        }
    }

    private static void updateSupplierDetails() {
        System.out.println("\n=== Update Supplier Details ===");
        System.out.print("Enter supplier ID to update: ");
        String id = scanner.nextLine();

        Supplier supplier = supplierSystem.findSupplier(id);
        if (supplier == null) {
            System.out.println("Supplier with this ID not found.");
            return;
        }

        System.out.println("Current supplier details: " + supplier);

        System.out.print("Enter new supplier name (leave empty to keep current value): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            supplier.setName(name);
        }

        System.out.print("Enter new address (leave empty to keep current value): ");
        String address = scanner.nextLine();
        if (!address.isEmpty()) {
            supplier.setAddress(address);
        }

        System.out.print("Enter new phone (leave empty to keep current value): ");
        String phone = scanner.nextLine();
        if (!phone.isEmpty()) {
            supplier.setPhone(phone);
        }

        supplierSystem.updateSupplier(supplier);
        System.out.println("Supplier details updated successfully!");
    }

    private static void updateProductDetails() {
        System.out.println("\n=== Update Product Details ===");
        System.out.print("Enter product ID to update: ");
        String id = scanner.nextLine();

        Product product = supplierSystem.findProduct(id);
        if (product == null) {
            System.out.println("Product with this ID not found.");
            return;
        }

        System.out.println("Current product details: " + product);

        System.out.print("Enter new product name (leave empty to keep current value): ");
        String name = scanner.nextLine();
        if (!name.isEmpty()) {
            product.setName(name);
        }

        System.out.print("Enter new price (leave empty to keep current value): ");
        String priceStr = scanner.nextLine();
        if (!priceStr.isEmpty()) {
            try {
                double price = Double.parseDouble(priceStr);
                product.setPrice(price);
            } catch (NumberFormatException e) {
                System.out.println("Invalid price. Other details were updated.");
            }
        }

        System.out.print("Enter new supplier ID (leave empty to keep current value): ");
        String supplierId = scanner.nextLine();
        if (!supplierId.isEmpty()) {
            if (supplierSystem.findSupplier(supplierId) != null) {
                product.setSupplierId(supplierId);
            } else {
                System.out.println("Supplier with this ID does not exist. Supplier ID not updated.");
            }
        }

        supplierSystem.updateProduct(product);
        System.out.println("Product details updated successfully!");
    }

    private static void displayAllSuppliers() {
        System.out.println("\n=== List of All Suppliers ===");
        for (Supplier supplier : supplierSystem.getAllSuppliers()) {
            System.out.println(supplier);
        }

        if (supplierSystem.getAllSuppliers().isEmpty()) {
            System.out.println("No suppliers in the system.");
        }
    }

    private static void displayAllProducts() {
        System.out.println("\n=== List of All Products ===");
        for (Product product : supplierSystem.getAllProducts()) {
            System.out.println(product);
        }

        if (supplierSystem.getAllProducts().isEmpty()) {
            System.out.println("No products in the system.");
        }
    }

    private static void searchSupplier() {
        System.out.println("\n=== Search Supplier ===");
        System.out.print("Enter supplier ID: ");
        String id = scanner.nextLine();

        Supplier supplier = supplierSystem.findSupplier(id);
        if (supplier != null) {
            System.out.println("Supplier found: " + supplier);
        } else {
            System.out.println("Supplier with this ID not found.");
        }
    }

    private static void searchProduct() {
        System.out.println("\n=== Search Product ===");
        System.out.print("Enter product ID: ");
        String id = scanner.nextLine();

        Product product = supplierSystem.findProduct(id);
        if (product != null) {
            System.out.println("Product found: " + product);

            // Display product's supplier details
            Supplier supplier = supplierSystem.findSupplier(product.getSupplierId());
            if (supplier != null) {
                System.out.println("Product supplier: " + supplier);
            }
        } else {
            System.out.println("Product with this ID not found.");
        }
    }

    private static void removeSupplier() {
        System.out.println("\n=== Remove Supplier ===");
        System.out.print("Enter supplier ID to remove: ");
        String id = scanner.nextLine();

        Supplier supplier = supplierSystem.findSupplier(id);
        if (supplier == null) {
            System.out.println("Supplier with this ID not found.");
            return;
        }

        // Check if there are products associated with this supplier
        boolean hasProducts = false;
        for (Product product : supplierSystem.getAllProducts()) {
            if (product.getSupplierId().equals(id)) {
                hasProducts = true;
                break;
            }
        }

        if (hasProducts) {
            System.out.println("Cannot remove supplier. There are products associated with this supplier.");
            System.out.print("Do you want to remove the supplier and all associated products? (y/n): ");
            String confirm = scanner.nextLine();

            if (confirm.equalsIgnoreCase("y")) {
                // Remove all products associated with this supplier
                for (Product product : supplierSystem.getAllProducts()) {
                    if (product.getSupplierId().equals(id)) {
                        supplierSystem.removeProduct(product.getId());
                    }
                }

                supplierSystem.removeSupplier(id);
                System.out.println("Supplier and all associated products removed successfully!");
            } else {
                System.out.println("Supplier removal cancelled.");
            }
        } else {
            supplierSystem.removeSupplier(id);
            System.out.println("Supplier removed successfully!");
        }
    }

    private static void removeProduct() {
        System.out.println("\n=== Remove Product ===");
        System.out.print("Enter product ID to remove: ");
        String id = scanner.nextLine();

        Product product = supplierSystem.findProduct(id);
        if (product == null) {
            System.out.println("Product with this ID not found.");
            return;
        }

        supplierSystem.removeProduct(id);
        System.out.println("Product removed successfully!");
    }
}