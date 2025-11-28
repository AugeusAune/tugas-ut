package orderapp.menu;

public abstract class MenuItem {

    private String name;

    private double price;

    private String type;

    public MenuItem(String name, double price, String type) {
        this.name = name;
        this.price = price;
        this.type = type;
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

    public void setType(String type) {
        this.type = type;
    }

    public String getType() {
        return this.type;
    }

    public String getCategory() {
        if (this instanceof Makanan) {
            return "Makanan";
        }
        return "Minuman";
    }

    // metode abstrak untuk polymorphism
    public abstract String printMenu();
}
