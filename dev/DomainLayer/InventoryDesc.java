package DomainLayer;

import java.util.List;

class InventoryDesc
{
    private String productName;
    private int totalQuantity;

    public InventoryDesc(String productName, int totalQuantity)
    {
        this.productName = productName;
        this.totalQuantity = totalQuantity;
    }

    @Override
    public String toString()
    {
        return productName + ":" + totalQuantity;
    }
}
