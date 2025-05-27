package DomainLayer.Inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class ExpiryReport
{
    private List<ExpiryDesc> expiryDescs=new ArrayList<>();
    private final LocalDate reportDate;

    public void add(ExpiryDesc currentDesc)
    {
        expiryDescs.add(currentDesc);
    }

    public ExpiryReport()
    {
        this.reportDate = LocalDate.now();
    }

    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("the expired product as of " + reportDate + " are: " + "\n");
        for (ExpiryDesc expiryDesc : expiryDescs) {
            toReturn.append(expiryDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }
}
