package src.DomainLayer;

public class SystemController {
    private static SystemController instance;
    private SupplierController supplierController;
    private ProductController productController;
    private OrderController orderController;

    private void SupplierController(){
        this.supplierController = SupplierController.getInstance();
        this.productController = ProductController.getInstance();
        this.orderController = OrderController.getInstance();
    }

    public static SystemController getInstance() {
        if (instance == null) {
            instance = new SystemController();
        }
        return instance;
    }
}
