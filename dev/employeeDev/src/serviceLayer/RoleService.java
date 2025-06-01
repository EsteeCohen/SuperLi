package employeeDev.src.serviceLayer;

import employeeDev.src.domainLayer.RoleFacade;
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

    public boolean createRole(String roleName) {
       return roleFacade.add(roleName);
    }

    public void deleteRole(String name) {
        roleFacade.delete(name);
    }

}
