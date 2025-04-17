package domainLayer;

import java.util.Dictionary;
import java.util.Hashtable;

public class EmployeeFacade {
    private Dictionary<String, Employee> employees;

    // Singleton
    private static EmployeeFacade instance = null;
    public static EmployeeFacade getInstance() {
        if (instance == null) {
            instance = new EmployeeFacade();
        }
        return instance;
    }

    private EmployeeFacade() {
        this.employees = new Hashtable<String, Employee>();
    }

    public void addEmployee(Employee e) {
        if (e != null) {
            employees.put(e.getId(), e);
        }
    }

    public Employee getEmployee(String id) {
        return employees.get(id);
    }

    public boolean employeeHasRole(String id, String roleName) {
        Employee employee = employees.get(id);
        if (employee != null) {
            return employee.hasRole(RoleFacade.getInstance().getRoleByName(roleName));
        }
        return false;
    }
}
