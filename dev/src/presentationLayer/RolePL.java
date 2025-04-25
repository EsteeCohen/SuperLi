package presentationLayer;

import serviceLayer.RoleSL;

public class RolePL {
    private final String name;

    public RolePL(String name) {
        this.name = name;
    }

    public RolePL(RoleSL role) {
        this.name = role.getName();
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
