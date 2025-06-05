package employeeDev.src.unitTests;

import employeeDev.src.domainLayer.*;
import employeeDev.src.dataAcssesLayer.RoleDAO;
import employeeDev.src.dtos.RoleDTO;
import employeeDev.src.mappers.RoleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RoleFacadeTest {

    private RoleFacade roleFacade;

    @BeforeEach
    void setUp() {
        roleFacade = new RoleFacade();
    }

    @Test
    void testAddRoleSuccess() {
        assertTrue(roleFacade.add("Manager"));
        assertNotNull(roleFacade.getRoleByName("Manager"));
    }

    @Test
    void testAddRoleDuplicate() {
        assertTrue(roleFacade.add("Cashier"));
        assertFalse(roleFacade.add("Cashier"));
    }

    @Test
    void testGetRoleByName() {
        roleFacade.add("Worker");
        assertNotNull(roleFacade.getRoleByName("Worker"));
        assertNull(roleFacade.getRoleByName("NotExist"));
    }

    @Test
    void testDeleteRole() {
        roleFacade.add("Cleaner");
        assertNotNull(roleFacade.getRoleByName("Cleaner"));

        // Mock RoleDAO for delete
        try (MockedStatic<RoleDAO> mockedDAO = mockStatic(RoleDAO.class)) {
            roleFacade.delete("Cleaner");
            assertNull(roleFacade.getRoleByName("Cleaner"));
        }
    }

    @Test
    void testGetAllRoles() {
        roleFacade.add("A");
        roleFacade.add("B");
        List<RoleDL> roles = roleFacade.getAllRoles();
        assertEquals(2, roles.size());
    }

    @Test
    void testLoadRolesFromDB() {
        // Mock RoleDAO and RoleMapper
        RoleDTO dto1 = mock(RoleDTO.class);
        RoleDTO dto2 = mock(RoleDTO.class);
        List<RoleDTO> dtos = Arrays.asList(dto1, dto2);

        RoleDAO mockDao = mock(RoleDAO.class);
        when(mockDao.getAllRoles()).thenReturn(dtos);

        RoleDL role1 = mock(RoleDL.class);
        RoleDL role2 = mock(RoleDL.class);
        when(role1.getName()).thenReturn("Role1");
        when(role2.getName()).thenReturn("Role2");

        try (MockedStatic<RoleDAO> mockedDAO = mockStatic(RoleDAO.class);
             MockedStatic<RoleMapper> mockedMapper = mockStatic(RoleMapper.class)) {
            mockedDAO.when(RoleDAO::new).thenReturn(mockDao);
            mockedMapper.when(() -> RoleMapper.fromDTO(dto1)).thenReturn(role1);
            mockedMapper.when(() -> RoleMapper.fromDTO(dto2)).thenReturn(role2);

            roleFacade.loadRolesFromDB();

            assertNotNull(roleFacade.getRoleByName("Role1"));
            assertNotNull(roleFacade.getRoleByName("Role2"));
        }
    }
}