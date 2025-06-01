package src.main.repositories;

import src.main.entities.User;
import src.main.enums.UserRole;
import java.util.List;

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