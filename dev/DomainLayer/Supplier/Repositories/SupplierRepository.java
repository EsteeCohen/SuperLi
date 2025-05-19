package DomainLayer.Supplier.Repositories;

import DataAccessLayer.interfacesDAO.*;
import DomainLayer.Supplier.ContactPerson;
import DomainLayer.Supplier.Supplier;
import DomainLayer.Supplier.Product;
import DataAccessLayer.*;

import java.sql.SQLException;
import java.util.List;

// The repository coordinates the data from DAOs and returns full domain objects
public class SupplierRepository {
    private final SupplierDAO supplierDAO;
    private final ProductDAO productDAO;
    private final AgreementDAO agreementDAO;
    private final ContactPersonDAO contactPersonDAO;

    public SupplierRepository() throws SQLException {
        this.supplierDAO = new SupplierDAOImpl();
        this.productDAO = new ProductDAOImpl();
        this.agreementDAO = new AgreementDAOImpl();
        this.contactPersonDAO = new ContactPersonDAOImpl();
    }

    public void addSupplier(Supplier supplier) {
        supplierDAO.create(supplier);
        for (ContactPerson cp : supplier.getContactPersons()) {
            contactPersonDAO.create(cp, supplier.getSupplierId());
        }
        // מוצרים והסכמים מתווספים דרך הריפוזיטורי שלהם, לא כאן.
    }

    public Supplier getSupplierById(String supplierId) {
        Supplier supplier = supplierDAO.read(supplierId);
        if (supplier == null) return null;
        // שליפת אנשי קשר
        supplier.setContactPersons(contactPersonDAO.readAllBySupplier(supplierId));
        // שליפת הסכמים
        supplier.setAgreements(agreementDAO.readAllBySupplier(supplierId));
        // שליפת מוצרים
        List<Product> products = productDAO.readAllBySupplier(supplierId);
        for (Product p : products) {
            supplier.addProductToCatalog(p);
        }
        return supplier;
    }

    public List<Supplier> getAllSuppliers() {
        List<Supplier> suppliers = supplierDAO.readAll();
        for (Supplier supplier : suppliers) {
            supplier.setContactPersons(contactPersonDAO.readAllBySupplier(supplier.getSupplierId()));
            supplier.setAgreements(agreementDAO.readAllBySupplier(supplier.getSupplierId()));
            List<Product> products = productDAO.readAllBySupplier(supplier.getSupplierId());
            for (Product p : products) {
                supplier.addProductToCatalog(p);
            }
        }
        return suppliers;
    }

    public void updateSupplier(Supplier supplier) {
        supplierDAO.update(supplier);
        // אפשר להוסיף עדכון contact persons
        // ועדכון מוצרים/הסכמים בריפוזיטורי המתאים
    }

    public void removeSupplier(String supplierId) {
        contactPersonDAO.deleteBySupplier(supplierId);
        productDAO.deleteBySupplier(supplierId);
        agreementDAO.deleteBySupplier(supplierId);
        supplierDAO.delete(supplierId);
    }
}