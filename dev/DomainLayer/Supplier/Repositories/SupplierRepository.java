package DomainLayer.Supplier.Repositories;

import DataAccessLayer.DTO.*;
import DataAccessLayer.interfacesDAO.*;
import DomainLayer.Supplier.*;
import DataAccessLayer.*;

import java.sql.SQLException;
import java.util.ArrayList;
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

    /*
    public void addSupplier(Supplier supplier) {
        supplierDAO.create(new SupplierDTO(supplier));
        for (ContactPerson contactPerson : supplier.getContactPersons()) {
            contactPersonDAO.create(new ContactPersonDTO(contactPerson, supplier.getSupplierId()));
        }
    }
     */

    public void addDeliverySupplier(SupplierWithDeliveryDays supplier) {
        // create DeliverySupplierDTO and persist
        supplierDAO.create(new DeliverySupplierDTO(supplier));

        // persist contact persons
        for (ContactPerson cp : supplier.getContactPersons()) {
            contactPersonDAO.create(new ContactPersonDTO(cp, supplier.getSupplierId()));
        }
    }

    public void addPickupSupplier(SupplierNeedsPickup supplier) {
        // create PickupSupplierDTO and persist
        supplierDAO.create(new PickupSupplierDTO(supplier));

        // persist contact persons
        for (ContactPerson cp : supplier.getContactPersons()) {
            contactPersonDAO.create(new ContactPersonDTO(cp, supplier.getSupplierId()));
        }
    }

    public Supplier getSupplierById(String supplierId) {
        SupplierDTO dto = supplierDAO.read(supplierId);
        if (dto == null) return null;
        return dto.toDomain();
    }

    public List<Supplier> getAllSuppliers() {
        List<SupplierDTO> dtos = supplierDAO.readAll();
        List<Supplier> suppliers = new ArrayList<>();
        for (SupplierDTO dto : dtos) {
            Supplier supplier = dto.toDomain();
            List<ContactPerson> contactPersons = new ArrayList<>();
            for(ContactPersonDTO cp : contactPersonDAO.readAllBySupplier(supplier.getSupplierId()) )
                contactPersons.add(cp.toDomain());
            supplier.setContactPersons(contactPersons);
            List<Agreement> agreements= new ArrayList<>();
            for (AgreementDTO agreementDTO: agreementDAO.readAllBySupplier(supplier.getSupplierId()))
                agreements.add(agreementDTO.toDomain());
            supplier.setAgreements(agreements);
            for(ProductDTO p : productDAO.readAllBySupplier(supplier.getSupplierId()))
                supplier.addProductToCatalog(p.toDomain());


            suppliers.add(supplier);
        }
        return suppliers;
    }


    public List<Supplier> getAllSuppliersFromDB() {
        List<Supplier> suppliers = new ArrayList<>();
        for (SupplierDTO dto : supplierDAO.readAll()) {
            Supplier s = dto.toDomain();

            // מלא אנשי קשר
            List<ContactPerson> cps = new ArrayList<>();
            for (ContactPersonDTO cpd : contactPersonDAO.readAllBySupplier(s.getSupplierId())) {
                cps.add(cpd.toDomain());
            }
            s.setContactPersons(cps);

            // מלא הסכמים
            List<Agreement> ags = new ArrayList<>();
            for (AgreementDTO ad : agreementDAO.readAllBySupplier(s.getSupplierId())) {
                ags.add(ad.toDomain());
            }
            s.setAgreements(ags);

            // מלא מוצרים
            for (ProductDTO pd : productDAO.readAllBySupplier(s.getSupplierId())) {
                s.addProductToCatalog(pd.toDomain());
            }

            suppliers.add(s);
        }
        return suppliers;
    }


    /*public List<String> getAllSupplierStrings() {
        List<String> all = new ArrayList<>();
        for (Supplier s : getAllSuppliersFromDB()) {
            all.add(s.toString());
        }
        return all;
    }*/


    public void updateSupplier(Supplier supplier) {
        if (supplier instanceof SupplierWithDeliveryDays d) {
            supplierDAO.update(new DeliverySupplierDTO(d));
        } else if (supplier instanceof SupplierNeedsPickup p) {
            supplierDAO.update(new PickupSupplierDTO(p));
        } else {
            throw new IllegalArgumentException("Unknown supplier type: " + supplier.getSupplierType()
            );
        }
    }

    public void removeSupplier(String supplierId) {
        contactPersonDAO.deleteBySupplier(supplierId);
        productDAO.deleteBySupplier(supplierId);
        agreementDAO.deleteBySupplier(supplierId);
        supplierDAO.delete(supplierId);
    }

    public void addProductToSupplier(Product product) {
        productDAO.create(new ProductDTO(product));
    }

    public void updateProduct(Product product) {
        productDAO.update(new ProductDTO(product));
    }

    public Product getProductBySupplierAndCatalog(String supplierId, String catalogNumber) {
        ProductDTO dto = productDAO.readBySupplierAndCatalog(supplierId, catalogNumber);
        return dto.toDomain();
    }

    public void updateSupplierName(String supplierId, String name) {
        supplierDAO.updateName(supplierId, name);
    }

    public void updateSupplierBankAccount(String supplierId, String bankAccount) {
        supplierDAO.updateBankAccount(supplierId, bankAccount);
    }

    public void updateSupplierId(String oldId, String newId) {
        supplierDAO.updateSupplierId(oldId, newId);
    }

    public void updateSupplierDeliveryDays(String supplierId, List<String> days) {
        supplierDAO.updateDeliveryDays(supplierId, days);
    }

    public void updateSupplierPickupAddress(String supplierId, String address) {
        supplierDAO.updatePickupAddress(supplierId, address);
    }

    public void addContactPerson(String supplierId, ContactPerson cp) {

        contactPersonDAO.create(new ContactPersonDTO(cp, supplierId));
    }

    public void removeProductFromSupplier(String supplierId, String catalogNumber) {
        productDAO.delete(catalogNumber);
    }
}