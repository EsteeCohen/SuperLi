package PresetationLayer;

import org.junit.jupiter.api.*;

import java.io.*;
import java.util.Scanner;

class presentationTest {
    private final ByteArrayOutputStream outputBuffer = new ByteArrayOutputStream();
    private PrintStream originalOut;
    private PipedOutputStream pos;
    private PipedInputStream pis;
    private PrintStream pipedWriter;
    private Thread programThread;

    @BeforeEach
    void setUp() throws IOException {
        // Redirect System.out to capture output
        originalOut = System.out;
        System.setOut(new PrintStream(outputBuffer));

        // Create piped stream for feeding input
        pos = new PipedOutputStream();
        pis = new PipedInputStream(pos);
        pipedWriter = new PrintStream(pos);

        // Start the program in a separate thread
        programThread = new Thread(() -> {
            Scanner scanner = new Scanner(pis);
            new PresetationLayer.presentation().run(scanner); // Make sure you have run(Scanner)
        });
        programThread.start();
    }

    @AfterEach
    void tearDown() throws Exception {
        // Send EXIT to stop the program if still running
        pipedWriter.println("EXIT");
        pipedWriter.flush();
        programThread.join();

        // Restore System.out
        System.setOut(originalOut);
    }

    private void sendLine(String line) {
        pipedWriter.println(line);
        pipedWriter.flush();
    }

    private String getOutput() {
        try {
            Thread.sleep(100); // Let the program process
        } catch (InterruptedException ignored) {}
        String output = outputBuffer.toString();
        outputBuffer.reset();
        return output;
    }

    /*
    try bad add command
    good add
    empty sreport
    dreport contains product:0
    bad supply
    good supply 20
    good supply 20
    empty sreport
    dreport contains product:0
    broke 10
    sold 10
    empty sreport
    dreport contains product:10
    sold 10
    sreport containsproduct:16
     */
    @Test
    void testStepByStepInteraction() {
        sendLine("ADD 1 1 [1[ 1 1 1 1");
        Assertions.assertTrue(getOutput().contains("Exception"), "Expected exception for bad command");

        sendLine("ADD 1 1 [1] 1 1 1 1");
        Assertions.assertTrue(getOutput().contains("added"), "Expected confirmation of product addition");

        sendLine("SREPORT");
        String report = getOutput();
        Assertions.assertTrue(report.lines().count()==3, "Expected an empty report");

        sendLine("DREPORT");
        String dreport1 = getOutput();
        Assertions.assertTrue(dreport1.contains("1:0"), "Expected a zeroed report");

        sendLine("SUPPLY 2 10 10 10 10/10/1010");
        Assertions.assertTrue(getOutput().contains("Exception"), "Expected exception for bad command");

        sendLine("SUPPLY 1 10 10 10 11/11/1111");
        Assertions.assertTrue(getOutput().contains("added new supply"), "Expected new supply");

        sendLine("SUPPLY 1 10 10 10 11/11/2011");
        Assertions.assertTrue(getOutput().contains("added new supply"), "Expected new supply");

        sendLine("SREPORT");
        Assertions.assertTrue(getOutput().lines().count()==3, "Expected an empty report");

        sendLine("DREPORT");
        Assertions.assertTrue(getOutput().contains("1:0"), "Expected a zeroed report");

        sendLine("BROKE 1 0 0 10");
        Assertions.assertTrue(getOutput().contains("updated"), "Expected update");

        sendLine("SOLD 1 1 10 0 ");
        Assertions.assertTrue(getOutput().contains("update"), "Expected update");

        sendLine("SREPORT");
        Assertions.assertTrue(getOutput().lines().count()==3, "Expected an empty report");

        sendLine("DREPORT");
        Assertions.assertTrue(getOutput().contains("1:10"), "Expected 10 broken items");

        sendLine("BROKE 1 0 0 0");
        Assertions.assertTrue(getOutput().contains("updated"), "Expected update");

        sendLine("SOLD 1 1 0 0");
        Assertions.assertTrue(getOutput().contains("update"), "Expected update");

        sendLine("SREPORT");
        Assertions.assertTrue(getOutput().contains("1:16"), "Expected an empty report");

        sendLine("DREPORT");
        Assertions.assertTrue(getOutput().contains("1:10"), "Expected 10 broken items");

        sendLine("EXIT");
    }
}
