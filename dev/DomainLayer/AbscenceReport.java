package DomainLayer;

import java.time.LocalDate;
import java.util.List;

public class AbscenceReport
{
    List<AbscenceDesc> abscenceDescriptions;
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
        StringBuilder toReturn = new StringBuilder("המוצרים שהיה בהם חוסר נכון לתאריך " + reportDate + " הם: " + "\n");
        for (AbscenceDesc abscenceDesc : abscenceDescriptions) {
            if (abscenceDesc.getAbscenceAmount() > 0)
                toReturn.append(abscenceDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }

}
