package PresetationLayer;

import ServiceLayer.ProductService;
import ServiceLayer.ReportService;

import javax.rmi.ssl.SslRMIClientSocketFactory;
import java.util.Scanner;

public class presentation {
    private final Scanner scanner=new Scanner(System.in);
    private final ProductService productService=new ProductService();
    private final ReportService reportService=new ReportService();

    public static void main(String[] args) {
        presentation p=new presentation();
        p.run();
    }
    presentation()
    {
    }

    /**
     * the main program, reads an input, translates it and sends to service layer
     */
    private void run()
    {
        System.out.println("Welcome to the storage/inventory managment system!");
        System.out.println("pls enter a command, for all commands enter help:");
        String command =scanner.nextLine().toLowerCase();
        while(!command.equals("exit"))
        {
            parse(command);
            System.out.println("pls enter a command, for all commands enter help:");
            command =scanner.nextLine().toLowerCase();
        }
        System.out.println("Good bye!");
    }

    /**
     * prints all available commands and how to use them
     */
    private void printHelp()
    {   System.out.println("symbols: 'abs' means a string,");
        System.out.println("         [abc] means a list of strings encased in [], for example [a,b,c]");
        System.out.println("         dates should be of format dd/mm/yyyy, for example 31/11/2025");
        System.out.println();

        System.out.println("to add a new product to the system:");
        System.out.println("ADD 'productName' 'category' [subCategories] 'manufacturer' 'sellPrice'");
        System.out.println();

        System.out.println("to set a discount on a specific product:");
        System.out.println("PDISCOUNT 'productName' 'startDate' 'endDate' 'precentage'");
        System.out.println();

        System.out.println("to set a discount on a category:");
        System.out.println("CDISCOUNT 'categoryName' 'startDate' 'endDate' 'precentage'");
        System.out.println();
    }

    /**
     * parses a given command and sends a request to the service layer
     * @param command the command to parse
     */
    private void parse(String command)
    {
        String[] args=command.split(" ");
        try {
            //base cases, no command, and help
            if (args.length < 1)
                return;
            if (args[0].equals("help"))
                printHelp();

            else if (args[0].isEmpty())
            {//just pressed enter
                return;
            }

            else
            {
                System.out.println("Unrecognized command!");
            }
        }
        catch (Exception e)
        {
            System.out.println("Exception occured!");
            System.out.println(e.getMessage());
        }
    }
}