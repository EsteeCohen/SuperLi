package domainLayer;

import java.util.List;

public interface RoleRepository {
    public List<Role> getAllRoles();
    public Role getRoleByName(String name);
    public void add(Role role);
    public void delete(Role role);
}
