package src.ServiceLayer;

import src.DomainLayer.SystemController;
import src.DomainLayer.Supplier;
import src.DomainLayer.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class SupplierSystemService {
    private static SupplierSystemService instance;
    private SystemController systemController;

    private SupplierSystemService() {
        this.systemController = SystemController.getInstance();
    }

    public static synchronized SupplierSystemService getInstance() {
        if (instance == null) {
            instance = new SupplierSystemService();
        }
        return instance;
    }

    // ===== Supplier Management =====

    public boolean addSupplierWithDelivery(String name, String id, String bankAccount, String deliveryDays, List<String> contactInputs) {
        return systemController.addSupplierWithDelivery(name, id, bankAccount, deliveryDays, contactInputs);
    }

    public boolean addSupplierNeedsPickup(String name, String id, String bankAccount, String address, List<String> contactInputs) {
        return systemController.addSupplierNeedsPickup(name, id, bankAccount, address, contactInputs);
    }


    public boolean updateSupplier(String id, String field, String newValue) {
        return systemController.updateSupplierField(id, field, newValue);
    }

    public List<String> getAllSuppliers() {
        return systemController.getAllSuppliers();
    }


    public String getSuppliersRequiringPickup() {
        return systemController.getSuppliersRequiringPickupAsString();
    }

    public List<String> getAgreementsBySupplier(String supplierId) {
        return systemController.getAgreementsBySupplierAsStrings(supplierId);
    }

    public boolean removeSupplier(String supplierId) {
        return systemController.removeSupplier(supplierId);
    }

    // ===== Product Management =====

    public boolean addProduct(String name, String supplierId, String catalogNumber, int quantityPerPackage,
                              ArrayList<String> discountInput, double price, int unit) {
        return systemController.addProductWithDiscounts(name, supplierId, catalogNumber, quantityPerPackage, discountInput, price, unit);
    }

    public boolean updateProduct(String supplierID, String id, String field, String value) {
        return systemController.updateProductField(supplierID, id, field, value);
    }


    public List<String> getAllProducts() {
        return systemController.getAllProducts();
    }


    public String getProductBySupplierAndCatalog(String supplierId, String catalogNumber) {
        return systemController.findProductBySupplierAndCatalog(supplierId, catalogNumber);
    }


    public boolean removeProduct(String supplierID, String id) {
        return systemController.removeProduct(supplierID, id);
    }


    // ===== ORDER =====

    public boolean insertOrder(String supplierId, LocalDate orderDate, LocalDate supplyDate, Map<Integer, Integer> indexString, String contactPresonName, String contactPersonPhone, int agreementIndex, int statusIndex)
    {
        return systemController.insertOrder(supplierId, orderDate, supplyDate, indexString, contactPresonName, contactPersonPhone, agreementIndex, statusIndex);
    }


    // Update order status
    public boolean updateOrderStatus(int orderId, int newStatus) {
        return systemController.updateOrderStatus(orderId, newStatus);
    }

    // Get all orders
    public List<String> getAllOrders() {
        return systemController.getAllOrders();
    }

    // Get order by id
    public String getOrderById(int orderId) {
        return systemController.getOrderById(orderId);
    }

    // Get orders by status
    public List<String> getOrdersByStatus(int status) {
        try {
            return systemController.getOrdersByStatus(status);
        } catch (IllegalArgumentException e) {
            return new ArrayList<>();
        }
    }

    // Get orders by supplier
    public List<String> getOrdersBySupplier(String supplierId) {
        return systemController.getOrdersBySupplier(supplierId);
    }

    public boolean createAgreement(String supplierId, int paymentMethod, int paymentTiming, LocalDate validFrom, LocalDate validTo, Set<Integer> IndexProducts) {
        return systemController.createAgreement(supplierId, paymentMethod, paymentTiming, validFrom, validTo, IndexProducts);
    }

    public boolean removeAgreement(String supplierId, int agreementIndex) {
        return systemController.removeAgreement(supplierId, agreementIndex);
    }
    //help func

    public boolean checkIfSupplierExists(String supplierId){
        return systemController.checkIfSupplierExists(supplierId);
    }

    public List<String> getProductsByAgreement(String supplierId, int agreementIndex) {
        return systemController.getProductsByAgreement(supplierId, agreementIndex);
    }

    public List<String> getProductsBySupplier(String supplierId) {
        return systemController.getProductsBySupplier(supplierId);
    }

    public String getSupplierById(String supplierId) {
        return systemController.getSupplierById(supplierId);
    }

    public boolean updateAgreementProducts(String supplierId, int agreementIndex, List<Integer> indexProducts) {
        return systemController.updateAgreementProducts(supplierId, agreementIndex, indexProducts);
    }

    public boolean updatePaymentMethods(String supplierId, int agreementIndex, int paymentMethodIndex) {
        return systemController.updatePaymentMethods(supplierId, agreementIndex, paymentMethodIndex);
    }

    public boolean updatePaymentTiming(String supplierId, int agreementIndex, int paymentTimingIndex) {
        return systemController.updatePaymentTiming(supplierId, agreementIndex, paymentTimingIndex);
    }

    public boolean updateValidFrom(String supplierId, int agreementIndex, LocalDate validFrom) {
        return systemController.updateValidFrom(supplierId, agreementIndex, validFrom);
    }

    public boolean updateValidTo(String supplierId, int agreementIndex, LocalDate validTo) {
        return systemController.updateValidTo(supplierId, agreementIndex, validTo);
    }
    public List<String> getPaymentMethods(){
        return systemController.getPaymentMethods();
    }

    public List<String> getPaymentTimings(){
        return systemController.getPaymentTimings();
    }

    public LocalDate getValidFromOfAgreement(String supplierId, int agreementIndex) {
        return systemController.getValidFromOfAgreement(supplierId, agreementIndex);
    }

}
