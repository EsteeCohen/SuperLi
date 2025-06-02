package transportDev.src.main.repositories;

import java.util.List;
import transportDev.src.main.entities.User;
import transportDev.src.main.enums.UserRole;

public interface UserRepository {
    User add(String id, String username, String password, String fullName, UserRole role);
    User findById(String id);
    User findByUsername(String username);
    List<User> findAll();
    List<User> findByRole(UserRole role);
    List<User> findActiveUsers();
    User authenticate(String username, String password);
    boolean updateUser(String id, String username, String password, String fullName, UserRole role);
    boolean updatePassword(String userId, String newPassword);
    boolean deactivateUser(String id);
} 