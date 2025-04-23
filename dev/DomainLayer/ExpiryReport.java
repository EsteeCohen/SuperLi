package DomainLayer;

import java.time.LocalDate;
import java.util.List;

class ExpiryReport
{
    private List<ExpiryDesc> expiryDescs;
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
        StringBuilder toReturn = new StringBuilder("המוצרים פגי-התוקף נכון לתאריך " + reportDate + " הם: " + "\n");
        for (ExpiryDesc expiryDesc : expiryDescs) {
            toReturn.append(expiryDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }
}
