package orderapp.menu;

public class Makanan extends MenuItem {

    public Makanan(String nama, double harga, String type) {
        super(nama, harga, type);
    }

    @Override
    public String printMenu() {
        return String.format("Makanan: %s | Rp%.2f | Jenis: %s", this.getName(), this.getPrice(), this.getType());
    }
}
