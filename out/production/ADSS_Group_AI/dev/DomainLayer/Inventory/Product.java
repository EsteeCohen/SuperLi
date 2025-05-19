package DomainLayer.Inventory;
import DomainLayer.TimeController;

import java.sql.Time;
import java.time.LocalDate;
import java.util.*;

class Product {
    private final int BUFFER_SUPPLY_DAYS = 1;
    private final int DAYS_BETWEEN_REPORTS = 3;//monday to thursday, thursday to monday, we don't work during shabat

    private final String productName;
    private final List<String> subCategories;
    private final String manufacturer;
    private String storeLocation;
    private String storageLocation;

    private int latestSupplyId = 0;
    private int shelfQuantity;
    private int storageQuantity;

    private int minQuantity;

    private double sellPrice;
    private Discount discount;
    private final TreeMap<LocalDate, Sale> salesReports = new TreeMap<>();
    int brokenQuantity;

    private List<Sale> latestSales;//saves sales data since the last restart
    private int latestSalesCount;//count the sales since the last restart

    private int supplyRate = 7;//the days it takes for a new supply to come, assume for now a week

    private final Map<Integer, Supply> supplies = new HashMap<>();//a map of all the supplies

    public Product(String productName, List<String> subCategories, String manufacturer, double sellPrice, String shelfPlace, String storageLocation) {
        this.productName = productName;
        this.subCategories = subCategories;
        this.manufacturer = manufacturer;

        //quantities=0 by default, you add to it by adding batches
        this.shelfQuantity = 0;
        this.storageQuantity = 0;
        this.minQuantity = 0;

        this.sellPrice = sellPrice;
        this.discount = null;
        latestSales = new ArrayList<>();
        latestSalesCount = 0;

        this.storeLocation = shelfPlace;
        this.storageLocation = storageLocation;
    }

    String getProductName() {
        return productName;
    }

    Discount getDiscount() {
        return discount;
    }

    public void SetDiscount(Discount discount) {
        this.discount = discount;
    }

    public int GetShelfQuantity() {
        return this.shelfQuantity;
    }

    public int GetStorageQuantity() {
        return this.storageQuantity;
    }

    List<Sale> getLatestSales() {
        return latestSales;
    }

    int getLatestSalesCount() {
        return latestSalesCount;
    }

    int getMinQuantity() {
        return minQuantity;
    }

    int getBrokenQuantity() {
        return brokenQuantity;
    }

    /**
     * tells the product to start counting sales from the beginning
     * should be used after getting the latest report
     */
    void restartSales() {
        latestSales = new ArrayList<>();
        latestSalesCount = 0;
    }

    /**
     * tells the product to start counting brokens from the beginning
     * should be used after getting the latest report
     */
    void restartBroken() {
        brokenQuantity = 0;
    }

    /**
     * updates the quantity and writes the missing amount as sold
     *
     * @param supplyID        the id of the supply to update
     * @param storeQuantity   the new shelf quantity
     * @param storageQuantity the new storage quantity
     * @throws Exception if the updated quantity is greater than before
     */
    void updateSoldQuantity(int supplyID, int storeQuantity, int storageQuantity) throws Exception {
        int totalSales = updateQuantities(supplyID, storeQuantity, storageQuantity);

        //document sells?
        if (discount != null && discount.getEndDate().isBefore(TimeController.getDate()))//discount ended
            discount = null;
        if (totalSales == 0)
            return;//if just moved, no need to for updates

        Sale sale = new Sale(totalSales, sellPrice, discount == null ? 0 : discount.getPrecentage());
        latestSales.add(sale);
        latestSalesCount += totalSales;
    }


    /**
     * updates the quantity and writes the missing amount as broken
     *
     * @param supplyId        the id of the supply to update
     * @param storeQuantity   the new shelf quantity
     * @param storageQuantity the new storage quantity
     * @throws Exception if the updated quantity is greater than before
     */
    void updateFoundBrokenItems(int supplyId, int storeQuantity, int storageQuantity) throws Exception {
        int totalLost = updateQuantities(supplyId, storeQuantity, storageQuantity);
        brokenQuantity += totalLost;
    }

    /**
     * calcs the new min quantity
     * since we report every 72 hours, we just take the accumilated sales and divide them by 3 to get the daily average
     * then we multiply by the days it takes for new supply to arrive+some buffer
     * new min= daily sales average*(time it takes for new supply+1)
     */
    void calcMinQuantity() {
        int dailyAvg = (int) Math.ceil((double) latestSalesCount / (double) DAYS_BETWEEN_REPORTS);
        minQuantity = dailyAvg * (supplyRate + BUFFER_SUPPLY_DAYS);
    }

