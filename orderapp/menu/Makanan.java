package orderapp.menu;

/**
 * Kelas Makanan sebagai turunan dari MenuItem
 */
public class Makanan extends MenuItem {

    private String type; // Jenis makanan: bakaran, goreng, default, dll

    // Constructor
    public Makanan(String name, double price, String type) {
        super(name, price, "Makanan");
        this.type = type;
    }

    // Getter dan Setter
    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // Implementasi metode abstrak dari parent class (Polymorphism)
    @Override
    public void showMenu() {
        System.out.printf("%-28s Rp. %,.2f (Jenis: %s)%n",
                getName(), getPrice(), type);
    }

    // Untuk menyimpan ke file
    @Override
    public String toFileString() {
        return String.format("M;%s;%.2f;%s", getName(), getPrice(), type);
    }
}
