package DomainLayer;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.*;

class Product {
    private final String productName;
    private final List<String> subCategories;
    private final String manufacturer;
    private int latestSupplyId=0;
    private int shelfQuantity;
    private int storageQuantity;

    private int minQuantity;

    private double sellPrice;
    private Discount discount;
    private final TreeMap<LocalDate,Sale> salesReports=new TreeMap<>();
    int brokenQuantity;

    private List<Sale> latestSales;
    private int latestSalesCount;

    private int supplyRate=7;//the days it takes for a new supply to come, assume for now a week
    private int lastMondaySales;
    private int lastThursdaySales;
    private boolean monday;

    private final Map<Integer,Supply> supplies=new HashMap<>();//a map of all the supplies

    Product(String productName, List<String> subCategories, String manufacturer, int sellPrice)
    {
        this.productName = productName;
        this.subCategories = subCategories;
        this.manufacturer = manufacturer;

        //quantities=0 by default, you add to it by adding batches
        this.shelfQuantity = 0;
        this.storageQuantity = 0;
        this.minQuantity = 0;

        this.sellPrice = sellPrice;
        this.discount = null;
        latestSales=new ArrayList<>();
        latestSalesCount=0;
        lastMondaySales=0;
        lastThursdaySales=0;
        monday=LocalDate.now().getDayOfWeek()== DayOfWeek.MONDAY;
    }
    public void SetDiscount(Discount discount)
    {
        this.discount = discount;
    }

    public int GetShelfQuantity()
    {
        return this.shelfQuantity;
    }

    public int GetStorageQuantity()
    {
        return this.storageQuantity;
    }




    void updateSoldQuantity(int supplyID, int storeQuantity, int storageQuantity) throws Exception
    {
        int totalSales=updateQuantities(supplyID,storeQuantity,storageQuantity);

        //document sells?
        if(discount.getEndDate().isBefore(LocalDate.now()))//discount ended
            discount=null;

        Sale sale=new Sale(totalSales,sellPrice, discount==null? 0: discount.getPrecentage());
        latestSales.add(sale);
        latestSalesCount+=totalSales;
        if(monday)
            lastMondaySales+=totalSales;
        else
            lastThursdaySales+=totalSales;
    }


    void updateFoundBrokenItems(int supplyId, int storeQuantity, int storageQuantity) throws  Exception
    {
        int totalLost=updateQuantities(supplyId,storeQuantity,storageQuantity);
    }

    /**
     * calcs the new min quantity
     * new min= daily sales average*8
     */
    void calcMinQuantity()
    {
        int dailyAvg=lastMondaySales+lastThursdaySales;
        dailyAvg/=7;
        minQuantity=dailyAvg*8;
    }

    /**
     * updates the quantities per batch of a product
     * @param supplyID the id of the supply to be updated
     * @param newStoreQuantity the quantity left in the store
     * @param newStorageQuantity the quantity left in the storage
     * @return the total difference between the old quantity and the new quantity
     * @throws Exception if supplyID not found, if given negative number
     */
    private int updateQuantities(int supplyID,int newStoreQuantity, int newStorageQuantity) throws Exception
    {
        if(newStoreQuantity<0 || newStorageQuantity<0)//cant have negative quantities
            throw new IllegalArgumentException("Cannot save negative quantities");

        Supply supply=supplies.get(supplyID);

        if(supply==null)//supply id not found
            throw new NoSuchElementException("Supply id "+ supplyID+" not found for product: "+productName);

        int oldQuantity=supply.shelfQuantity+supply.storageQuantity;
        int newQuantity=newStoreQuantity+newStorageQuantity;
        if(oldQuantity< newQuantity)//
            throw new IllegalArgumentException("updated quantity is greater than previous one! can't happen when you declare missing products!");

        int storageChange=newStorageQuantity-supply.storageQuantity;//new-old
        int shelfChange=newStoreQuantity-supply.shelfQuantity;//new-old

        //update global quantities
        this.storageQuantity+=storageChange;
        this.shelfQuantity+=shelfChange;

        supply.storageQuantity=newStorageQuantity;
        supply.shelfQuantity=newStoreQuantity;

        return oldQuantity-newQuantity;
    }

    /**
     * adds a new supply for a product
     * @param cost the cost per product the the store had to pay
     * @param expirationDate the expiration date for said supply
     * @param storeQuantity the quantity in the store
     * @param storageQuantity the quantity in the storage
     * @param shelfLocation the location in the store
     * @param storageLocation the location in the storage
     * @return the id for this supply
     * @throws Exception if either quantity or cost is ngative
     */
    int addSupply(int cost, LocalDate expirationDate, int storeQuantity, int storageQuantity, String shelfLocation, String storageLocation) throws Exception
    {
        if(storeQuantity<0 || storageQuantity<0 || cost<0)
            throw new IllegalArgumentException("one of the quantities or the cost is negative!");
        int id=latestSupplyId++;
        Supply newSupply= new Supply(id, cost, expirationDate, storeQuantity, storageQuantity, shelfLocation, storageLocation);
        this.shelfQuantity+=storeQuantity;
        this.storageQuantity+=storageQuantity;
        supplies.put(id,newSupply);
        return id;
    }

    /*
    will represent each supply independetly
    each supply hold its own experation date
    and its own cost price to allow managing purchacing prices
     */
    private class Supply
    {
        private final int supplyID;
        private final int cost;//the cost per product that the store had to pay
        private final LocalDate expirationDate;

        private int shelfQuantity;
        private int storageQuantity;

        private String storeLocation;
        private String storageLocation;

        Supply(int supplyID, int cost, LocalDate expirationDate, int shelfQuantity, int storageQuantity, String storeLocation, String storageLocation )
        {
            this.supplyID=supplyID;
            this.cost=cost;
            this.expirationDate=expirationDate;
            this.shelfQuantity=shelfQuantity;
            this.storageQuantity=storageQuantity;
            this.storeLocation=storeLocation;
            this.storageLocation=storageLocation;
        }
    }
}
