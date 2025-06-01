package src.main.dao;

import src.main.entities.User;
import src.main.enums.UserRole;
import java.util.List;

public interface UserDAO {
    void create(User user);
    User read(String id);
    void update(User user);
    void delete(String id);
    List<User> getAll();
    User getByUsername(String username);
    List<User> getByRole(UserRole role);
} 