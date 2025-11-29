package orderapp.menu;

/**
 * Kelas abstrak MenuItem sebagai parent class untuk semua item menu
 */
public abstract class MenuItem {

    // Atribut dengan access modifier private (Encapsulation)
    private String name;
    private double price;
    private String category;

    // Constructor
    public MenuItem(String name, double price, String category) {
        this.name = name;
        this.price = price;
        this.category = category;
    }

    // Getter dan Setter (Encapsulation)
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    // Metode abstrak yang harus diimplementasikan oleh subclass (Polymorphism)
    public abstract void showMenu();

    // Metode untuk format ke string (untuk file I/O)
    public abstract String toFileString();
}
