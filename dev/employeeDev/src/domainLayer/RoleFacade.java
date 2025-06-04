package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.RoleDAO;
import employeeDev.src.dtos.RoleDTO;
import employeeDev.src.mappers.RoleMapper;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class RoleFacade{
    private Hashtable<String, RoleDL> roles;

    public RoleFacade() {
        this.roles = new Hashtable<String, RoleDL>();
    }


    public boolean add(String roleName) {
        RoleDL role = new RoleDL(roleName);
        if (!roles.containsKey(role.getName())) {
            roles.put(role.getName(), role);
            return true;
        }
        return false;
    }

    public RoleDL getRoleByName(String name) {
        return roles.get(name);
    }

    public void delete(String name) {
        if (roles.containsKey(name)) {
            roles.remove(name);
        }
    }

    public List<RoleDL> getAllRoles() {
        return new ArrayList<>(roles.values());
    }

    // loads all roles from the database and adds them to the roles hashtable in the facade
    public void loadRolesFromDB() {
        RoleDAO roleDAO = new RoleDAO();
        List<RoleDTO> roleDTOs = roleDAO.getAllRoles();
        for (RoleDTO roleDTO : roleDTOs) {
            RoleDL role = RoleMapper.fromDTO(roleDTO);
            if (!roles.containsKey(role.getName())) {
                roles.put(role.getName(), role);
            }
        }
    }
}
