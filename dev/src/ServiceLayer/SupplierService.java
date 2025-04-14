package src.ServiceLayer;

import src.DomainLayer.Supplier;
import src.DomainLayer.SupplierController;
import src.DomainLayer.ContactPerson;
import src.DomainLayer.Agreement;
import src.DomainLayer.Product;
import src.DomainLayer.SupplierNeedsPickup;
import src.DomainLayer.SupplierWithDeliveryDays;
import src.DomainLayer.Enums.DaysOfTheWeek;

import java.util.ArrayList;
import java.util.List;

public class SupplierService {
    private static SupplierService instance;
    private SupplierController supplierController;

    private SupplierService() {
        supplierController = SupplierController.getInstance();
    }

    public static synchronized SupplierService getInstance() {
        if (instance == null) {
            instance = new SupplierService();
        }
        return instance;
    }

    public void createSupplier(String name, String supplierId, int bankAccount) {
        Supplier supplier = new SupplierNeedsPickup(name, "", supplierId, bankAccount);
        supplierController.addSupplier(supplierId, supplier);
        //return supplier;
    }

    public Supplier createSupplierWithDelivery(String name, int supplierId, int bankAccount, List<DaysOfTheWeek> deliveryDays) {
        Supplier supplier = new SupplierWithDeliveryDays(name, supplierId, bankAccount, deliveryDays);
        supplierController.addSupplier(supplierId, supplier);
        return supplier;
    }

    public Supplier createSupplierNeedsPickup(String name, String address, int supplierId, int bankAccount) {
        Supplier supplier = new SupplierNeedsPickup(name, address, supplierId, bankAccount);
        supplierController.addSupplier(supplierId, supplier);
        return supplier;
    }

    public Supplier getSupplier(String supplierId) {
        return supplierController.getSupplierById(supplierId);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierController.getAllSuppliers();
    }

    public boolean removeSupplier(int supplierId) {
        return supplierController.removeSupplier(supplierId);
    }

    public void addContactPersonToSupplier(int supplierId, String name, int phoneNumber) {
        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier != null) {
            ContactPerson contactPerson = new ContactPerson(name, phoneNumber);
            supplier.addContactPerson(contactPerson);
        }
    }

    public void addAgreementToSupplier(int supplierId, Agreement agreement) {
        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier != null) {
            supplier.addAgreement(agreement);
        }
    }

    public List<Product> getSupplierProducts(int supplierId) {
        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier != null) {
            return supplier.getAvailableProducts();
        }
        return new ArrayList<>();
    }

    public List<Agreement> getValidAgreements(int supplierId) {
        Supplier supplier = supplierController.getSupplierById(supplierId);
        if (supplier != null) {
            return supplier.getValidAgreements();
        }
        return new ArrayList<>();
    }

    public boolean addSupplierWithDelivery(String name, String id, String bankAccount, String deliveryDays) {
        try{
            return supplierController.addSupplierWithDelivery(name, id, bankAccount, deliveryDays);
        } catch (Exception e) {
            return false;
        }
    }

    public boolean addSupplierNeedsPickup(String name, String id, String bankAccount, String address) {
        try{
            return supplierController.addSupplierNeedsPickup(name, id, bankAccount, address);
        } catch (Exception e) {
            return false;
        }
    }
}