package employeeDev.src.unitTests;

import employeeDev.src.domainLayer.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoleFacadeTest {
    private static final String TEST_ROLE_1 = "tests";
    private static final String TEST_ROLE_2 = "tests2";

    private RoleFacade roleFacade;
    private SiteFacade siteFacade;
    private EmployeeFacade employeeFacade;
    private ShiftFacade shiftFacade;
    
    @BeforeEach
    void setUp() {
        roleFacade = new RoleFacade();
        siteFacade = new SiteFacade();
        employeeFacade = new EmployeeFacade(roleFacade, siteFacade);
        shiftFacade = new ShiftFacade(employeeFacade, siteFacade, roleFacade);
        new AvailabilityFacade(shiftFacade, employeeFacade, siteFacade);
    }

    @AfterEach
    void cleanUp() {
        employeeDev.src.dataAcssesLayer.RoleDAO dao = new employeeDev.src.dataAcssesLayer.RoleDAO();
        dao.deleteRole(TEST_ROLE_1);
        dao.deleteRole(TEST_ROLE_2);
    }

    @Test
    void testAddRoleSuccess() {
        assertTrue(roleFacade.add(TEST_ROLE_1));
        assertNotNull(roleFacade.getRoleByName(TEST_ROLE_1));
    }

    @Test
    void testAddRoleDuplicate() {
        assertTrue(roleFacade.add(TEST_ROLE_1));
        assertFalse(roleFacade.add(TEST_ROLE_1));
    }

    @Test
    void testGetRoleByName() {
        roleFacade.add(TEST_ROLE_1);
        assertNotNull(roleFacade.getRoleByName(TEST_ROLE_1));
        assertNull(roleFacade.getRoleByName("NotExist"));
    }

    @Test
    void testDeleteRole() {
        roleFacade.add(TEST_ROLE_1);
        assertNotNull(roleFacade.getRoleByName(TEST_ROLE_1));
        roleFacade.delete(TEST_ROLE_1);
        assertNull(roleFacade.getRoleByName(TEST_ROLE_1));
    }

    @Test
    void testGetAllRoles() {
        roleFacade.add(TEST_ROLE_1);
        roleFacade.add(TEST_ROLE_2);
        List<RoleDL> roles = roleFacade.getAllRoles();
        assertEquals(2, roles.size());
    }

    @Test
    void testLoadRolesFromDB() {
        roleFacade.add(TEST_ROLE_1);
        roleFacade.add(TEST_ROLE_2);
        roleFacade = new RoleFacade();
        roleFacade.loadRolesFromDB();
        assertNotNull(roleFacade.getRoleByName(TEST_ROLE_1));
        assertNotNull(roleFacade.getRoleByName(TEST_ROLE_2));
    }
}