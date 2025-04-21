package BusinessLayer;

import java.time.LocalDate;
import java.util.List;

public class InventoryReport
{
    private List<InventoryDesc> inventoryDescs;
    private LocalDate reportDate;

    public void add(InventoryDesc inventoryDesc)
    {
        inventoryDescs.add(inventoryDesc);
    }
    public String toString()
    {
        StringBuilder toReturn = new StringBuilder("המלאי נכון לתאריך " + reportDate + " הוא: " + "\n");
        for (InventoryDesc inventoryDesc : inventoryDescs) {
            toReturn.append(inventoryDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }
}
