package DomainLayer;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

//not as critical of the tests, just to ensure my math is correct
class SaleTest {
    Sale s1;
    Sale sZero;
    Sale sFull;

    @BeforeEach
    void setup()
    {
        s1=new Sale(10,100,10);
        sZero=new Sale(10,100,0);
        sFull=new Sale(10,100,100);
    }

    @Test
    void getTotalPrice() {
        assertEquals(90.0,s1.getTotalPrice());
        assertEquals(100.0,sZero.getTotalPrice());
        assertEquals(0.0,sFull.getTotalPrice());
    }

    @Test
    void getTotalRevenue() {
        assertEquals(900.0,s1.getTotalRevenue());
        assertEquals(1000.0,sZero.getTotalRevenue());
        assertEquals(0.0,sFull.getTotalRevenue());
    }
}