package orderapp.menu;

/**
 * Kelas Diskon sebagai turunan dari MenuItem
 */
public class Diskon extends MenuItem {

    private double persenDiskon; // Persentase diskon (contoh: 10 untuk 10%)

    // Constructor
    public Diskon(String name, double persenDiskon) {
        super(name, 0, "Diskon");
        this.persenDiskon = persenDiskon;
    }

    // Getter dan Setter
    public double getPersenDiskon() {
        return persenDiskon;
    }

    public void setPersenDiskon(double persenDiskon) {
        this.persenDiskon = persenDiskon;
    }

    // Metode untuk menghitung jumlah diskon
    public double hitungDiskon(double totalHarga) {
        return totalHarga * (persenDiskon / 100);
    }

    // Implementasi metode abstrak dari parent class (Polymorphism)
    @Override
    public void showMenu() {
        System.out.printf("%-28s Diskon: %.0f%%%n",
                getName(), persenDiskon);
    }

    // Untuk menyimpan ke file
    @Override
    public String toFileString() {
        return String.format("D;%s;%.2f", getName(), persenDiskon);
    }
}
