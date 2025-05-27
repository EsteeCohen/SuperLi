package DomainLayer.Inventory;

import DataAccessLayer.DatabaseConnection;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductFacadeTest {
    ProductFacade facade;
    private static Connection connection;


    @BeforeEach
    void setUp() {
        try {
            connection = DatabaseConnection.getConnection();
        }
        catch (Exception e){}
        facade=ProductFacade.getInstance();
        // Clear database tables first

            // Disable foreign key checks if needed (depends on your DB)
            try (Statement stmt = connection.createStatement()) {
                // Delete from SUPPLIES first (child table)
                stmt.executeUpdate("DELETE FROM SUPPLIES");
                // Then delete from INVENTORYPRODUCTS (parent table)
                stmt.executeUpdate("DELETE FROM INVENTORYPRODUCTS");
            }
            catch (SQLException e) {
            fail("Failed to clean database: " + e.getMessage());
        }

        try {
            facade.AddProduct("11", "1", new ArrayList<>(), "", 1,"","");
            facade.AddProduct("12", "1", new ArrayList<>(), "", 1,"","");
            facade.AddProduct("21", "2", new ArrayList<>(), "", 1,"","");
            facade.AddProduct("31", "3", new ArrayList<>(), "", 1,"","");
        }
        catch(Exception e){e.printStackTrace();};
    }

    @AfterEach
    void tearDown() {
        ProductFacade.flush();
    }

    @Test
    void getInstance() {
        assertNotNull(facade);
    }


    @Test
    void addProduct() {
        //try to add a product with the same name, should throw exception
        assertThrows(Exception.class,()->{facade.AddProduct("11", "1", new ArrayList<>(), "", 1,"","");});

        //try to add a new product to existing category, should work
        assertDoesNotThrow(()->{facade.AddProduct("13", "1", new ArrayList<>(), "", 1,"","");});
        assertNotNull(facade.getProduct("13"));
        assertNotNull(facade.getProductFromCategory("13","1"));

        //try to add a new product to a new category, should work
        assertDoesNotThrow(()->{facade.AddProduct("41", "4", new ArrayList<>(), "", 1,"","");});
        assertNotNull(facade.getProduct("41"));
        assertNotNull(facade.getProductFromCategory("41","4"));
    }
    @Test
    void updateDataBaseWithNewProduct()
    {
        // Test successful supply addition and verify database
        try {
            //try to add a new product to existing category, should work
            assertDoesNotThrow(()->{facade.AddProduct("13", "1", new ArrayList<>(), "", 1,"","");});

            // Verify database state
            try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM inventoryProducts WHERE product_name = ?")) {

                stmt.setString(1, "13");
                ResultSet rs = stmt.executeQuery();

                assertTrue(rs.next(), "Product should exist in database");
                assertEquals("1", rs.getString("category"));
            }
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void setDiscountForCategory() {
        //try to discount a non existing category, should result in an error
        assertThrows(Exception.class,()->{facade.SetDiscountForCategory("0", LocalDate.now(),LocalDate.now(),10);});

        //try to discount an existing category
        assertNull(facade.getProduct("11").getDiscount());
        assertNull(facade.getProduct("12").getDiscount());
        assertDoesNotThrow(()->{facade.SetDiscount("11", LocalDate.now(),LocalDate.now(),10);});
        assertNotNull(facade.getProduct("11"));
        assertNotNull(facade.getProduct("11").getDiscount());
    }

    @Test
    void setDiscount() {
        //try to discount a non existing product
        assertThrows(Exception.class,()->{facade.SetDiscount("51", LocalDate.now(),LocalDate.now(),10);});

        //try to discount an existing product
        assertNull(facade.getProduct("11").getDiscount());
        assertNull(facade.getProduct("12").getDiscount());
        assertDoesNotThrow(()->{facade.SetDiscountForCategory("1", LocalDate.now(),LocalDate.now(),10);});
        assertNotNull(facade.getProduct("11").getDiscount());
        assertNotNull(facade.getProduct("12").getDiscount());
    }


    @Test
    void addSupply() {
        //try adding a supply to a non existing product, should throw exception
        assertThrows(Exception.class,()->{ facade.addSupply("wow",1,LocalDate.now(),1,1);});
        assertThrows(Exception.class,()->{ facade.addSupply("wawa",1,LocalDate.now(),1,1);});

        //try to add a supply with negative quantity/cost, should throw exception
        assertThrows(Exception.class,()->{ facade.addSupply("11",-1,LocalDate.now(),1,1);});
        assertThrows(Exception.class,()->{ facade.addSupply("11",1,LocalDate.now(),-1,1);});
        assertThrows(Exception.class,()->{ facade.addSupply("11",1,LocalDate.now(),1,-1);});

        //try to add a supply with quantity 0, should throw exception
        assertThrows(Exception.class,()->{ facade.addSupply("11",1,LocalDate.now(),0,0);});

        //try to add a new supply with positive quantity, non negative cost, and to an existing product
        int oldShelfQuantity=facade.getProduct("11").GetShelfQuantity();
        int oldStorageQuantity=facade.getProduct("11").GetStorageQuantity();
        assertDoesNotThrow(()->{facade.addSupply("11",1,LocalDate.now(),1,1);});
        assertEquals(oldShelfQuantity+1,facade.getProduct("11").GetShelfQuantity());
        assertEquals(oldStorageQuantity+1,facade.getProduct("11").GetStorageQuantity());

    }
    @Test
    void updateDataBaseWithNewSupply()
    {
        // Test successful supply addition and verify database
        try {
            // Add supply
            int supplyId = facade.addSupply("11", 10, LocalDate.now(), 20, 15);
            // Verify database state
            try (
                 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM supplies WHERE supply_id = ?")) {

                stmt.setInt(1, supplyId);
                ResultSet rs = stmt.executeQuery();

                assertTrue(rs.next(), "Supply should exist in database");
                assertEquals(10, rs.getInt("cost"));
                assertEquals(20, rs.getInt("shelf_quantity"));
                assertEquals(15, rs.getInt("storage_quantity"));
                assertEquals("11", rs.getString("product_name"));
            }
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void updateSoldQuantity() {
        int supId1=0,supId2=0;
        try {
            supId1=facade.addSupply("11",1,LocalDate.now(),10,10);
            supId2=facade.addSupply("11",1,LocalDate.now(),10,10);
        }
        catch(Exception ignored){}
        int fsupId1=supId1,fsupId2=supId2;
        //try to update a non existing product, should throw exception
        assertThrows(Exception.class,()->{facade.updateSoldQuantity("50",fsupId1,1,1);});

        //try to update a non existing supply, should throw exception
        assertThrows(Exception.class,()->{facade.updateSoldQuantity("11",-1,1,1);});

        //try to update a supply with such that the new quantity>old quantity, Exception
        assertThrows(Exception.class,()->{facade.updateSoldQuantity("11",fsupId1,11,11);});

        //try to update a supply with all proper things
        int oldShelf=facade.getProduct("11").GetShelfQuantity();
        int oldStor=facade.getProduct("11").GetStorageQuantity();

        assertDoesNotThrow(()->{facade.updateSoldQuantity("11",fsupId1,1,1);});
        assertEquals(oldShelf-9,facade.getProduct("11").GetShelfQuantity());
        assertEquals(oldStor-9,facade.getProduct("11").GetStorageQuantity());
    }


    @Test
    void updateDataBaseAfterSoldQuantity()
    {
        // Test successful supply addition and verify database
        try {
            int supId1=0;
            try {
                supId1=facade.addSupply("11",1,LocalDate.now(),10,10);
            }
            catch(Exception ignored){}
            // Add supply
            facade.updateSoldQuantity("11",supId1,5,5);

            // Verify database state
            try (

                 PreparedStatement stmt = DatabaseConnection.getConnection().prepareStatement("SELECT * FROM supplies WHERE supply_id = ?")) {

                stmt.setInt(1, supId1);
                ResultSet rs = stmt.executeQuery();

                assertTrue(rs.next(), "Supply should exist in database");
                assertEquals(5, rs.getInt("shelf_quantity"));
                assertEquals(5, rs.getInt("storage_quantity"));
            }
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }

    @Test
    void updateBrokenQuantity() {
        int supId1=0,supId2=0;
        try {
            supId1=facade.addSupply("11",1,LocalDate.now(),10,10);
            supId2=facade.addSupply("11",1,LocalDate.now(),10,10);
        }
        catch(Exception ignored){}
        int fsupId1=supId1,fsupId2=supId2;
        //try to update a non existing product, should throw exception
        assertThrows(Exception.class,()->{facade.updateBrokenQuantity("50",fsupId1,1,1);});

        //try to update a non existing supply, should throw exception
        assertThrows(Exception.class,()->{facade.updateBrokenQuantity("11",-1,1,1);});

        //try to update a supply with such that the new quantity>old quantity, Exception
        assertThrows(Exception.class,()->{facade.updateBrokenQuantity("11",fsupId1,11,11);});

        //try to update a supply with all proper things
        int oldShelf=facade.getProduct("11").GetShelfQuantity();
        int oldStor=facade.getProduct("11").GetStorageQuantity();

        assertDoesNotThrow(()->{facade.updateBrokenQuantity("11",fsupId1,1,1);});
        assertEquals(oldShelf-9,facade.getProduct("11").GetShelfQuantity());
        assertEquals(oldStor-9,facade.getProduct("11").GetStorageQuantity());
    }
    @Test
    void updateDataBaseAfterBrokenQuantity()
    {
        // Test successful supply addition and verify database
        try {
            int supId1=0;
            try {
                supId1=facade.addSupply("11",1,LocalDate.now(),10,10);
            }
            catch(Exception ignored){}
            // Add supply
            facade.updateBrokenQuantity("11",supId1,5,5);

            // Verify database state
            try (
                 PreparedStatement stmt = connection.prepareStatement("SELECT * FROM supplies WHERE supply_id = ?")) {

                stmt.setInt(1, supId1);
                ResultSet rs = stmt.executeQuery();

                assertTrue(rs.next(), "Supply should exist in database");
                assertEquals(5, rs.getInt("shelf_quantity"));
                assertEquals(5, rs.getInt("storage_quantity"));
            }
        } catch (Exception e) {
            fail("Should not throw exception: " + e.getMessage());
        }
    }
}