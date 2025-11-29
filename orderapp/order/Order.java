package orderapp.order;

import orderapp.menu.MenuItem;
import orderapp.menu.Diskon;
import orderapp.menu.MenuManagement;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.text.NumberFormat;

/**
 * Kelas Order untuk mengelola pesanan pelanggan Menerapkan konsep
 * Encapsulation, Exception Handling, dan File I/O Dengan mekanisme: Pesan ->
 * Mau pesan lagi? -> Terapkan diskon -> Cetak struk
 */
public class Order {

    private final ArrayList<MenuItem> itemPesanan;
    private final ArrayList<Integer> jumlahItem;
    private Diskon diskonDipakai;
    private String namaPelanggan;
    protected static int nomorPesananCounter = 1;
    protected int nomorPesanan;
    private NumberFormat formatter;

    // Constructor
    public Order(String namaPelanggan, NumberFormat formatter) {
        this.itemPesanan = new ArrayList<>();
        this.jumlahItem = new ArrayList<>();
        this.namaPelanggan = namaPelanggan;
        this.nomorPesanan = nomorPesananCounter++;
        this.diskonDipakai = null;
        this.formatter = formatter;
    }

    /**
     * Metode utama untuk proses pemesanan dengan flow: 1. Pesan item (tanpa
     * menampilkan diskon) 2. Tanya "Mau pesan lagi?" (Y/N) 3. Jika N, tanya
     * "Mau pakai diskon?" (Y/N) 4. Tampilkan dan simpan struk
     */
    public void prosesOrder(Scanner scanner, MenuManagement menuManagement) {
        try {
            boolean pesanLagi = true;

            // Loop pemesanan item
            while (pesanLagi) {
                try {
                    // Tampilkan menu TANPA diskon
                    menuManagement.printMenuTanpaDiskon();

                    // Input nama item
                    System.out.print("\nMasukkan nama menu yang dipesan: ");
                    String namaItem = scanner.nextLine().trim();

                    if (namaItem.isEmpty()) {
                        System.out.println("✗ Nama menu tidak boleh kosong!");
                        continue;
                    }

                    // Cari item di menu
                    MenuItem item = menuManagement.cariMenu(namaItem);

                    // Validasi: tidak bisa memesan diskon
                    if (item instanceof Diskon) {
                        System.out.println("✗ Diskon tidak bisa dipesan sebagai item!");
                        continue;
                    }

                    // Input jumlah
                    System.out.print("Masukkan jumlah: ");
                    int jumlah = getIntInput(scanner);

                    if (jumlah <= 0) {
                        System.out.println("✗ Jumlah harus lebih dari 0!");
                        continue;
                    }

                    // Tambahkan ke pesanan
                    tambahItemPesanan(item, jumlah);

                } catch (Exception e) {
                    System.out.println("✗ " + e.getMessage());
                    System.out.println("Silakan coba lagi.");
                    continue;
                }

                // Tanya apakah mau pesan lagi
                System.out.print("\nMau pesan lagi? (Y/N): ");
                String jawaban = getYesNoInput(scanner);

                if (jawaban.equalsIgnoreCase("N")) {
                    pesanLagi = false;
                }
            }

            // Cek apakah ada pesanan
            if (isEmpty()) {
                System.out.println("✗ Tidak ada pesanan yang dibuat.");
                return;
            }

            // Tanya apakah mau pakai diskon
            System.out.print("\nMau pakai diskon? (Y/N): ");
            String pakaiDiskon = getYesNoInput(scanner);

            if (pakaiDiskon.equalsIgnoreCase("Y")) {
                terapkanDiskonInteraktif(scanner, menuManagement);
            }

            // Tampilkan dan simpan struk
            System.out.println("\n" + "=".repeat(70));
            System.out.println("              PESANAN BERHASIL DIBUAT!");
            System.out.println("=".repeat(70));

            tampilkanStruk();
            simpanStrukKeFile();

        } catch (Exception e) {
            System.out.println("✗ Terjadi kesalahan dalam proses order: " + e.getMessage());
        }
    }

