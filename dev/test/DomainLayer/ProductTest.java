package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    Product milk;
    int milkId1,milkId2,iceId1,iceId2,waterId1,waterId2;
    Product ice;
    Product water;
    @BeforeEach
    void setUp() {
        milk=new Product("milk",new ArrayList<>(),"",5.0);
        ice=new Product("ice",new ArrayList<>(),"",3.0);
        water=new Product("water",new ArrayList<>(),"",1.0);

        try {
            milkId1=milk.addSupply(1, LocalDate.now(), 10, 10, "", "");
            milkId2=milk.addSupply(2, LocalDate.now(), 10, 10, "", "");

            iceId1=ice.addSupply(1, LocalDate.now(), 0, 10, "", "");
            iceId2=ice.addSupply(2, LocalDate.now(), 10, 0, "", "");

            waterId1=water.addSupply(1, LocalDate.now(), 5, 5, "", "");
            waterId2=water.addSupply(2, LocalDate.now(), 5, 5, "", "");

        }
        catch (Exception ignored)
        {}
    }


    @Test
    /*
     * ensure that updating the items as sold, it actually saves the sold data
     */
    void updateSoldQuantity() {
        //test for correct exceptions, should happen if updated supply>old supply
        assertThrows(Exception.class,()->{milk.updateSoldQuantity(milkId1,10,11);});
        assertThrows(Exception.class,()->{milk.updateSoldQuantity(milkId2,21,0);});

        //test for correct sales data and placements after selling nothing
        int oldMilkShelf=milk.GetShelfQuantity();
        int oldMilkStorage=milk.GetStorageQuantity();
        int oldIceShelf=ice.GetShelfQuantity();
        int oldIceStorage=ice.GetStorageQuantity();
        int oldWaterShelf=water.GetShelfQuantity();
        int oldWaterStorage=water.GetStorageQuantity();
        try {
            milk.updateSoldQuantity(milkId1, 10, 10);
            ice.updateSoldQuantity(iceId1,10,0);
            water.updateSoldQuantity(waterId1,0,10);
        }
        catch (Exception ignored)
        {

        }
        assertEquals(oldMilkShelf,milk.GetShelfQuantity());
        assertEquals(oldMilkStorage,milk.GetStorageQuantity());
        assertNotEquals(oldIceShelf,ice.GetShelfQuantity());
        assertNotEquals(oldIceStorage,ice.GetStorageQuantity());
        assertNotEquals(oldWaterShelf,water.GetShelfQuantity());
        assertNotEquals(oldWaterStorage,water.GetStorageQuantity());

        assertTrue(milk.getLatestSales().isEmpty());
        assertTrue(ice.getLatestSales().isEmpty());
        assertTrue(water.getLatestSales().isEmpty());

        //------------------------------------------
        //test for actually selling stuff
        oldMilkShelf=milk.GetShelfQuantity();
        oldMilkStorage=milk.GetStorageQuantity();
        oldIceShelf=ice.GetShelfQuantity();
        oldIceStorage=ice.GetStorageQuantity();
        oldWaterShelf=water.GetShelfQuantity();
        oldWaterStorage=water.GetStorageQuantity();
        try
        {
            milk.updateSoldQuantity(milkId1, 19, 0);
            ice.updateSoldQuantity(iceId1,1,0);
            water.updateSoldQuantity(waterId1,4,4);
        }
        catch (Exception ignored){}
        assertEquals(oldMilkShelf+9,milk.GetShelfQuantity());
        assertEquals(oldMilkStorage-10,milk.GetStorageQuantity());
        assertEquals(oldIceShelf-9,ice.GetShelfQuantity());
        assertEquals(oldIceStorage,ice.GetStorageQuantity());
        assertEquals(oldWaterShelf+4,water.GetShelfQuantity());
        assertEquals(oldWaterStorage-6,water.GetStorageQuantity());

        assertFalse(milk.getLatestSales().isEmpty());
        assertFalse(ice.getLatestSales().isEmpty());
        assertFalse(water.getLatestSales().isEmpty());

        assertEquals(1,milk.getLatestSales().get(0).getQuantity());
        assertEquals(9,ice.getLatestSales().get(0).getQuantity());
        assertEquals(2,water.getLatestSales().get(0).getQuantity());

    }


    @Test
    void updateFoundBrokenItems() {
        //test for correct exceptions, should happen if updated supply>old supply
        assertThrows(Exception.class,()->{milk.updateFoundBrokenItems(milkId1,10,11);});
        assertThrows(Exception.class,()->{milk.updateFoundBrokenItems(milkId2,21,0);});

        //test for correct sales data and placements after selling nothing
        int oldMilkShelf=milk.GetShelfQuantity();
        int oldMilkStorage=milk.GetStorageQuantity();
        int oldIceShelf=ice.GetShelfQuantity();
        int oldIceStorage=ice.GetStorageQuantity();
        int oldWaterShelf=water.GetShelfQuantity();
        int oldWaterStorage=water.GetStorageQuantity();
        try {
            milk.updateFoundBrokenItems(milkId1, 10, 10);
            ice.updateFoundBrokenItems(iceId1,10,0);
            water.updateFoundBrokenItems(waterId1,0,10);
        }
        catch (Exception ignored) {}
        assertEquals(oldMilkShelf,milk.GetShelfQuantity());
        assertEquals(oldMilkStorage,milk.GetStorageQuantity());
        assertNotEquals(oldIceShelf,ice.GetShelfQuantity());
        assertNotEquals(oldIceStorage,ice.GetStorageQuantity());
        assertNotEquals(oldWaterShelf,water.GetShelfQuantity());
        assertNotEquals(oldWaterStorage,water.GetStorageQuantity());
        try
        {
            milk.updateFoundBrokenItems(milkId1, 9, 0);
            ice.updateFoundBrokenItems(iceId1,1,0);
            water.updateFoundBrokenItems(waterId1,8,0);
        }
        catch (Exception ignored){}
        assertEquals(11,milk.getBrokenQuantity());
        assertEquals(9,ice.getBrokenQuantity());
        assertEquals(2,water.getBrokenQuantity());

    }

    @Test
    void calcMinQuantity() {
        try
        {
            milk.updateSoldQuantity(milkId1, 9, 0);
            ice.updateSoldQuantity(iceId1,1,0);
            water.updateSoldQuantity(waterId1,8,0);
        }
        catch (Exception ignored){}
        milk.calcMinQuantity();
        ice.calcMinQuantity();
        water.calcMinQuantity();
        assertEquals(16,milk.getMinQuantity());
        assertEquals(12,ice.getMinQuantity());
        assertEquals(4,water.getMinQuantity());

    }

    @Test
    void addSupply() {
    }
}