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
 * Kelas Order untuk mengelola pesanan
 */
public class Order {

    private final ArrayList<MenuItem> orderItem;
    private final ArrayList<Integer> totalItem;
    private Diskon usedDiscount;
    private String customerName;
    protected static int orderNumberCounter = 1;
    protected int orderNumber;
    private NumberFormat formatter;

    // Constructor
    public Order(String customerName, NumberFormat formatter) {
        this.orderItem = new ArrayList<>();
        this.totalItem = new ArrayList<>();
        this.customerName = customerName;
        this.orderNumber = orderNumberCounter++;
        this.usedDiscount = null;
        this.formatter = formatter;
    }

    /**
     * Metode utama untuk proses pemesanan
     */
    public void processOrder(Scanner scanner, MenuManagement menuManagement) {
        try {
            boolean pesanLagi = true;

            // Loop pemesanan item
            while (pesanLagi) {
                try {
                    // Tampilkan menu TANPA diskon
                    menuManagement.printMenuWithoutDiscount();

                    // Input nama item
                    System.out.print("\nMasukkan nama menu yang dipesan: ");
                    String namaItem = scanner.nextLine().trim();

                    if (namaItem.isEmpty()) {
                        System.out.println("✗ Nama menu tidak boleh kosong!");
                        continue;
                    }

                    // Cari item di menu
                    MenuItem item = menuManagement.searchMenu(namaItem);

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
                    addOrderItem(item, jumlah);

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

            useDiscountInteraktif(scanner, menuManagement);

            // Tampilkan dan simpan struk
            System.out.println("\n" + "=".repeat(70));
            System.out.println("              PESANAN BERHASIL DIBUAT!");
            System.out.println("=".repeat(70));

            showStruct();
            saveStructToFile();

        } catch (Exception e) {
            System.out.println("✗ Terjadi kesalahan dalam proses order: " + e.getMessage());
        }
    }

    /**
     * Metode untuk menerapkan diskon secara interaktif
     */
    private void useDiscountInteraktif(Scanner scanner, MenuManagement menuManagement) {
        System.out.print("\nMau pakai diskon? (Y/N): ");
        String pakaiDiskon = getYesNoInput(scanner);

        if (!pakaiDiskon.equalsIgnoreCase("Y")) {
            System.out.println("Lanjut tanpa diskon.");
            return;
        }

        ArrayList<Diskon> daftarDiskon = menuManagement.getDiscounts();
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

        while (true) {
            try {
                System.out.print("\nMasukkan nomor diskon yang ingin dipakai (0 untuk batal): ");
                int nomorDiskon = Integer.parseInt(scanner.nextLine().trim());

                if (nomorDiskon == 0) {
                    System.out.println("Lanjut tanpa diskon.");
                    return;
                }

                if (nomorDiskon < 1 || nomorDiskon > daftarDiskon.size()) {
                    System.out.println("✗ Nomor diskon tidak valid! Pilih antara 1-" + daftarDiskon.size());
                    continue;
                }

                // Ambil diskon berdasarkan index (nomor - 1)
                Diskon diskon = daftarDiskon.get(nomorDiskon - 1);
                useDiscount(diskon);
                System.out.println("✓ Diskon " + diskon.getName() + " berhasil diterapkan!");
                break;

            } catch (NumberFormatException e) {
                System.out.println("✗ Input tidak valid! Masukkan nomor diskon.");
            } catch (Exception e) {
                System.out.println("✗ " + e.getMessage());
                System.out.println("Lanjut tanpa diskon.");
                break;
            }
        }
    }

    /**
     * Menambahkan item ke pesanan
     */
    public void addOrderItem(MenuItem item, int jumlah) {
        if (item == null) {
            throw new IllegalArgumentException("Item tidak boleh null!");
        }

        if (jumlah <= 0) {
            throw new IllegalArgumentException("Jumlah harus lebih dari 0!");
        }

        // Cek apakah item sudah ada dalam pesanan
        int index = -1;
        for (int i = 0; i < orderItem.size(); i++) {
            if (orderItem.get(i).getName().equals(item.getName())) {
                index = i;
                break;
            }
        }

        if (index != -1) {
            // Jika item sudah ada, tambahkan jumlahnya
            totalItem.set(index, totalItem.get(index) + jumlah);
        } else {
            // Jika item baru, tambahkan ke list
            orderItem.add(item);
            totalItem.add(jumlah);
        }

        System.out.println("✓ " + jumlah + "x " + item.getName() + " ditambahkan ke pesanan");
    }

    /**
     * Menerapkan diskon
     */
    public void useDiscount(Diskon diskon) {
        if (diskon == null) {
            throw new IllegalArgumentException("Diskon tidak boleh null!");
        }
        this.usedDiscount = diskon;
    }

    /**
     * Menghitung subtotal (sebelum diskon)
     */
    public double calculateSubTotal() {
        double subtotal = 0;
        for (int i = 0; i < orderItem.size(); i++) {
            subtotal += orderItem.get(i).getPrice() * totalItem.get(i);
        }
        return subtotal;
    }

    /**
     * Menghitung total (setelah diskon)
     */
    public double calculateTotal() {
        double subtotal = calculateSubTotal();
        if (usedDiscount != null) {
            double potongan = usedDiscount.hitungDiskon(subtotal);
            return subtotal - potongan;
        }
        return subtotal;
    }

    /**
     * Menghitung jumlah diskon
     */
    public double calculateDiscount() {
        if (usedDiscount != null) {
            return usedDiscount.hitungDiskon(calculateSubTotal());
        }
        return 0;
    }

    /**
     * Menampilkan struk pesanan
     */
    public void showStruct() {
        try {
            if (orderItem.isEmpty()) {
                System.out.println("✗ Pesanan masih kosong!");
                return;
            }

            System.out.println("\n" + "=".repeat(70));
            System.out.println("                    STRUK PESANAN RESTORAN");
            System.out.println("=".repeat(70));
            System.out.println("No. Pesanan : #" + orderNumber);
            System.out.println("Pelanggan   : " + customerName);
            System.out.println("Tanggal     : " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            System.out.println("-".repeat(70));
            System.out.printf("%-30s %10s %15s %15s%n", "Item", "Jumlah", "Harga Satuan", "Subtotal");
            System.out.println("-".repeat(70));

            for (int i = 0; i < orderItem.size(); i++) {
                MenuItem item = orderItem.get(i);
                int qty = totalItem.get(i);
                double subtotalItem = item.getPrice() * qty;
                System.out.printf("%-30s %10d Rp %13s Rp %13s%n",
                        item.getName(), qty,
                        formatter.format(item.getPrice()),
                        formatter.format(subtotalItem));
            }

            System.out.println("-".repeat(70));
            System.out.printf("%-30s %40s Rp %13s%n", "", "SUBTOTAL:",
                    formatter.format(calculateSubTotal()));

            if (usedDiscount != null) {
                System.out.printf("%-30s %40s Rp %13s%n", "",
                        "DISKON (" + usedDiscount.getName() + " "
                        + String.format("%.0f", usedDiscount.getPersenDiskon()) + "%):",
                        formatter.format(-calculateDiscount()));
            }

            System.out.printf("%-30s %40s Rp %13s%n", "", "TOTAL:",
                    formatter.format(calculateTotal()));
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
    public void saveStructToFile() {
        // Buat folder orders-file jika belum ada
        File folder = new File("./orderapp/storage/orders-file/");
        if (!folder.exists()) {
            folder.mkdirs();
        }

        String namaFile = "./orderapp/storage/orders-file/struk_pesanan_" + orderNumber + ".txt";
        PrintWriter writer = null;

        try {
            writer = new PrintWriter(new FileWriter(namaFile));

            writer.println("=".repeat(70));
            writer.println("                    STRUK PESANAN RESTORAN");
            writer.println("=".repeat(70));
            writer.println("No. Pesanan : #" + orderNumber);
            writer.println("Pelanggan   : " + customerName);
            writer.println("Tanggal     : " + LocalDateTime.now().format(
                    DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss")));
            writer.println("-".repeat(70));
            writer.printf("%-30s %10s %15s %15s%n", "Item", "Jumlah", "Harga Satuan", "Subtotal");
            writer.println("-".repeat(70));

            for (int i = 0; i < orderItem.size(); i++) {
                MenuItem item = orderItem.get(i);
                int qty = totalItem.get(i);
                double subtotalItem = item.getPrice() * qty;
                writer.printf("%-30s %10d Rp %13s Rp %13s%n",
                        item.getName(), qty,
                        formatter.format(item.getPrice()),
                        formatter.format(subtotalItem));
            }

            writer.println("-".repeat(70));
            writer.printf("%-30s %40s Rp %13s%n", "", "SUBTOTAL:",
                    formatter.format(calculateSubTotal()));

            if (usedDiscount != null) {
                writer.printf("%-30s %40s Rp %13s%n", "",
                        "DISKON (" + usedDiscount.getName() + " "
                        + String.format("%.0f", usedDiscount.getPersenDiskon()) + "%):",
                        formatter.format(-calculateDiscount()));
            }

            writer.printf("%-30s %40s Rp %13s%n", "", "TOTAL:",
                    formatter.format(calculateTotal()));
            writer.println("=".repeat(70));
            writer.println("            Terima kasih atas kunjungan Anda!");
            writer.println("=".repeat(70));

            System.out.println("✓ Struk berhasil disimpan ke file: " + namaFile);
        } catch (IOException e) {
            System.out.println("✗ Error menyimpan struk: " + e.getMessage());
        } finally {
            if (writer != null) {
                writer.close();
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
    public ArrayList<MenuItem> getOrderItem() {
        return orderItem;
    }

    public String getCustomerName() {
        return customerName;
    }

    public int getOrderNumber() {
        return orderNumber;
    }

    public boolean isEmpty() {
        return orderItem.isEmpty();
    }

    public Diskon getUsedDiscount() {
        return usedDiscount;
    }

    public NumberFormat getFormatter() {
        return formatter;
    }

    // Setter methods
    public void setUsedDiscount(Diskon usedDiscount) {
        this.usedDiscount = usedDiscount;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public void setFormatter(NumberFormat formatter) {
        this.formatter = formatter;
    }
}
