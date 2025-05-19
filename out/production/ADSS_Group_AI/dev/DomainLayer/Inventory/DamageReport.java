package DomainLayer.Inventory;

import DomainLayer.Inventory.Product;

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
        StringBuilder toReturn = new StringBuilder("the defected products reported as of " + reportDate + " are: " + "\n");
        for (DamageDesc damageDesc : damageDescriptions) {
            toReturn.append(damageDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }
}
