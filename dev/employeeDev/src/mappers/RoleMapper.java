package employeeDev.src.mappers;

import employeeDev.src.domainLayer.RoleDL;
import employeeDev.src.dtos.RoleDTO;

public class RoleMapper {
    
    public static RoleDL fromDTO(RoleDTO roleDTO) {
        RoleDL role = new RoleDL(roleDTO.getName());
        return role;
    }

    public static RoleDTO toDTO(RoleDL role) {
        RoleDTO roleDTO = new RoleDTO(role.getName());
        return roleDTO;
    }
}

