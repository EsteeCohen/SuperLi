package DataAccessLayer;

import DataAccessLayer.interfacesDAO.ContactPersonDAO;
import DomainLayer.Supplier.ContactPerson;
import DataAccessLayer.DatabaseConnection;
import java.sql.*;
import java.util.*;

public class ContactPersonDAOImpl implements ContactPersonDAO {
    private final Connection connection;

    public ContactPersonDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(ContactPerson contact, String supplierId) {
        String sql = "INSERT INTO SupplierContacts (supplier_id, contact_name, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.setString(2, contact.getContactName());
            stmt.setString(3, contact.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ContactPerson> readAllBySupplier(String supplierId) {
        List<ContactPerson> contacts = new ArrayList<>();
        String sql = "SELECT * FROM SupplierContacts WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                contacts.add(new ContactPerson(
                        rs.getString("contact_name"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contacts;
    }

    @Override
    public void deleteBySupplier(String supplierId) {
        String sql = "DELETE FROM SupplierContacts WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
