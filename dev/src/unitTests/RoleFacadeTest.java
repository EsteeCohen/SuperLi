package unitTests;

import domainLayer.RoleDL;
import domainLayer.RoleFacade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleFacadeTest {

    private RoleFacade roleFacade;

    @BeforeEach
    void setUp() {
        roleFacade = new RoleFacade();
    }

    @Test
    void testAddRole() {
        roleFacade.add("Manager");
        RoleDL role = roleFacade.getRoleByName("Manager");

        assertNotNull(role);
        assertEquals("Manager", role.getName());
    }

    @Test
    void testAddDuplicateRole() {
        roleFacade.add("Manager");
        roleFacade.add("Manager"); // Attempt to add the same role again

        List<RoleDL> roles = roleFacade.getAllRoles();
        assertEquals(1, roles.size()); // Ensure only one instance of the role exists
    }

    @Test
    void testGetRoleByName() {
        roleFacade.add("Manager");
        RoleDL role = roleFacade.getRoleByName("Manager");

        assertNotNull(role);
        assertEquals("Manager", role.getName());
    }

    @Test
    void testGetRoleByNameNotFound() {
        RoleDL role = roleFacade.getRoleByName("NonexistentRole");

        assertNull(role); // Ensure null is returned for a nonexistent role
    }

    @Test
    void testDeleteRole() {
        roleFacade.add("Manager");
        roleFacade.delete("Manager");

        RoleDL role = roleFacade.getRoleByName("Manager");
        assertNull(role); // Ensure the role is deleted
    }

    @Test
    void testDeleteNonexistentRole() {
        roleFacade.delete("NonexistentRole"); // Attempt to delete a role that doesn't exist

        // Ensure no exceptions are thrown and the roles list remains empty
        List<RoleDL> roles = roleFacade.getAllRoles();
        assertTrue(roles.isEmpty());
    }

    @Test
    void testGetAllRoles() {
        roleFacade.add("Manager");
        roleFacade.add("Developer");

        List<RoleDL> roles = roleFacade.getAllRoles();

        assertEquals(2, roles.size());
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals("Manager")));
        assertTrue(roles.stream().anyMatch(role -> role.getName().equals("Developer")));
    }
}
