package ServiceLayer;
import DomainLayer.*;
public class ServiceFactory {

    private final ProductFacade productFacade;
    private final ReportFacade reportFacade;
    private final ProductService productService;
    private final ReportService reportService;
    public ServiceFactory()
    {
        productFacade=new ProductFacade();
        reportFacade=new ReportFacade(productFacade);
        productService=new ProductService(productFacade);
        reportService=new ReportService(reportFacade);
    }

    public ReportService getReportService()
    {
        return reportService;
    }

    public ProductService getProductService() {
        return productService;
    }
}
