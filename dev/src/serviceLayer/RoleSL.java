package serviceLayer;

import domainLayer.RoleDL;

public class RoleSL {
    private final String name;

    public RoleSL(String name) {
        this.name = name;
    }

    public RoleSL(RoleDL role) {
        this.name = role.getName();
    }
    
    public String getName() {
        return name;
    }
}
