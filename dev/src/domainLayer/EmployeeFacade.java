package domainLayer;

import java.util.Dictionary;
import java.util.Hashtable;

public class EmployeeFacade {
    private Dictionary<String, EmployeeDL> employees;

    // Singleton
    private static EmployeeFacade instance = null;
    public static EmployeeFacade getInstance() {
        if (instance == null) {
            instance = new EmployeeFacade();
        }
        return instance;
    }

    private EmployeeFacade() {
        this.employees = new Hashtable<String, EmployeeDL>();
    }

    public void addEmployee(EmployeeDL e) {
        if (e != null) {
            employees.put(e.getId(), e);
        }
    }

    public EmployeeDL getEmployee(String id) {
        return employees.get(id);
    }

    public boolean employeeHasRole(String id, String roleName) {
        EmployeeDL employee = employees.get(id);
        if (employee != null) {
            return employee.hasRole(RoleFacade.getInstance().getRoleByName(roleName));
        }
        return false;
    }
}
