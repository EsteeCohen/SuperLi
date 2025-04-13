package BussinessLayer;

public class Item {
    private int id;
    private String name;
    private int quantity;
    private double weight;

    public Item(int id, String name, int quantity, double weight) {
        this.id = id;
        this.name = name;
        this.quantity = quantity;
        this.weight = weight;
    }
//     GETTERS
    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }
    public int getQuantity(){
        return quantity;
    }
    public double getWeight(){
        return weight;
    }

//    SETTERS
    public void setQuantity(int quantity){
        this.quantity = quantity;
    }
    public void setWeight(double weight){
        this.weight = weight;
    }

    public double getTotalWeight(){
        return weight * quantity;
    }

}
