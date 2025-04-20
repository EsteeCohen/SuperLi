package serviceLayer;

import domainLayer.RoleFacade;
import java.util.List;

public class RoleService {
    private RoleFacade roleFacade;

    public RoleService(RoleFacade roleFacade) {
        this.roleFacade = roleFacade;
    }

    public List<RoleSL> getAllRoles() {
        return roleFacade.getAllRoles().stream()
                .map(RoleSL::new)
                .toList();
    }

    public void createRole(String roleName) {
       roleFacade.add(roleName);
    }

    public void deleteRole(String name) {
        roleFacade.delete(name);
    }

}
