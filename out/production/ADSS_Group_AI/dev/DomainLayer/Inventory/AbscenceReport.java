package DomainLayer.Inventory;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AbscenceReport
{
    List<AbscenceDesc> abscenceDescriptions=new ArrayList<>();
    private final LocalDate reportDate;

    public void add(AbscenceDesc abscenceReport)
    {
        abscenceDescriptions.add(abscenceReport);
    }
    public AbscenceReport()
    {
        this.reportDate = LocalDate.now();
    }
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("the product with an absence as of " + reportDate + " are: " + "\n");
        for (AbscenceDesc abscenceDesc : abscenceDescriptions) {
            if (abscenceDesc.getAbscenceAmount() > 0)
                toReturn.append(abscenceDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }

}
