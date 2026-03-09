package src.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import src.main.entities.User;
import src.main.enums.UserRole;
import src.main.services.UserService;

import static org.junit.jupiter.api.Assertions.*;

public class UserServiceTests {

    private UserService userService;
    private String adminSession;
    private String managerSession;
    private String driverSession;
    private String viewerSession;

    @BeforeEach
    public void setUp() {
        userService = new UserService(); // seeds admin/manager/driver/viewer
        User admin   = userService.authenticateUser("admin",   "admin123");
        User manager = userService.authenticateUser("manager", "manager123");
        User driver  = userService.authenticateUser("driver1", "driver123");
        User viewer  = userService.authenticateUser("viewer",  "viewer123");

        adminSession   = userService.createSession(admin.getId());
        managerSession = userService.createSession(manager.getId());
        driverSession  = userService.createSession(driver.getId());
        viewerSession  = userService.createSession(viewer.getId());
    }

    // --- Authentication ---

    @Test
    public void testLoginWithCorrectPassword() {
        User user = userService.authenticateUser("admin", "admin123");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
    }

    @Test
    public void testLoginWithWrongPassword() {
        User user = userService.authenticateUser("admin", "wrongpassword");
        assertNull(user);
    }

    @Test
    public void testLoginWithUnknownUser() {
        assertNull(userService.authenticateUser("ghost", "pass"));
    }

    @Test
    public void testPasswordIsHashed() {
        // Raw password must not equal the stored value (it should be hashed)
        User admin = userService.getUserByUsername("admin");
        assertNotEquals("admin123", admin.getPassword());
    }

    // --- Permissions: SYSTEM_ADMIN ---

    @Test
    public void testAdminCanDoEverything() {
        assertTrue(userService.hasPermission(adminSession, "CREATE", "TRANSPORT"));
        assertTrue(userService.hasPermission(adminSession, "DELETE", "DRIVER"));
        assertTrue(userService.hasPermission(adminSession, "VIEW",   "TRUCK"));
        assertTrue(userService.hasPermission(adminSession, "UPDATE", "SITE"));
    }

    // --- Permissions: TRANSPORT_MANAGER ---

    @Test
    public void testManagerCanViewAll() {
        assertTrue(userService.hasPermission(managerSession, "VIEW", "TRANSPORT"));
        assertTrue(userService.hasPermission(managerSession, "VIEW", "DRIVER"));
        assertTrue(userService.hasPermission(managerSession, "VIEW", "TRUCK"));
        assertTrue(userService.hasPermission(managerSession, "VIEW", "SITE"));
    }

    @Test
    public void testManagerCanManageTransportAndIncidents() {
        assertTrue(userService.hasPermission(managerSession, "CREATE", "TRANSPORT"));
        assertTrue(userService.hasPermission(managerSession, "UPDATE", "INCIDENT"));
    }

    // --- Permissions: DRIVER ---

    @Test
    public void testDriverCanOnlyViewTransportAndSchedule() {
        assertTrue(userService.hasPermission(driverSession, "VIEW", "TRANSPORT"));
        assertTrue(userService.hasPermission(driverSession, "VIEW", "SCHEDULE"));
        assertFalse(userService.hasPermission(driverSession, "CREATE", "TRANSPORT"));
        assertFalse(userService.hasPermission(driverSession, "VIEW", "DRIVER"));
    }

    // --- Permissions: VIEWER ---

    @Test
    public void testViewerCanOnlyView() {
        assertTrue(userService.hasPermission(viewerSession, "VIEW", "TRANSPORT"));
        assertFalse(userService.hasPermission(viewerSession, "CREATE", "TRANSPORT"));
        assertFalse(userService.hasPermission(viewerSession, "DELETE", "SITE"));
    }

    // --- Invalid / expired session ---

    @Test
    public void testInvalidSessionHasNoPermissions() {
        assertFalse(userService.hasPermission("FAKE_SESSION", "VIEW", "TRANSPORT"));
    }

    @Test
    public void testLoggedOutSessionHasNoPermissions() {
        userService.logout(viewerSession);
        assertFalse(userService.hasPermission(viewerSession, "VIEW", "TRANSPORT"));
    }

    // --- Deactivated user ---

    @Test
    public void testDeactivatedUserCannotLogin() {
        userService.addUser("X1", "tempuser", "pass123", "Temp", UserRole.VIEWER);
        userService.deactivateUser("X1");
        assertNull(userService.authenticateUser("tempuser", "pass123"));
    }
}
