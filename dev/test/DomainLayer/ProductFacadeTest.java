package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ProductFacadeTest {
    ProductFacade facade;
    @BeforeEach
    void setUp() {
        facade = new ProductFacade();
        try {
            facade.AddProduct("11", "1", new ArrayList<>(), "", 1);
            facade.AddProduct("12", "1", new ArrayList<>(), "", 1);
            facade.AddProduct("21", "2", new ArrayList<>(), "", 1);
            facade.AddProduct("31", "3", new ArrayList<>(), "", 1);
        }
        catch(Exception ignored){};
    }
    @Test
    //check that adding is correct, you can add new product, no duplicates
    void addProduct() {
        //try to add a product with the same name, should throw exception
        assertThrows(Exception.class,()->{facade.AddProduct("11", "1", new ArrayList<>(), "", 1);});

        //try to add a new product to existing category, should work
        assertDoesNotThrow(()->{facade.AddProduct("13", "1", new ArrayList<>(), "", 1);});
        assertNotNull(facade.getProduct("13"));
        assertNotNull(facade.getProductFromCategory("13","1"));

        //try to add a new product to a new category, should work
        assertDoesNotThrow(()->{facade.AddProduct("41", "4", new ArrayList<>(), "", 1);});
        assertNotNull(facade.getProduct("41"));
        assertNotNull(facade.getProductFromCategory("41","4"));
    }

    @Test
    void setDiscountForProduct() {
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
    }

    @Test
    void getShelfQuantity() {
    }

    @Test
    void getStorageQuantity() {
    }
}