package transportDev.src.main.dao;

import java.util.List;
import transportDev.src.main.entities.User;
import transportDev.src.main.enums.UserRole;

public interface UserDAO {
    void create(User user);
    User read(String id);
    void update(User user);
    void delete(String id);
    List<User> getAll();
    User getByUsername(String username);
    List<User> getByRole(UserRole role);
} 