package orderapp.menu;

public class Diskon extends MenuItem {

    private double diskonPercent;

    public Diskon(String nama, double harga, String type, double diskonPercent) {
        super(nama, harga, type);
        this.diskonPercent = diskonPercent;
    }

    public double getDiskonPercent() {
        return diskonPercent;
    }

    public void setDiskonPercent(double diskonPercent) {
        this.diskonPercent = diskonPercent;
    }

    @Override
    public String printMenu() {
        return String.format("Diskon: %s | Rp%.2f (placeholder price) | Diskon: %.2f%%", this.getName(), this.getPrice(), this.getDiskonPercent());
    }
}
