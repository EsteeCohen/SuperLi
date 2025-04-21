package BusinessLayer;

import java.time.LocalDate;
import java.util.List;

public class ExpiryReport
{
    private List<ExpiryDesc> expiryDescs;
    private LocalDate reportDate;

    public void add(ExpiryDesc currentDesc)
    {
        expiryDescs.add(currentDesc);
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
