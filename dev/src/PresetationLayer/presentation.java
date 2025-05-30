package PresetationLayer;

import DomainLayer.TimeController;
import ServiceLayer.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class presentation {
    private Scanner scanner=new Scanner(System.in);
    private ServiceFactory serviceFactory=new ServiceFactory();
    private static presentation instance;

    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    public static presentation getInstance()
    {
        if(instance==null) {
                instance = new presentation();
        }
        return instance;
    }
    private presentation()
    {
    }

    /**
     * the main program, reads an input, translates it and sends to service layer
     */
    public void run()
    {
        System.out.println("Welcome to the storage/inventory management system!");

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

    //like normal run, but with a custom scanner
    //used for tests
    void run(Scanner scanner)
    {
        this.scanner=scanner;
        System.out.println("Welcome to the storage/inventory managment system!");

        System.out.println("pls enter a command, for all commands enter help:");
        String command =scanner.nextLine().toLowerCase();
        while(!command.equals("exit"))
        {
            parse(command);
            System.out.println("pls enter a command, for all commands enter HELP:");
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
        System.out.println("         for 'percentage, only write the number, no need to write %");
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

        System.out.println("to report about sales of a product:");
        System.out.println("SOLD 'productName' 'supplyId' 'newShelfQuantity' 'newStorageQuantity'");
        System.out.println();

        System.out.println("to report about bad product found:");
        System.out.println("BROKE 'productName' 'supplyId' 'newShelfQuantity' 'newStorageQuantity'");
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


        System.out.println("to advance one day:");
        System.out.println("ND");
        System.out.println();

        System.out.println("to exit the program:");
        System.out.println("EXIT");
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
            else if(args[0].equals("supply"))
            {
                parseSupply(args);
            }
            else if (args[0].isEmpty())
            {//just pressed enter
                return;
            }
            else if(args[0].equals("add"))
            {
                parseAdd(args);
            }
            else if(args[0].equals("pdiscount"))
            {
                parsePdiscount(args);
            }
            else if(args[0].equals("cdiscount"))
            {
                parseCdiscount(args);
            }
            else if(args[0].equals("sold"))
            {
                parseSold(args);
            }
            else if(args[0].equals("broke"))
            {
                parseBroke(args);
            }
            else if(args[0].equals("sreport"))
            {
                parseSreport(args);
            }
            else if(args[0].equals("ireport"))
            {
                parseIreport(args);
            }
            else if(args[0].equals("ereport"))
            {
                parseEreport(args);
            }
            else if(args[0].equals("dreport"))
            {
                parseDreport(args);
            }
            else if(args[0].equals("startup"))
            {
                startUp();
            }
            else if(args[0].equals("nd"))
            {
                TimeController.NextDay();
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

    //parses a SUPPLY command
    private void parseSupply(String[] args) throws Exception
    {
        if(args.length!=6)
            throw new Exception("SUPPLY command gets exactly 5 arguments!");
        if(!isInteger(args[2])||!isInteger(args[3]))
            throw new Exception("quantities must be integers!");
        if(!isNumber(args[4]))
            throw new Exception("cost price must be a number!");
        if(!correctDate(args[5]))
            throw new Exception("ExpirationDate must be of format: dd/MM/yyyy!");

        System.out.println(serviceFactory.addSupply(args[1],Double.parseDouble(args[4]),LocalDate.parse(args[5],FORMATTER),Integer.parseInt(args[2]),Integer.parseInt(args[3])));
    }

    //parses and add commands
    //ADD 'productName' 'category' [subCategories] 'manufacturer' 'sellPrice' 'shelfLocation' 'storageLocation'
    private void parseAdd(String[] args) throws Exception
    {
        if(args.length!=8)
            throw new Exception("ADD gets exactly 7 arguments!");
        if(!isListFormat(args[3]))
            throw new Exception("[subcategories] is a list of subcategories! list should be [a,b,c...,z] with no spaces between!");
        if(!isNumber(args[5]))
            throw new Exception("sellPrice must be a number!");
        System.out.println(serviceFactory.AddProduct(args[1],args[2],parseList(args[3]),args[4],Double.parseDouble(args[5]),args[6],args[7]));
    }

    //parses and productDscount commands
    private void parsePdiscount(String[] args) throws Exception
    {
        if(args.length!=5)
            throw new Exception("CDICOUNT command gets exactly 4 argument!");
        if(!correctDate(args[2])||!correctDate(args[3]))
            throw new Exception("StartDate and EndDate must be of format dd/MM/yyyy!");
        if(!isNumber(args[4]))
            throw new Exception("Discount precentage must be a number!");
        System.out.println(serviceFactory.SetDiscount(args[1],LocalDate.parse(args[2],FORMATTER),LocalDate.parse(args[3],FORMATTER),Double.parseDouble(args[4])));
    }

    //parses a categoryDiscount command
    private void parseCdiscount(String[] args) throws Exception
    {
        if(args.length!=5)
            throw new Exception("CDICOUNT command gets exactly 4 argument!");
        if(!correctDate(args[2])||!correctDate(args[3]))
            throw new Exception("StartDate and EndDate must be of format dd/MM/yyyy!");
        if(!isNumber(args[4]))
            throw new Exception("Discount precentage must be a number!");
        System.out.println(serviceFactory.SetDiscountForCategory(args[1],LocalDate.parse(args[2],FORMATTER),LocalDate.parse(args[3],FORMATTER),Double.parseDouble(args[4])));
    }


    //parses a SOLD command
    private void parseSold(String[] args) throws Exception
    {
        if(args.length!=5)
            throw new Exception("SOLD command gets exactly 4 arguments!");

        if(!isInteger(args[2])||!isInteger(args[3])||!isInteger(args[4]))
            throw new Exception("supplyId and quantities must be integers!");

        System.out.println(serviceFactory.reportSales(args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4])));
    }

    //parses a BROKE command
    private void parseBroke(String[] args) throws Exception
    {
        if(args.length!=5)
            throw new Exception("BROKE command gets exactly 4 arguments!");

        if(!isInteger(args[2])||!isInteger(args[3])||!isInteger(args[4]))
            throw new Exception("supplyId and quantities must be integers!");

        System.out.println(serviceFactory.reportBroke(args[1],Integer.parseInt(args[2]),Integer.parseInt(args[3]),Integer.parseInt(args[4])));
    }

    //parses an Sreport command
    private void parseSreport(String[] args)
    {
        System.out.println(serviceFactory.GenerateAbscenceReport());
    }

    //parses an Ireport command
    private void parseIreport(String[] args) throws Exception
    {
        if(args.length!=2)
            throw new Exception("IREPORT takes exactly 1 argument!");
        if(isListFormat(args[1]))
            System.out.println(serviceFactory.GenerateInventoryReport(parseList(args[1])));
        else
            System.out.println(serviceFactory.GenerateInventoryReport(args[1]));

    }

    //parses an Dreport command
    private void parseDreport(String[] args)
    {
        System.out.println(serviceFactory.GenerateDamageReport());
    }

    //parses an Ereport command
    private void parseEreport(String[] args) throws Exception
    {
        if(args.length!=2)
            throw new Exception("EREPORT takes exactly 1 argumet!");
        if(!correctDate(args[1]))
            throw new Exception("cannot parse the expiery date!");
        System.out.println(serviceFactory.GenerateExpiryReport(LocalDate.parse(args[1],FORMATTER)));
    }

    /**
     * used to initate some data if needed
     * @throws Exception if anything goes wrong
     */
    public void startUp()
    {
        //initiate a new service factory because you cant have the same product twice
        serviceFactory = new ServiceFactory();
        try{
            serviceFactory.AddProduct("milk","diary",Arrays.asList("milk","3%"),"tara",10,"a1","a1");
            serviceFactory.AddProduct("yogurt","diary",Arrays.asList("yogurt","8%"),"tnuva",15,"a2","a2");
            serviceFactory.AddProduct("water","drinks",Arrays.asList("water","mineral"),"ein-gedi",7.5,"z5","a3");

            serviceFactory.addSupply("milk",5,LocalDate.parse("05/05/2025",FORMATTER),10,20);
            serviceFactory.addSupply("milk",5,LocalDate.parse("10/05/2025",FORMATTER),5,15);
            serviceFactory.addSupply("yogurt",4,LocalDate.parse("03/05/2025",FORMATTER),20,20);
            serviceFactory.addSupply("yogurt",4,LocalDate.parse("12/05/2025",FORMATTER),10,15);
            serviceFactory.addSupply("water",2,LocalDate.parse("03/05/2026",FORMATTER),30,20);
            serviceFactory.addSupply("water",1,LocalDate.parse("23/08/2026",FORMATTER),0,100);
        }catch(Exception e) {
            System.out.println("an Error occured during initialization, pls try again later...");
            e.printStackTrace();
            System.exit(-1);
        }
        System.out.println("initiated");

    }
}