package domainLayer;

import java.util.Dictionary;
import java.util.Hashtable;

public class EmployeeFacade {
    private Dictionary<String, EmployeeSL> employees;

    // Singleton
    private static EmployeeFacade instance = null;
    public static EmployeeFacade getInstance() {
        if (instance == null) {
            instance = new EmployeeFacade();
        }
        return instance;
    }

    private EmployeeFacade() {
        this.employees = new Hashtable<String, EmployeeSL>();
    }

    public void addEmployee(EmployeeSL e) {
        if (e != null) {
            employees.put(e.getId(), e);
        }
    }

    public EmployeeSL getEmployee(String id) {
        return employees.get(id);
    }

    public boolean employeeHasRole(String id, String roleName) {
        EmployeeSL employee = employees.get(id);
        if (employee != null) {
            return employee.hasRole(RoleFacade.getInstance().getRoleByName(roleName));
        }
        return false;
    }
}
