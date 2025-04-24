package DomainLayer;

import DomainLayer.Product;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class DamageReport
{
    private List<DamageDesc> damageDescriptions=new ArrayList<>();
    private final LocalDate reportDate;

    public void add(DamageDesc damageDesc)
    {
        damageDescriptions.add(damageDesc);
    }
    public DamageReport()
    {
        this.reportDate = LocalDate.now();
    }
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("המוצרים שהיו פגים נכון לתאריך " + reportDate + " הם: " + "\n");
        for (DamageDesc damageDesc : damageDescriptions) {
            toReturn.append(damageDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }
}
