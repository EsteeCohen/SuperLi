package src.PresentationLayer;

import java.util.Scanner;

public class SupplierForm {
    private Scanner scanner;
    private SupplierService supplierService;
    private OrderService orderService;

    public SupplierForm() {
        scanner = new Scanner(System.in);
        supplierService = SupplierService.getInstance();
        orderService = OrderService.getInstance();
    }

    public void displayMainMenu() {
        boolean exit = false;
        while (!exit) {
            System.out.println("\n===== Supplier Management System =====");
            System.out.println("1. Manage Suppliers");
            System.out.println("2. Manage Agreements");
            System.out.println("3. Manage Orders");
            System.out.println("4. Generate Reports");
            System.out.println("0. Exit");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    manageSuppliers();
                    break;
                case 2:
                    manageAgreements();
                    break;
                case 3:
                    manageOrders();
                    break;
                case 4:
                    generateReports();
                    break;
                case 0:
                    exit = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void manageSuppliers() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Supplier Management =====");
            System.out.println("1. Create New Supplier");
            System.out.println("2. View All Suppliers");
            System.out.println("3. View Supplier Details");
            System.out.println("4. Update Supplier Info");
            System.out.println("5. Add Contact Person");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    createSupplier();
                    break;
                case 2:
                    viewAllSuppliers();
                    break;
                case 3:
                    viewSupplierDetails();
                    break;
                case 4:
                    updateSupplierInfo();
                    break;
                case 5:
                    addContactPerson();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createSupplier() {
        System.out.println("\n===== Create New Supplier =====");
        System.out.print("Enter supplier name: ");
        String name = scanner.nextLine();

        System.out.print("Enter company ID (numbers only): ");
        int companyId = getIntInput();

        try {
            Supplier supplier = supplierService.createSupplier(name, companyId);
            System.out.println("Supplier created successfully! Supplier ID: " + supplier.getSupplierId());

            System.out.print("Do you want to add bank account details now? (y/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("y")) {
                System.out.print("Enter bank account number: ");
                int bankAccount = getIntInput();
                supplierService.updateSupplierBankAccount(supplier.getSupplierId(), bankAccount);
                System.out.println("Bank account updated successfully!");
            }

            System.out.print("Do you want to set payment terms now? (y/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("y")) {
                System.out.println("Available payment terms:");
                System.out.println("1. CASH_ON_DELIVERY");
                System.out.println("2. ADVANCE_PAYMENT");
                System.out.print("Select payment term (1-2): ");
                int paymentChoice = getIntInput();

                PaymentTerms paymentTerm = (paymentChoice == 1) ?
                        PaymentTerms.CASH_ON_DELIVERY :
                        PaymentTerms.ADVANCE_PAYMENT;

                supplierService.updateSupplierPaymentTerms(supplier.getSupplierId(), paymentTerm);
                System.out.println("Payment terms updated successfully!");
            }

        } catch (Exception e) {
            System.out.println("Error creating supplier: " + e.getMessage());
        }
    }

    private void viewAllSuppliers() {
        System.out.println("\n===== All Suppliers =====");
        List<Supplier> suppliers = supplierService.getAllSuppliers();

        if (suppliers.isEmpty()) {
            System.out.println("No suppliers found.");
            return;
        }

        for (Supplier supplier : suppliers) {
            System.out.println("ID: " + supplier.getSupplierId() + " | Name: " + supplier.getName() +
                    " | Company ID: " + supplier.getCompanyId());
        }
    }

    private void viewSupplierDetails() {
        System.out.println("\n===== View Supplier Details =====");
        System.out.print("Enter supplier ID: ");
        int supplierId = getIntInput();

        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                System.out.println("Supplier not found.");
                return;
            }

            System.out.println("\nSupplier Details:");
            System.out.println("ID: " + supplier.getSupplierId());
            System.out.println("Name: " + supplier.getName());
            System.out.println("Company ID: " + supplier.getCompanyId());
            System.out.println("Bank Account: " + (supplier.getBankAccount() != 0 ? supplier.getBankAccount() : "Not set"));
            System.out.println("Payment Terms: " + (supplier.getPaymentTerms() != null ? supplier.getPaymentTerms() : "Not set"));

            System.out.println("\nContact Persons:");
            List<ContactPerson> contacts = supplier.getContactPersons();
            if (contacts.isEmpty()) {
                System.out.println("No contact persons.");
            } else {
                for (ContactPerson contact : contacts) {
                    System.out.println("- Name: " + contact.getContactName() +
                            " | Email: " + contact.getEmail() +
                            " | Phone: " + contact.getPhoneNumber());
                }
            }

            System.out.println("\nAgreements:");
            List<Agreement> agreements = supplier.getAgreements();
            if (agreements.isEmpty()) {
                System.out.println("No agreements.");
            } else {
                for (Agreement agreement : agreements) {
                    System.out.println("- Valid from: " + agreement.getValidFrom() +
                            " to " + agreement.getValidTo() +
                            " | Delivery by supplier: " + (agreement.isDeliveredByCompany() ? "Yes" : "No"));
                }
            }

        } catch (Exception e) {
            System.out.println("Error retrieving supplier details: " + e.getMessage());
        }
    }

