package src.main.dtos;

public class ItemDTO {
    private int id;
    private String name;
    private double weight;
    private int quantity;
    private String description;

    public ItemDTO(int id, String name, double weight, int quantity, String description) {
        this.id = id;
        this.name = name;
        this.weight = weight;
        this.quantity = quantity;
        this.description = description;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getWeight() {
        return weight;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getDescription() {
        return description;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    // Calculate total weight for this item
    public double getTotalWeight() {
        return weight * quantity;
    }
} 