    /**
     * Metode untuk menerapkan diskon secara interaktif
     */
    private void terapkanDiskonInteraktif(Scanner scanner, MenuManagement menuManagement) {
        try {
            ArrayList<Diskon> daftarDiskon = menuManagement.getDaftarDiskon();

            if (daftarDiskon.isEmpty()) {
                System.out.println("✗ Tidak ada diskon tersedia saat ini.");
                return;
            }

            // Tampilkan diskon yang tersedia
            System.out.println("\n--- DISKON TERSEDIA ---");
            for (int i = 0; i < daftarDiskon.size(); i++) {
                Diskon d = daftarDiskon.get(i);
                System.out.printf("%d. %s - %.0f%% off\n",
                        i + 1, d.getName(), d.getPersenDiskon());
            }

            System.out.print("\nMasukkan nama diskon yang ingin dipakai: ");
            String namaDiskon = scanner.nextLine().trim();

            if (namaDiskon.isEmpty()) {
                System.out.println("✗ Nama diskon tidak boleh kosong. Lanjut tanpa diskon.");
                return;
            }

            // Cari dan terapkan diskon
            Diskon diskon = menuManagement.cariDiskon(namaDiskon);
            terapkanDiskon(diskon);

        } catch (Exception e) {
            System.out.println("✗ " + e.getMessage());
            System.out.println("Lanjut tanpa diskon.");
        }
    }

    /**
     * Menambahkan item ke pesanan
     */
    public void tambahItemPesanan(MenuItem item, int jumlah) {
        if (item == null) {
            throw new IllegalArgumentException("Item tidak boleh null!");
        }

        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0!");
        }

        // Cek apakah item sudah ada dalam pesanan
        int index = -1;
        for (int i = 0; i < itemPesanan.size(); i++) {
            if (itemPesanan.get(i).getName().equals(item.getName())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Jika item sudah ada, tambahkan jumlahnya
            jumlahItem.set(index, jumlahItem.get(index) + jumlah);
        } else {
            // Jika item baru, tambahkan ke list
            itemPesanan.add(item);
            jumlahItem.add(jumlah);
        }

        System.out.println("✓ " + jumlah + "x " + item.getName() + " ditambahkan ke pesanan");
    }

    /**
     * Menerapkan diskon
     */
    public void terapkanDiskon(Diskon diskon) {
        if (diskon == null) {
            throw new IllegalArgumentException("Diskon tidak boleh null!");
        }

        this.diskonDipakai = diskon;
        System.out.println("✓ Diskon " + diskon.getName() + " ("
                + String.format("%.0f", diskon.getPersenDiskon()) + "%) diterapkan!");
    }

    /**
     * Menghitung subtotal (sebelum diskon)
     */
    public double hitungSubtotal() {
        double subtotal = 0;
        for (int i = 0; i < itemPesanan.size(); i++) {
            subtotal += itemPesanan.get(i).getPrice() * jumlahItem.get(i);
        }
        return subtotal;
    }

    /**
     * Menghitung total (setelah diskon)
     */
    public double hitungTotal() {
        double subtotal = hitungSubtotal();
        if (diskonDipakai != null) {
            double potongan = diskonDipakai.hitungDiskon(subtotal);
            return subtotal - potongan;
        }
        return subtotal;
    }

    /**
     * Menghitung jumlah diskon
     */
    public double hitungJumlahDiskon() {
        if (diskonDipakai != null) {
            return diskonDipakai.hitungDiskon(hitungSubtotal());
        }
        return 0;
    }

