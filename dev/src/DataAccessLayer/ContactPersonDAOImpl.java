package DataAccessLayer;

import DataAccessLayer.DTO.ContactPersonDTO;
import DataAccessLayer.interfacesDAO.ContactPersonDAO;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ContactPersonDAOImpl implements ContactPersonDAO {

    public ContactPersonDAOImpl() {
    }

    @Override
    public void create(ContactPersonDTO dto) {
        String sql = "INSERT OR IGNORE INTO ContactPersons (supplier_id, contact_name, phone_number) VALUES (?, ?, ?)";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
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
        String sql = "SELECT supplier_id, contact_name, phone_number FROM ContactPersons WHERE supplier_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
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
        String sql = "DELETE FROM ContactPersons WHERE supplier_id=?";
        try (PreparedStatement stmt = DatabaseConnection.getValidConnection().prepareStatement(sql)) {
            stmt.setString(1, supplierId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
