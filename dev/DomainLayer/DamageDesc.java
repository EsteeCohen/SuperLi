package DomainLayer;

public class DamageDesc
{
    private String productName;
    private int brokenQuantity;

    public DamageDesc(String productName, int brokenQuantity)
    {
        this.productName = productName;
        this.brokenQuantity = brokenQuantity;
    }

    @Override
    public String toString()
    {
        return "The amount of broken products from " + productName + ":" + brokenQuantity;
    }
}
