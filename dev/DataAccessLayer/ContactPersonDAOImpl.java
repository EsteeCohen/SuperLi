package DataAccessLayer;

import DataAccessLayer.DTO.ContactPersonDTO;
import DataAccessLayer.interfacesDAO.ContactPersonDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactPersonDAOImpl implements ContactPersonDAO {
    private final Connection connection;

    public ContactPersonDAOImpl() throws SQLException {
        this.connection = DatabaseConnection.getConnection();
    }

    @Override
    public void create(ContactPersonDTO dto) {
        String sql = "INSERT INTO SupplierContacts (supplier_id, contact_name, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, dto.getSupplierId());
            stmt.setString(2, dto.getContactName());
            stmt.setString(3, dto.getPhoneNumber());
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<ContactPersonDTO> readAllBySupplier(String supplierId) {
        List<ContactPersonDTO> list = new ArrayList<>();
        String sql = "SELECT supplier_id, contact_name, phone_number FROM SupplierContacts WHERE supplier_id=?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                list.add(new ContactPersonDTO(
                        rs.getString("supplier_id"),
                        rs.getString("contact_name"),
                        rs.getString("phone_number")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
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
