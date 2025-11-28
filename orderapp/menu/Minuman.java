package orderapp.menu;

public class Minuman extends MenuItem {

    public Minuman(String nama, double harga, String type) {
        super(nama, harga, type);
    }

    @Override
    public String printMenu() {
        return String.format("Minuman: %s | Rp%.2f | Jenis: %s", this.getName(), this.getPrice(), this.getType());
    }
}
