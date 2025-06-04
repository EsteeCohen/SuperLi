package employeeDev.src.domainLayer;

import employeeDev.src.dataAcssesLayer.RoleDAO;
import employeeDev.src.mappers.RoleMapper;

public class RoleDL {
    private final String name;

    public RoleDL(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RoleDL roleDL = (RoleDL) obj;
        return name.equals(roleDL.name);
    }
    public void presistIntoDB() {
        RoleDAO roleDAO = new RoleDAO();
        roleDAO.addOrReplaceRole(RoleMapper.toDTO(this));
    }
}
