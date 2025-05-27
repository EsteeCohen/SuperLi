package DomainLayer.Inventory;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

class InventoryReport
{
    private List<InventoryDesc> inventoryDescs=new ArrayList<>();
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
        StringBuilder toReturn = new StringBuilder("the inventory as described as of " + reportDate + " is; " + "\n");
        for (InventoryDesc inventoryDesc : inventoryDescs) {
            toReturn.append(inventoryDesc.toString()).append("\n");
        }
        return toReturn.toString();
    }
}
