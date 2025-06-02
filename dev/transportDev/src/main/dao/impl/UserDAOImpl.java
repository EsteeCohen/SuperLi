package transportDev.src.main.dao.impl;

import transportDev.src.main.dao.UserDAO;
import transportDev.src.main.entities.User;
import transportDev.src.main.enums.UserRole;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDAOImpl implements UserDAO {
    private Map<String, User> users = new HashMap<>();
    
    @Override
    public void create(User user) {
        users.put(user.getId(), user);
    }
    
    @Override
    public User read(String id) {
        return users.get(id);
    }
    
    @Override
    public void update(User user) {
        users.put(user.getId(), user);
    }
    
    @Override
    public void delete(String id) {
        users.remove(id);
    }
    
    @Override
    public List<User> getAll() {
        return new ArrayList<>(users.values());
    }
    
    @Override
    public User getByUsername(String username) {
        return users.values().stream()
            .filter(user -> user.getUsername().equals(username))
            .findFirst()
            .orElse(null);
    }
    
    @Override
    public List<User> getByRole(UserRole role) {
        return users.values().stream()
            .filter(user -> user.getRole() == role)
            .collect(java.util.stream.Collectors.toList());
    }
} 