    private void updateSupplierInfo() {
        System.out.println("\n===== Update Supplier Info =====");
        System.out.print("Enter supplier ID: ");
        int supplierId = getIntInput();

        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                System.out.println("Supplier not found.");
                return;
            }

            System.out.println("\nCurrent Supplier Details:");
            System.out.println("1. Name: " + supplier.getName());
            System.out.println("2. Bank Account: " + (supplier.getBankAccount() != 0 ? supplier.getBankAccount() : "Not set"));
            System.out.println("3. Payment Terms: " + (supplier.getPaymentTerms() != null ? supplier.getPaymentTerms() : "Not set"));
            System.out.println("0. Cancel");
            System.out.print("Select field to update (0-3): ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    System.out.print("Enter new name: ");
                    String name = scanner.nextLine();
                    supplierService.updateSupplierName(supplierId, name);
                    System.out.println("Name updated successfully!");
                    break;
                case 2:
                    System.out.print("Enter new bank account number: ");
                    int bankAccount = getIntInput();
                    supplierService.updateSupplierBankAccount(supplierId, bankAccount);
                    System.out.println("Bank account updated successfully!");
                    break;
                case 3:
                    System.out.println("Available payment terms:");
                    System.out.println("1. CASH_ON_DELIVERY");
                    System.out.println("2. ADVANCE_PAYMENT");
                    System.out.print("Select payment term (1-2): ");
                    int paymentChoice = getIntInput();

                    PaymentTerms paymentTerm = (paymentChoice == 1) ?
                            PaymentTerms.CASH_ON_DELIVERY :
                            PaymentTerms.ADVANCE_PAYMENT;

                    supplierService.updateSupplierPaymentTerms(supplierId, paymentTerm);
                    System.out.println("Payment terms updated successfully!");
                    break;
                case 0:
                    // Cancel
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }

        } catch (Exception e) {
            System.out.println("Error updating supplier: " + e.getMessage());
        }
    }

    private void addContactPerson() {
        System.out.println("\n===== Add Contact Person =====");
        System.out.print("Enter supplier ID: ");
        int supplierId = getIntInput();

        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                System.out.println("Supplier not found.");
                return;
            }

            System.out.print("Enter contact person name: ");
            String name = scanner.nextLine();

            System.out.print("Enter contact person email: ");
            String email = scanner.nextLine();

            System.out.print("Enter contact person phone number: ");
            int phoneNumber = getIntInput();

            supplierService.addContactPersonToSupplier(supplierId, name, email, phoneNumber);
            System.out.println("Contact person added successfully!");

        } catch (Exception e) {
            System.out.println("Error adding contact person: " + e.getMessage());
        }
    }

    private void manageAgreements() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Agreement Management =====");
            System.out.println("1. Create New Agreement");
            System.out.println("2. View Supplier Agreements");
            System.out.println("3. Add Product to Agreement");
            System.out.println("4. View Agreement Products");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    createAgreement();
                    break;
                case 2:
                    viewSupplierAgreements();
                    break;
                case 3:
                    addProductToAgreement();
                    break;
                case 4:
                    viewAgreementProducts();
                    break;
                case 0:
                    back = true;
                    break;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }

    private void createAgreement() {
        System.out.println("\n===== Create New Agreement =====");
        System.out.print("Enter supplier ID: ");
        int supplierId = getIntInput();

        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                System.out.println("Supplier not found.");
                return;
            }

            System.out.println("Available payment terms:");
            System.out.println("1. CASH_ON_DELIVERY");
            System.out.println("2. ADVANCE_PAYMENT");
            System.out.print("Select payment term (1-2): ");
            int paymentChoice = getIntInput();

            PaymentTerms paymentTerm = (paymentChoice == 1) ?
                    PaymentTerms.CASH_ON_DELIVERY :
                    PaymentTerms.ADVANCE_PAYMENT;

            System.out.print("Is delivery by company? (true/false): ");
            boolean deliveredByCompany = Boolean.parseBoolean(scanner.nextLine());

            System.out.print("Enter agreement start date (yyyy-MM-dd): ");
            String startDateStr = scanner.nextLine();
            LocalDate startDate = LocalDate.parse(startDateStr);

            System.out.print("Enter agreement end date (yyyy-MM-dd): ");
            String endDateStr = scanner.nextLine();
            LocalDate endDate = LocalDate.parse(endDateStr);

            Agreement agreement = new Agreement(supplierId, paymentTerm, startDate, endDate, deliveredByCompany);

            System.out.println("Select delivery days (enter numbers separated by commas):");
            System.out.println("1. SUNDAY");
            System.out.println("2. MONDAY");
            System.out.println("3. TUESDAY");
            System.out.println("4. WEDNESDAY");
            System.out.println("5. THURSDAY");
            System.out.println("6. FRIDAY");
            System.out.println("7. SATURDAY");
            System.out.print("Enter selection: ");

            String[] selections = scanner.nextLine().split(",");
            for (String selection : selections) {
                int day = Integer.parseInt(selection.trim());
                switch (day) {
                    case 1: agreement.addDeliveryDay(DaysOfTheWeek.SUNDAY); break;
                    case 2: agreement.addDeliveryDay(DaysOfTheWeek.MONDAY); break;
                    case 3: agreement.addDeliveryDay(DaysOfTheWeek.TUESDAY); break;
                    case 4: agreement.addDeliveryDay(DaysOfTheWeek.WEDNESDAY); break;
                    case 5: agreement.addDeliveryDay(DaysOfTheWeek.THURSDAY); break;
                    case 6: agreement.addDeliveryDay(DaysOfTheWeek.FRIDAY); break;
                    case 7: agreement.addDeliveryDay(DaysOfTheWeek.SATURDAY); break;
                }
            }

            supplierService.addAgreement(supplierId, agreement);
            System.out.println("Agreement created successfully!");

        } catch (Exception e) {
            System.out.println("Error creating agreement: " + e.getMessage());
        }
    }

    private void viewSupplierAgreements() {
        System.out.println("\n===== View Supplier Agreements =====");
        System.out.print("Enter supplier ID: ");
        int supplierId = getIntInput();

        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                System.out.println("Supplier not found.");
                return;
            }

            List<Agreement> agreements = supplier.getAgreements();
            if (agreements.isEmpty()) {
                System.out.println("No agreements found for this supplier.");
                return;
            }

            System.out.println("\nAgreements for " + supplier.getName() + ":");
            for (int i = 0; i < agreements.size(); i++) {
                Agreement agreement = agreements.get(i);
                System.out.println((i+1) + ". Valid from: " + agreement.getValidFrom() +
                        " to " + agreement.getValidTo() +
                        " | Payment terms: " + agreement.getPaymentTerms() +
                        " | Delivery by company: " + (agreement.isDeliveredByCompany() ? "Yes" : "No"));

                System.out.print("   Delivery days: ");
                List<DaysOfTheWeek> deliveryDays = agreement.getDeliveryDay();
                for (int j = 0; j < deliveryDays.size(); j++) {
                    System.out.print(deliveryDays.get(j));
                    if (j < deliveryDays.size() - 1) {
                        System.out.print(", ");
                    }
                }
                System.out.println();
            }

        } catch (Exception e) {
            System.out.println("Error retrieving agreements: " + e.getMessage());
        }
    }

    private void addProductToAgreement() {
        System.out.println("\n===== Add Product to Agreement =====");
        System.out.print("Enter supplier ID: ");
        int supplierId = getIntInput();

        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                System.out.println("Supplier not found.");
                return;
            }

            List<Agreement> agreements = supplier.getAgreements();
            if (agreements.isEmpty()) {
                System.out.println("No agreements found for this supplier.");
                return;
            }

            System.out.println("\nSelect an agreement:");
            for (int i = 0; i < agreements.size(); i++) {
                Agreement agreement = agreements.get(i);
                System.out.println((i+1) + ". Valid from: " + agreement.getValidFrom() +
                        " to " + agreement.getValidTo());
            }

            System.out.print("Enter selection (1-" + agreements.size() + "): ");
            int agreementIndex = getIntInput() - 1;

            if (agreementIndex < 0 || agreementIndex >= agreements.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Agreement selectedAgreement = agreements.get(agreementIndex);

            System.out.print("Enter product catalog number: ");
            int catalogNumber = getIntInput();

            System.out.print("Enter product ID: ");
            int productId = getIntInput();

            System.out.print("Enter product price: ");
            double price = getDoubleInput();

            Product product = new Product(supplierId, catalogNumber, productId, price);

            System.out.print("Do you want to add quantity discounts? (y/n): ");
            if (scanner.nextLine().trim().toLowerCase().equals("y")) {
                boolean addMoreDiscounts = true;
                while (addMoreDiscounts) {
                    System.out.print("Enter quantity threshold: ");
                    int quantity = getIntInput();

                    System.out.print("Enter discount rate (0-1, e.g., 0.1 for 10%): ");
                    double discount = getDoubleInput();

                    product.addQuantityDiscount(quantity, discount);

                    System.out.print("Add another discount? (y/n): ");
                    addMoreDiscounts = scanner.nextLine().trim().toLowerCase().equals("y");
                }
            }

            supplierService.addProductToAgreement(supplierId, selectedAgreement, product);
            System.out.println("Product added to agreement successfully!");

        } catch (Exception e) {
            System.out.println("Error adding product to agreement: " + e.getMessage());
        }
    }

    private void viewAgreementProducts() {
        System.out.println("\n===== View Agreement Products =====");
        System.out.print("Enter supplier ID: ");
        int supplierId = getIntInput();

        try {
            Supplier supplier = supplierService.getSupplierById(supplierId);
            if (supplier == null) {
                System.out.println("Supplier not found.");
                return;
            }

            List<Agreement> agreements = supplier.getAgreements();
            if (agreements.isEmpty()) {
                System.out.println("No agreements found for this supplier.");
                return;
            }

            System.out.println("\nSelect an agreement:");
            for (int i = 0; i < agreements.size(); i++) {
                Agreement agreement = agreements.get(i);
                System.out.println((i+1) + ". Valid from: " + agreement.getValidFrom() +
                        " to " + agreement.getValidTo());
            }

            System.out.print("Enter selection (1-" + agreements.size() + "): ");
            int agreementIndex = getIntInput() - 1;

            if (agreementIndex < 0 || agreementIndex >= agreements.size()) {
                System.out.println("Invalid selection.");
                return;
            }

            Agreement selectedAgreement = agreements.get(agreementIndex);
            List<Product> products = selectedAgreement.getProducts();

            if (products.isEmpty()) {
                System.out.println("No products found in this agreement.");
                return;
            }

            System.out.println("\nProducts in selected agreement:");
            for (Product product : products) {
                System.out.println("- ID: " + product.getProductId() +
                        " | Catalog #: " + product.getCatalogNumber() +
                        " | Price: " + product.getPrice());

                Map<Integer, Double> discounts = product.getQuantityDiscounts();
                if (!discounts.isEmpty()) {
                    System.out.println("  Quantity Discounts:");
                    for (Map.Entry<Integer, Double> entry : discounts.entrySet()) {
                        System.out.println("  - " + entry.getKey() + "+ units: " + (entry.getValue() * 100) + "% discount");
                    }
                }
            }

        } catch (Exception e) {
            System.out.println("Error retrieving products: " + e.getMessage());
        }
    }

    private void manageOrders() {
        boolean back = false;
        while (!back) {
            System.out.println("\n===== Order Management =====");
            System.out.println("1. Create New Order");
            System.out.println("2. View All Orders");
            System.out.println("3. View Order Details");
            System.out.println("4. Update Order Status");
            System.out.println("0. Back to Main Menu");
            System.out.print("Select an option: ");

            int choice = getIntInput();

            switch (choice) {
                case 1:
                    createOrder();
                    break;
                case 2:
                    viewAllOrders();
                    break;
                case 3:
                    viewOrderDetails();
                    break;
                case 4:
                    updateOrderStatus();
                    break;RetryClaude hit the max length for a message and has paused its response. You can write Continue to keep the chat going. Claude does not have the ability to run the code it generates yet.Claude can make mistakes. Please double-check responses.
}
