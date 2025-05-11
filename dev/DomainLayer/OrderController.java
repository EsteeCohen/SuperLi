package DomainLayer;

import DomainLayer.Inventory.ProductFacade;
import DomainLayer.Inventory.ReportFacade;
import ServiceLayer.SupplierSystemService;
import DomainLayer.Supplier.Order;


import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OrderController
{
    /*Dependencies injected via constructor*/
    private final TimeController timeController;

    private final SupplierSystemService supplierService;
    private final ProductFacade productFacade;
    private final ReportFacade reportFacade;
    private List<Order> orders;


    private static OrderController instance;

    public static OrderController getInstance(){
        if(instance == null)
            instance = new OrderController();
        return instance;
    }
    private OrderController() {
        this.supplierService = SupplierSystemService.getInstance();
        this.productFacade = ProductFacade.getInstance();
        this.reportFacade = ReportFacade.getInstance();
        this.orders = new ArrayList<Order>();
        this.timeController = TimeController.getInstance();


    }


    /**
     * Declare that a specific product is below its minimum
     * stock level (manual or automatic trigger).
     *
     * @param productName   logical product key in inventory
     * @param missingUnits  how many units are required to restore
     *                      stock to the defined minimum
     */
    public void reportStockShortage(String productName, int missingUnits)
    {

    }

    /**
     * Decide which supplier can satisfy the requested quantity
     * for the given product at the best total price, according to
     * current agreements and contract quantities.
     *
     * @param productName      logical product key
     * @param requiredQuantity units to order
     * @return supplierId      identifier of the chosen supplier
     */
    public String chooseBestSupplier(String productName, int requiredQuantity);

    /**
     * Notify the system that a previously issued order has
     * physically arrived.
     *
     * @param orderId            system‑wide order identifier
     * @param actualArrivalDate  real arrival date at warehouse
     */
    public void confirmOrderArrival(int orderId, LocalDate actualArrivalDate);

    /**
     * Retrieve every order whose scheduled supply date equals
     * the given date (e.g., "today").
     *
     * @param date a calendar day to check
     * @return list of Order domain entities
     */
    public List<Order> getOrdersArrivingOn(LocalDate date);

    /*------------------------------------------------------
     *  Suggested additional capabilities
     *----------------------------------------------------*/

    /**
     * Automatically raise a purchase order for each product
     * whose projected stock (until next delivery) falls below
     * its minimum level.  Aggregates shortages discovered in
     * the latest Abscence/Damage/Expiry reports.
     */
    public void createAutomaticShortageOrders();

    /**
     * Generate or refresh standing (periodic) orders one day
     * before each supplier’s fixed delivery day, ensuring that
     * projected stock after the delivery will be above minimum.
     */
    public void updatePeriodicOrders();

    /**
     * Recalculate inventory quantities and release updated
     * reports after an order is confirmed as delivered.
     *
     * @param orderId id of the delivered order
     */
    public void reconcileStockAfterArrival(int orderId);

    /**
     * Helper used internally to build the item‑quantity map
     * for a given shortage batch keyed by catalog number.
     *
     * @param shortages map<productName, missingUnits>
     * @return Map<catalogNumber, quantityPerCatalog>
     */
    private Map<String,Integer> mapShortagesToCatalog(Map<String,Integer> shortages);
}



