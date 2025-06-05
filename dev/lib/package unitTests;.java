package unitTests;

import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ExampleTest {

    @Test
    void testMocking() {
        // Create a mock object
        List<String> mockList = mock(List.class);

        // Define behavior
        when(mockList.size()).thenReturn(5);

        // Verify behavior
        assertEquals(5, mockList.size());
        verify(mockList).size();
    }
}