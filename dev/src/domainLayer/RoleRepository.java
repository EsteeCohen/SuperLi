package domainLayer;

import java.util.List;

public interface RoleRepository {
    public List<RolePL> getAllRoles();
    public RolePL getRoleByName(String name);
    public void add(RolePL role);
    public void delete(RolePL role);
}
