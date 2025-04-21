package DomainLayer;

import java.time.LocalDate;
import java.util.List;

public class ExpiryDesc
{
    private String productName;
    private List<LocalDate> expiredDates;

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

    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder(productName + ":" + "\n");
        for (int i = 0; i < expiredDates.size(); i++)
        {
            toReturn.append(expiredDates.get(i)).append("\n");
        }
        return toReturn.toString();
    }
}
