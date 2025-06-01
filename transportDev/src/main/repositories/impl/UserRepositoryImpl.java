package src.main.repositories.impl;

import src.main.repositories.UserRepository;
import src.main.dao.UserDAO;
import src.main.entities.User;
import src.main.enums.UserRole;
import java.util.List;

public class UserRepositoryImpl implements UserRepository {
    private UserDAO userDAO;
    
    public UserRepositoryImpl(UserDAO userDAO) {
        this.userDAO = userDAO;
    }
    
    @Override
    public User add(String id, String username, String password, String fullName, UserRole role) {
        if (userDAO.read(id) != null || userDAO.getByUsername(username) != null) {
            return null; // User already exists
        }
        User user = new User(id, username, password, fullName, role);
        userDAO.create(user);
        return user;
    }
    
    @Override
    public User findById(String id) {
        return userDAO.read(id);
    }
    
    @Override
    public User findByUsername(String username) {
        return userDAO.getByUsername(username);
    }
    
    @Override
    public List<User> findAll() {
        return userDAO.getAll();
    }
    
    @Override
    public List<User> findByRole(UserRole role) {
        return userDAO.getByRole(role);
    }
    
    @Override
    public List<User> findActiveUsers() {
        return userDAO.getAll().stream()
            .filter(User::isActive)
            .collect(java.util.stream.Collectors.toList());
    }
    
    @Override
    public User authenticate(String username, String password) {
        User user = userDAO.getByUsername(username);
        if (user != null && user.checkPassword(password) && user.isActive()) {
            return user;
        }
        return null;
    }
    
    @Override
    public boolean updateUser(String id, String username, String password, String fullName, UserRole role) {
        User user = userDAO.read(id);
        if (user == null) return false;
        
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setRole(role);
        userDAO.update(user);
        return true;
    }
    
    @Override
    public boolean updatePassword(String userId, String newPassword) {
        User user = userDAO.read(userId);
        if (user == null) return false;
        
        user.setPassword(newPassword);
        userDAO.update(user);
        return true;
    }
    
    @Override
    public boolean deactivateUser(String id) {
        User user = userDAO.read(id);
        if (user == null) return false;
        
        user.setActive(false);
        userDAO.update(user);
        return true;
    }
} 