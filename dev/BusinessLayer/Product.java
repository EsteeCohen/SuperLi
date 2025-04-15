package BusinessLayer;

import java.time.LocalDate;
import java.util.List;

class Product {
    private final String productName;
    private final int supplyID;
    private final List<String> subCategories;
    private final String manufacturer;
    private final LocalDate experationDate;

    private int shelfQuantity;
    private int storageQuantity;
    private int minQuantity;
    private final double costPrice;
    private final double sellPrice;
    private Discount discount;

    Product(String productName, int supplyID, List<String> subCategories, String manufacturer, LocalDate experationDate, int shelfQuantity, int storageQuantity, int costPrice, int sellPrice)
    {
        this.productName=productName;
        this.supplyID=supplyID;
        this.subCategories=subCategories;
        this.manufacturer=manufacturer;
        this.experationDate=experationDate;
        this.shelfQuantity=shelfQuantity;
        this.storageQuantity=storageQuantity;
        this.minQuantity=0;
        this.costPrice=costPrice;
        this.sellPrice=sellPrice;
        this.discount=null;

    }
}
