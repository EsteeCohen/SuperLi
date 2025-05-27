package DataAccessLayer.DTO;

import DomainLayer.Supplier.Order;
import DomainLayer.Supplier.ContactPerson;
import DomainLayer.Supplier.Agreement;
import DomainLayer.Supplier.Enums.STATUS;

import java.time.LocalDate;
import java.util.Map;

public class OrderDTO {
    private int orderId;
    private String supplierId;
    private LocalDate orderDate;
    private LocalDate supplyDate;
    private String contactName;
    private String contactPhone;
    private int agreementId;
    private String status;
    private double totalPrice;
    private Map<String, Integer> items;
    private Boolean isPeriodic;

    /**
     * Full constructor for DTO
     */
    public OrderDTO(int orderId,
                    String supplierId,
                    LocalDate orderDate,
                    LocalDate supplyDate,
                    String contactName,
                    String contactPhone,
                    int agreementId,
                    String status,
                    double totalPrice,
                    Map<String, Integer> items,
                    Boolean isPeriodic) {
        this.orderId = orderId;
        this.supplierId = supplierId;
        this.orderDate = orderDate;
        this.supplyDate = supplyDate;
        this.contactName = contactName;
        this.contactPhone = contactPhone;
        this.agreementId = agreementId;
        this.status = status;
        this.totalPrice = totalPrice;
        this.items = items;
        this.isPeriodic = isPeriodic;
    }

    /**
     * Build DTO from domain object
     */
    public OrderDTO(Order order, Boolean isPeriodic) {
        this(order.getOrderId(),
                order.getSupplierId(),
                order.getDate(),
                order.getSupplyDate(),
                order.getContactPerson().getContactName(),
                order.getContactPerson().getPhoneNumber(),
                order.getAgreement().getAgreementId(),
                order.getStatus().toString(),
                order.getTotalPrice(),
                order.getItems(), isPeriodic);
    }

    /**
     * Convert DTO back to domain object
     */
    public Order toDomain() {
        // Reconstruct contact person
        ContactPerson contact = new ContactPerson(contactName, contactPhone);
        // Minimal Agreement placeholder; fetch full Agreement if needed
        Agreement agreement = new Agreement(supplierId, agreementId, null, null, null, null);
        return new Order(orderId,
                supplierId,
                orderDate,
                contact,
                agreement,
                supplyDate,
                items,
                STATUS.valueOf(status),
                totalPrice);
    }

    // Getters
    public int getOrderId() { return orderId; }
    public String getSupplierId() { return supplierId; }
    public LocalDate getOrderDate() { return orderDate; }
    public LocalDate getSupplyDate() { return supplyDate; }
    public String getContactName() { return contactName; }
    public String getContactPhone() { return contactPhone; }
    public int getAgreementId() { return agreementId; }
    public String getStatus() { return status; }
    public double getTotalPrice() { return totalPrice; }
    public Map<String, Integer> getItems() { return items; }

    public void setStatus(String newStatus) {
        this.status = STATUS.valueOf(newStatus).toString();
    }

    public void addProduct(String catalogNumber, int quantity) {
        if (items.containsKey(catalogNumber)) {
            items.put(catalogNumber, items.get(catalogNumber) + quantity);
        } else {
            items.put(catalogNumber, quantity);
        }
    }

    public void update(Order order) {
        this.supplierId = order.getSupplierId();
        this.orderDate = order.getDate();
        this.contactName = order.getContactPerson().getContactName();
        this.contactPhone = order.getContactPerson().getPhoneNumber();
        this.agreementId = order.getAgreement().getAgreementId();
        this.status = order.getStatus().toString();
        this.totalPrice = order.getTotalPrice();
        this.items = order.getItems();
    }

    public void setSupplyDate(LocalDate supplyDate) {
        this.supplyDate = supplyDate;
    }
}