    /**
     * Menampilkan struk pesanan
     */
    public void tampilkanStruk() {
        try {
            if (itemPesanan.isEmpty()) {
                System.out.println("✗ Pesanan masih kosong!");
                return;
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("                    STRUK PESANAN RESTORAN");
            System.out.println("=".repeat(70));
            System.out.println("No. Pesanan : #" + nomorPesanan);
            System.out.println("Pelanggan   : " + namaPelanggan);
            System.out.println("Tanggal     : " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            System.out.println("-".repeat(70));
            System.out.printf("%-30s %10s %15s %15s%n", "Item", "Jumlah", "Harga Satuan", "Subtotal");
            System.out.println("-".repeat(70));

            for (int i = 0; i < itemPesanan.size(); i++) {
                MenuItem item = itemPesanan.get(i);
                int qty = jumlahItem.get(i);
                double subtotalItem = item.getPrice() * qty;
                System.out.printf("%-30s %10d Rp %13s Rp %13s%n",
                        item.getName(), qty,
                        formatter.format(item.getPrice()),
                        formatter.format(subtotalItem));
            }

            System.out.println("-".repeat(70));
            System.out.printf("%-30s %40s Rp %13s%n", "", "SUBTOTAL:",
                    formatter.format(hitungSubtotal()));

            if (diskonDipakai != null) {
                System.out.printf("%-30s %40s Rp %13s%n", "",
                        "DISKON (" + diskonDipakai.getName() + " "
                        + String.format("%.0f", diskonDipakai.getPersenDiskon()) + "%):",
                        formatter.format(-hitungJumlahDiskon()));
                System.out.println("-".repeat(70));
            }

            System.out.printf("%-30s %40s Rp %13s%n", "", "TOTAL:",
                    formatter.format(hitungTotal()));
            System.out.println("=".repeat(70));
            System.out.println("            Terima kasih atas kunjungan Anda!");
            System.out.println("=".repeat(70) + "\n");
        } catch (Exception e) {
            System.out.println("✗ Gagal menampilkan struk: " + e.getMessage());
        }
    }

    /**
     * Menyimpan struk ke file (File I/O)
     */
    public void simpanStrukKeFile() {
        String namaFile = "struk_pesanan_" + nomorPesanan + ".txt";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(namaFile));

            writer.println("=".repeat(70));
            writer.println("                    STRUK PESANAN RESTORAN");
            writer.println("=".repeat(70));
            writer.println("No. Pesanan : #" + nomorPesanan);
            writer.println("Pelanggan   : " + namaPelanggan);
            writer.println("Tanggal     : " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            writer.println("-".repeat(70));
            writer.printf("%-30s %10s %15s %15s%n", "Item", "Jumlah", "Harga Satuan", "Subtotal");
            writer.println("-".repeat(70));

            for (int i = 0; i < itemPesanan.size(); i++) {
                MenuItem item = itemPesanan.get(i);
                int qty = jumlahItem.get(i);
                double subtotalItem = item.getPrice() * qty;
                writer.printf("%-30s %10d Rp %13s Rp %13s%n",
                        item.getName(), qty,
                        formatter.format(item.getPrice()),
                        formatter.format(subtotalItem));
            }

            writer.println("-".repeat(70));
            writer.printf("%-30s %40s Rp %13s%n", "", "SUBTOTAL:",
                    formatter.format(hitungSubtotal()));

            if (diskonDipakai != null) {
                writer.printf("%-30s %40s Rp %13s%n", "",
                        "DISKON (" + diskonDipakai.getName() + " "
                        + String.format("%.0f", diskonDipakai.getPersenDiskon()) + "%):",
                        formatter.format(-hitungJumlahDiskon()));
                writer.println("-".repeat(70));
            }

            writer.printf("%-30s %40s Rp %13s%n", "", "TOTAL:",
                    formatter.format(hitungTotal()));
            writer.println("=".repeat(70));
            writer.println("            Terima kasih atas kunjungan Anda!");
            writer.println("=".repeat(70));

            System.out.println("✓ Struk berhasil disimpan ke file: " + namaFile);
        } catch (IOException e) {
            System.out.println("✗ Error menyimpan struk: " + e.getMessage());
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (Exception e) {
                    System.out.println("✗ Error menutup file: " + e.getMessage());
                }
            }
        }
    }

    // Helper methods untuk input
    private int getIntInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Input harus berupa angka! Coba lagi: ");
            }
        }
    }

    private String getYesNoInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim().toUpperCase();

                if (input.equals("Y") || input.equals("N")
                        || input.equalsIgnoreCase("YES") || input.equalsIgnoreCase("NO")) {
                    return input.substring(0, 1).toUpperCase(); // Return "Y" or "N"
                }

                System.out.print("Input tidak valid! Masukkan Y atau N: ");
            } catch (Exception e) {
                System.out.print("Input tidak valid! Coba lagi: ");
            }
        }
    }

    // Getter methods
    public ArrayList<MenuItem> getItemPesanan() {
        return itemPesanan;
    }

    public String getNamaPelanggan() {
        return namaPelanggan;
    }

    public int getNomorPesanan() {
        return nomorPesanan;
    }

    public boolean isEmpty() {
        return itemPesanan.isEmpty();
    }

    public Diskon getDiskonDipakai() {
        return diskonDipakai;
    }

    public NumberFormat getFormatter() {
        return formatter;
    }

    // Setter methods
    public void setDiskonDipakai(Diskon diskonDipakai) {
        this.diskonDipakai = diskonDipakai;
    }

    public void setNamaPelanggan(String namaPelanggan) {
        this.namaPelanggan = namaPelanggan;
    }

    public void setFormatter(NumberFormat formatter) {
        this.formatter = formatter;
    }
}
