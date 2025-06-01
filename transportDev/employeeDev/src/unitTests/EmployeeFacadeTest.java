package unitTests;

import domainLayer.EmployeeDL;
import domainLayer.EmployeeFacade;
import domainLayer.RoleDL;
import domainLayer.RoleFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeFacadeTest {

    private EmployeeFacade employeeFacade;
    private RoleFacade roleFacade;

    @BeforeEach
    void setUp() {
        roleFacade = new RoleFacade(); // Use real RoleFacade
        employeeFacade = new EmployeeFacade(roleFacade);
    }

    @Test
    void testAddEmployee() {
        EmployeeDL employee = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        employeeFacade.addEmployee(employee);

        assertEquals(employee, employeeFacade.getEmployee("E001"));
    }

    @Test
    void testGetEmployee() {
        EmployeeDL employee = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        employeeFacade.addEmployee(employee);

        EmployeeDL retrievedEmployee = employeeFacade.getEmployee("E001");
        assertNotNull(retrievedEmployee);
        assertEquals("John Doe", retrievedEmployee.getFullName());
    }

    @Test
    void testEmployeeHasRole() {
        EmployeeDL employee = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        RoleDL role = new RoleDL("Manager");

        roleFacade.add("Manager"); // Add role to RoleFacade
        employeeFacade.addEmployee(employee);
        employeeFacade.assignRoleToEmployee("E001", "Manager");

        assertTrue(employeeFacade.employeeHasRole("E001", "Manager"));
    }

    @Test
    void testLoginSuccess() {
        EmployeeDL employee = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        employeeFacade.addEmployee(employee);

        EmployeeDL loggedInEmployee = employeeFacade.login("E001", "password");
        assertNotNull(loggedInEmployee);
        assertEquals("John Doe", loggedInEmployee.getFullName());
    }

    @Test
    void testLoginFailure() {
        EmployeeDL employee = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        employeeFacade.addEmployee(employee);

        EmployeeDL loggedInEmployee = employeeFacade.login("E001", "wrongpassword");
        assertNull(loggedInEmployee);
    }

    @Test
    void testRegisterEmployee() {
        boolean result = employeeFacade.registerEmployee("John Doe", "password", "E001", 5000, 'H', 10, 15);
        assertTrue(result);

        EmployeeDL employee = employeeFacade.getEmployee("E001");
        assertNotNull(employee);
        assertEquals("John Doe", employee.getFullName());
    }

    @Test
    void testRegisterEmployeeDuplicateId() {
        employeeFacade.registerEmployee("John Doe", "password", "E001", 5000, 'H', 10, 15);
        boolean result = employeeFacade.registerEmployee("Jane Doe", "password", "E001", 6000, 'M', 12, 20);

        assertFalse(result); // Duplicate ID should not allow registration
    }

    @Test
    void testAssignRoleToEmployee() {
        EmployeeDL employee = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        RoleDL role = new RoleDL("Manager");

        roleFacade.add("Manager"); // Add role to RoleFacade
        employeeFacade.addEmployee(employee);
        boolean result = employeeFacade.assignRoleToEmployee("E001", "Manager");

        assertTrue(employee.getRoles().contains(role) && result);
    }

    @Test
    void testAssignRoleToNonexistentEmployee() {
        roleFacade.add("Manager"); // Add role to RoleFacade

        boolean result = employeeFacade.assignRoleToEmployee("E999", "Manager");
        assertFalse(result); // Employee does not exist
    }

    @Test
    void testGetAllEmployees() {
        EmployeeDL employee1 = new EmployeeDL("E001", "password", "John Doe", LocalDate.now(), 5000, 'H', 10, 15);
        EmployeeDL employee2 = new EmployeeDL("E002", "password", "Jane Doe", LocalDate.now(), 6000, 'G', 12, 20);

        employeeFacade.addEmployee(employee1);
        employeeFacade.addEmployee(employee2);

        List<EmployeeDL> employees = employeeFacade.getAllEmployees();
        assertEquals(2, employees.size());
        assertTrue(employees.contains(employee1));
        assertTrue(employees.contains(employee2));
    }
}
