package DataAccessLayer.interfacesDAO;

import DataAccessLayer.DTO.OrderDTO;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public interface OrderDAO {
    void create(OrderDTO order);
    OrderDTO read(int orderId);
    List<OrderDTO> readAll();
    List<OrderDTO> readAllBySupplier(String supplierId);
    void update(OrderDTO order);
    void delete(int orderId);
    void deleteBySupplier(String supplierId);

    List<OrderDTO> readAllPeriodic();

    List<OrderDTO> readAllNotPeriodic();
}
