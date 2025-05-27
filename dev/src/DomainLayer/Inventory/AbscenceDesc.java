package DomainLayer.Inventory;

public class AbscenceDesc
{
    private String productName;
    private int abscenceAmount;

    public AbscenceDesc(String productName, int abscenceAmount)
    {
        this.productName = productName;
        this.abscenceAmount = abscenceAmount;
    }

    public int getAbscenceAmount() {
        return abscenceAmount;
    }

    @Override
    public String toString()
    {
        return "    The amount of absent products from " + productName + ":" + abscenceAmount;
    }
}
