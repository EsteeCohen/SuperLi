package domainLayer;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RoleFacade{
    private Hashtable<String, RoleDL> roles;

    private RoleFacade() {
        this.roles = new Hashtable<String, RoleDL>();
    }


    public void add(RoleDL role) {
        if (role != null) {
            roles.put(role.getName(), role);
        }
    }

    public RoleDL getRoleByName(String name) {
        return roles.get(name);
    }

    public void delete(RoleDL role) {
        if (role != null) {
            roles.remove(role.getName());
        }
    }

    public List<RoleDL> getAllRoles() {
        return new ArrayList<>(roles.values());
    }
}
