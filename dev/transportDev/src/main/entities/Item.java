package src.main.entities;


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

 //    METHODS
    public double getTotalWeight(){
        return weight * quantity;
    }
    @Override
    public String toString() {
        return "פריט #" + id + ": " + name +
                ", כמות: " + quantity +
                ", משקל ליחידה: " + weight + " ק\"ג" +
                ", משקל כולל: " + getTotalWeight() + " ק\"ג";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Item item)) return false;
        return this.id == item.id;
    }

// :)
}
