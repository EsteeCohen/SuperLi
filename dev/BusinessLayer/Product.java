package BusinessLayer;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class Product {
    private final String productName;
    private final List<String> subCategories;
    private final String manufacturer;

    private int shelfQuantity;
    private int storageQuantity;
    private int minQuantity;
    private double sellPrice;
    private Discount discount;

    private final List<supply> supplies=new ArrayList<>();

    Product(String productName, List<String> subCategories, String manufacturer, int sellPrice)
    {
        this.productName=productName;
        this.subCategories=subCategories;
        this.manufacturer=manufacturer;

        //quantities=0 by default, you add to it by adding batches
        this.shelfQuantity=0;
        this.storageQuantity=0;
        this.minQuantity=0;

        this.sellPrice=sellPrice;
        this.discount=null;
    }

    /*
    will represent each supply independetly
    each supply hold its own experation date
    and its own cost price to allow managing purchacing prices
     */
    private class supply
    {
        private final int supplyID;
        private final int cost;//the cost per product that the store had to pay
        private final LocalDate expirationDate;

        private int shelfQuantity;
        private int storageQuantity;

        private String storeLocation;
        private String storageLocation;

        supply(int supplyID, int cost, LocalDate expirationDate, int shelfQuantity, int storageQuantity, String storeLocation, String storageLocation )
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