    /**
     * updates the quantities per batch of a product
     *
     * @param supplyID           the id of the supply to be updated
     * @param newStoreQuantity   the quantity left in the store
     * @param newStorageQuantity the quantity left in the storage
     * @return the total difference between the old quantity and the new quantity
     * @throws Exception if supplyID not found, if given negative number
     */
    private int updateQuantities(int supplyID, int newStoreQuantity, int newStorageQuantity) throws Exception {
        if (newStoreQuantity < 0 || newStorageQuantity < 0)//cant have negative quantities
            throw new IllegalArgumentException("Cannot save negative quantities");

        Supply supply = supplies.get(supplyID);

        if (supply == null)//supply id not found
            throw new NoSuchElementException("Supply id " + supplyID + " not found for product: " + productName);

        int oldQuantity = supply.shelfQuantity + supply.storageQuantity;
        int newQuantity = newStoreQuantity + newStorageQuantity;
        if (oldQuantity < newQuantity)//
            throw new IllegalArgumentException("updated quantity is greater than previous one! can't happen when you declare missing products!");

        int storageChange = newStorageQuantity - supply.storageQuantity;//new-old
        int shelfChange = newStoreQuantity - supply.shelfQuantity;//new-old

        //update global quantities
        this.storageQuantity += storageChange;
        this.shelfQuantity += shelfChange;

        supply.storageQuantity = newStorageQuantity;
        supply.shelfQuantity = newStoreQuantity;

        if (newQuantity == 0)//if supply is empty, delete it
            supplies.remove(supplyID);

        return oldQuantity - newQuantity;
    }

    /**
     * adds a new supply for a product
     *
     * @param cost            the cost per product the the store had to pay
     * @param expirationDate  the expiration date for said supply
     * @param storeQuantity   the quantity in the store
     * @param storageQuantity the quantity in the storage
     * @return the id for this supply
     * @throws Exception if either quantity or cost is ngative
     */
    int addSupply(double cost, LocalDate expirationDate, int storeQuantity, int storageQuantity) throws Exception {
        if (storeQuantity < 0 || storageQuantity < 0 || cost < 0)
            throw new IllegalArgumentException("one of the quantities or the cost is negative!");
        if (storeQuantity + storageQuantity == 0)
            throw new IllegalArgumentException("new supply has quantity of 0! its like its not even there!");
        int id = latestSupplyId++;
        Supply newSupply = new Supply(id, cost, expirationDate, storeQuantity, storageQuantity);
        this.shelfQuantity += storeQuantity;
        this.storageQuantity += storageQuantity;
        supplies.put(id, newSupply);
        return id;
    }

    public ExpiryDesc GetExpiryDescription(LocalDate until) {
        ExpiryDesc expiryDesc = new ExpiryDesc();

        List<LocalDate> expiredDates = new ArrayList<>();
        List<Supply> allSupplies = new ArrayList<>(supplies.values());
        int amount=0;

            for (Supply supply : allSupplies) {
                if (supply.expirationDate.isBefore(until)) {
                    expiredDates.add(supply.expirationDate);
                    expiryDesc.addAmount( supply.shelfQuantity+supply.storageQuantity);
                }
            }
            expiryDesc.setProduct(productName);
            expiryDesc.setExpiredDates(expiredDates);

            return expiryDesc;
        }
        public InventoryDesc GetInventoryDescription ()
        {
            return new InventoryDesc(productName, shelfQuantity + storageQuantity);
        }
        public DamageDesc GetDamageDescription ()
        {
            int brokenQuantity = this.brokenQuantity;
            restartBroken();

            return new DamageDesc(productName, brokenQuantity);
        }

    public AbscenceDesc GetAbscenceDescription()
    {
        return new AbscenceDesc(productName, minQuantity - (storageQuantity + shelfQuantity));
    }

    /*
    will represent each supply independetly
    each supply hold its own experation date
    and its own cost price to allow managing purchacing prices
     */
    private class Supply {
        private final int supplyID;
        private final double cost;//the cost per product that the store had to pay
        private final LocalDate expirationDate;

        private int shelfQuantity;
        private int storageQuantity;


        Supply(int supplyID, double cost, LocalDate expirationDate, int shelfQuantity, int storageQuantity) {
            this.supplyID = supplyID;
            this.cost = cost;
            this.expirationDate = expirationDate;
            this.shelfQuantity = shelfQuantity;
            this.storageQuantity = storageQuantity;
        }
    }
}
