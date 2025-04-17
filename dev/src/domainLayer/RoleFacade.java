package domainLayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RoleFacade implements RoleRepository {
    private Hashtable<String, Role> roles;
    
    // Singleton
    private static RoleFacade instance = null;
    public static RoleFacade getInstance() {
        if (instance == null) {
            instance = new RoleFacade();
        }
        return instance;
    }

    private RoleFacade() {
        this.roles = new Hashtable<String, Role>();
    }

    @Override
    public void add(Role role) {
        if (role != null) {
            roles.put(role.getName(), role);
        }
    }

    @Override
    public Role getRoleByName(String name) {
        return roles.get(name);
    }

    @Override
    public void delete(Role role) {
        if (role != null) {
            roles.remove(role.getName());
        }
    }

    @Override
    public List<Role> getAllRoles() {
        return new ArrayList<>(roles.values());
    }
}
