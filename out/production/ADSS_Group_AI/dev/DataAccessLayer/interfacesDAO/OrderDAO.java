package DataAccessLayer.interfacesDAO;


import DomainLayer.Supplier.Order;
import java.util.List;

public interface OrderDAO {
    void create(Order order);
    Order read(int orderId);
    List<Order> readAll();
    List<Order> readAllBySupplier(String supplierId);
    void update(Order order);
    void delete(int orderId);
    void deleteBySupplier(String supplierId);
}