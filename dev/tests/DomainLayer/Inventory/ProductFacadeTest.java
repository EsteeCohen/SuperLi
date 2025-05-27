package DomainLayer.Inventory;
import DomainLayer.Inventory.ProductFacade;
import org.junit.BeforeClass;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductFacadeTest {
    private ProductFacade productFacade;

    @BeforeAll
    static void firstCleanUp()
    {

    }

    @BeforeEach
    void setUp()
    {
        productFacade=ProductFacade.getInstance();
    }

    @AfterEach
    void cleanUp()
    {
        ProductFacade.flush();
    }

    @Test
    void AddProduct()
    {
        assertNotNull(productFacade);
    }
}
