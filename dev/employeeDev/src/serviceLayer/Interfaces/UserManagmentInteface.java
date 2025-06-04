package employeeDev.src.serviceLayer.Interfaces;

import employeeDev.src.serviceLayer.EmployeeSL;
import java.util.List;

public interface UserManagmentInteface {

    EmployeeSL login(String id, String password);

    EmployeeSL getUserById(String id);

    List<EmployeeSL> getAllUsers();

    boolean hasRole(String userId, String roleName);

}
