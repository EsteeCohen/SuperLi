package DomainLayer;

import java.time.LocalDate;
import java.util.List;

class InventoryReport
{
    private List<InventoryDesc> inventoryDescs;
    private final LocalDate reportDate;

    public void add(InventoryDesc inventoryDesc)
    {
        inventoryDescs.add(inventoryDesc);
    }

    public InventoryReport()
    {
        this.reportDate = LocalDate.now();
    }

    @Override
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("המלאי נכון לתאריך " + reportDate + " הוא: " + "\n");
        for (InventoryDesc inventoryDesc : inventoryDescs) {
            toReturn.append(inventoryDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }
}
