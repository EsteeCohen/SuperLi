package src.ServiceLayer;

public class SupplierService {
    private static SupplierService instance;
    private SupplierRepository supplierRepository;

    private SupplierService() {
        supplierRepository = SupplierRepository.getInstance();
    }

    public static synchronized SupplierService getInstance() {
        if (instance == null) {
            instance = new SupplierService();
        }
        return instance;
    }

    public Supplier createSupplier(String name, int bankAccount) {
        int companyId = supplierRepository.generateSupplierId();
        Supplier supplier = new Supplier(name, companyId, bankAccount);
        supplierRepository.addSupplier(supplier);
        return supplier;
    }

    public Supplier getSupplier(int companyId) {
        return supplierRepository.getSupplier(companyId);
    }

    public List<Supplier> getAllSuppliers() {
        return supplierRepository.getAllSuppliers();
    }

    public boolean removeSupplier(int companyId) {
        return supplierRepository.removeSupplier(companyId);
    }

    public List<Supplier> findSuppliersByName(String name) {
        return supplierRepository.findSuppliersByName(name);
    }

    public void addContactPersonToSupplier(int companyId, String name, String email, int phoneNumber) {
        Supplier supplier = supplierRepository.getSupplier(companyId);
        if (supplier != null) {
            ContactPerson contactPerson = new ContactPerson(name, email, phoneNumber);
            supplier.addContactPerson(contactPerson);
        }
    }

    public void addAgreementToSupplier(int companyId, Agreement agreement) {
        Supplier supplier = supplierRepository.getSupplier(companyId);
        if (supplier != null) {
            supplier.addAgreement(agreement);
        }
    }

    public List<Product> getSupplierProducts(int companyId) {
        Supplier supplier = supplierRepository.getSupplier(companyId);
        if (supplier != null) {
            return supplier.getAvailableProducts();
        }
        return new ArrayList<>();
    }

    public List<Agreement> getValidAgreements(int companyId) {
        Supplier supplier = supplierRepository.getSupplier(companyId);
        if (supplier != null) {
            return supplier.getValidAgreements();
        }
        return new ArrayList<>();
    }
}
