package PresetationLayer;

import ServiceLayer.ProductService;
import ServiceLayer.ReportService;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class presentation {
    private final Scanner scanner=new Scanner(System.in);
    private final ProductService productService=new ProductService();
    private final ReportService reportService=new ReportService();

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

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
        System.out.println("         for 'precentage, only write the number, np need to write %");
        System.out.println();

        System.out.println("to add a new product to the system:");
        System.out.println("ADD 'productName' 'category' [subCategories] 'manufacturer' 'sellPrice' 'shelfLocation' 'storageLocation'");
        System.out.println();

        System.out.println("to set a discount on a specific product:");
        System.out.println("PDISCOUNT 'productName' 'startDate' 'endDate' 'precentage'");
        System.out.println();

        System.out.println("to set a discount on a category:");
        System.out.println("CDISCOUNT 'categoryName' 'startDate' 'endDate' 'precentage'");
        System.out.println();

        System.out.println("to add a new supply of a product:");
        System.out.println("SUPPLY 'productName' 'shelfQuantity' 'storageQuantity' 'supplyCostPerItem' 'expirationDate'");
        System.out.println();

        System.out.println("to report about sales of a product:");
        System.out.println("SOLD 'productName' 'supplyId' 'newShelfQuantity' 'newStoreCostPerItem'");
        System.out.println();

        System.out.println("to report about bad product found:");
        System.out.println("BROKE 'productName' 'supplyId' 'newShelfQuantity' 'newStoreCostPerItem'");
        System.out.println();

        System.out.println("to get a missing supply report(only use after updating all the products!):");
        System.out.println("SREPORT");
        System.out.println();

        System.out.println("to an inventory report of a single category:");
        System.out.println("IREPORT category");
        System.out.println();

        System.out.println("to an inventory report of some categories:");
        System.out.println("IREPORT [categories]");
        System.out.println();

        System.out.println("to get defect products report:");
        System.out.println("DREPORT");
        System.out.println();

        System.out.println("to get a report about all products that will expire until a provided date:");
        System.out.println("EREPORT 'untilDate'");
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
            else if(args[0].equals("add"))
            {

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

    /**
     * checks if provided str is parsable into Local date
     * @param str the string to check
     * @return whether its the correct format
     */
    private boolean correctDate(String str)
    {
        try {
            LocalDate.parse(str, FORMATTER);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * checks whether or not provided string is an integer
     * @param str string to check
     * @return str=num
     */
    public static boolean isInteger(String str) {
        try {
            Integer.parseInt(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * checks whether or not provided string is a double
     * @param str string to check
     * @return str=double
     */
    public static boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    /**
     * checks whether or not provided string is a number
     * @param str string to check
     * @return str=num
     */
    public static boolean isNumber(String str) {
        return isInteger(str) || isDouble(str);
    }




    public static boolean isListFormat(String str) {
        // Check if it starts with "[" and ends with "]"
        if (!str.startsWith("[") || !str.endsWith("]")) {
            return false;
        }

        // Remove brackets and split by commas
        String content = str.substring(1, str.length() - 1);
        String[] elements = content.split(",");

        // Check if there are elements between the brackets
        return elements.length > 0;
    }
    private List<String> parseList(String str)
    {
        String content = str.substring(1, str.length() - 1);
        return Arrays.asList(content.split(","));
    }

    //parses and add commands
    private void parseAdd(String[] args)
    {

    }

    //parses and productDscount commands
    private void parsePdiscount(String[] args)
    {

    }

    //parses and categoryDiscount commands
    private void parseCdiscount(String[] args)
    {

    }

    //parses and add commands
    private void parseSupply(String[] args)
    {

    }

    //parses and productDscount commands
    private void parseSold(String[] args)
    {

    }

    //parses and categoryDiscount commands
    private void parseBroke(String[] args)
    {

    }

    //parses an Sreport command
    private void parseSreport(String[] args)
    {
        //System.out.println(reportService.);
    }

    //parses an Ireport command
    private void parseIreport(String[] args) throws Exception
    {
        if(isListFormat(args[1]))
            System.out.println(reportService.GenerateInventoryReport(parseList(args[1])));
        else
            System.out.println(reportService.GenerateInventoryReport(args[1]));

    }

    //parses an Dreport command
    private void parseDreport(String[] args)
    {
        System.out.println(reportService.GenerateDamageReport());
    }

    //parses an Ereport command
    private void parseEreport(String[] args) throws Exception
    {
        if(!correctDate(args[1]))
            throw new Exception("cannot parse the expiery date!");
        System.out.println(reportService.GenerateExpiryReport(LocalDate.parse(args[1],FORMATTER)));
    }

}