package orderapp;

public class Menu {

    private String name;

    private double price;

    private MenuCategory category;

    public Menu(String name, double price, MenuCategory category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getPrice() {
        return this.price;
    }

    public void setCategory(MenuCategory category) {
        this.category = category;
    }

    public MenuCategory getCategory() {
        return this.category;
    }
}
