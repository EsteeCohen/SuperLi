package DomainLayer.Inventory;

import java.time.LocalDate;
import java.util.List;

class ExpiryDesc
{
    private String productName;
    private List<LocalDate> expiredDates;
    private int expiredAmount=0;
    public void setExpiredDates(List<LocalDate> expiredDates)
    {
        this.expiredDates = expiredDates;
    }
    public void setProduct(String productName)
    {
        this.productName = productName;
    }
    public boolean isNotEmpty()
    {
        return !expiredDates.isEmpty();
    }

    //used for tests, return the number of supplies
    int getSupplyCount()
    {
        return expiredDates.size();
    }
    //add the amount of the expired supply
    void addAmount(int amount)
    {
        this.expiredAmount+=amount;
    }

    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("product:"+productName + ":, amount:" +String.valueOf(expiredAmount)+ "\n");
        for (int i = 0; i < expiredDates.size(); i++)
        {
            toReturn.append("    ").append(expiredDates.get(i)).append("\n");
        }
        return toReturn.toString();
    }
}
