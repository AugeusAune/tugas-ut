package orderapp.order;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**
 * Kelas OrderManagement untuk mengelola semua pesanan Dengan fitur save/load
 * dari file struk
 */
public class OrderManagement {

    private final ArrayList<Order> daftarPesanan;
    private final NumberFormat formatter;

    public OrderManagement(NumberFormat formatter) {
        this.daftarPesanan = new ArrayList<>();
        this.formatter = formatter;
    }

    /**
     * Load semua pesanan dari file struk yang ada
     */
    public void loadMenuFromFile() {
        daftarPesanan.clear();

        // Cari semua file struk_pesanan_*.txt di folder orders-file
        File currentDir = new File("./orderapp/storage/orders-file/");

        // Buat folder jika belum ada
        if (!currentDir.exists()) {
            currentDir.mkdirs();
        }

        File[] files = currentDir.listFiles((dir, name)
                -> name.startsWith("struk_pesanan_") && name.endsWith(".txt"));

        if (files == null || files.length == 0) {
            System.out.println("Belum ada pesanan tersimpan.");
            return;
        }

        int berhasil = 0;
        int gagal = 0;

        for (File file : files) {
            try {
                Order order = loadOrderFromStruct(file);
                if (order != null) {
                    daftarPesanan.add(order);
                    berhasil++;
                }
            } catch (IOException e) {
                gagal++;
                System.out.println("✗ Gagal memuat " + file.getName() + ": " + e.getMessage());
            }
        }

        System.out.println("✓ Berhasil memuat " + berhasil + " pesanan dari file");
        if (gagal > 0) {
            System.out.println("⚠ " + gagal + " file gagal dimuat");
        }
    }

    /**
     * Load Order dari file struk tertentu
     */
    private Order loadOrderFromStruct(File file) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;

            String customerName = "";
            int orderNumber = 0;
            double totalBayar = 0;
            String namaDiskon = null;
            int totalItem = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Parse nomor pesanan
                if (line.startsWith("No. Pesanan : #")) {
                    String nomor = line.replace("No. Pesanan : #", "").trim();
                    orderNumber = Integer.parseInt(nomor);
                }

                // Parse nama pelanggan
                if (line.startsWith("Pelanggan   :")) {
                    customerName = line.replace("Pelanggan   :", "").trim();
                }

                // Parse total bayar
                if (line.contains("TOTAL:") && line.contains("Rp")) {
                    String[] parts = line.split("Rp");
                    if (parts.length > 1) {
                        String totalStr = parts[parts.length - 1].trim().replace(".", "").replace(",", "");
                        try {
                            totalBayar = Double.parseDouble(totalStr);
                        } catch (NumberFormatException e) {
                            // Ignore parsing errors
                        }
                    }
                }

                // Parse diskon jika ada - FIXED: Ambil nama tanpa persentase
                if (line.contains("DISKON (") && line.contains("%)")) {
                    int start = line.indexOf("DISKON (") + 8;
                    int end = line.indexOf("%)", start);
                    if (start > 7 && end > start) {
                        String diskonPart = line.substring(start, end).trim();
                        // Format: "Member 10" -> ambil "Member" saja
                        // Pisahkan berdasarkan spasi, ambil semua kecuali angka terakhir
                        String[] parts = diskonPart.split("\\s+");
                        if (parts.length >= 2) {
                            // Ambil semua bagian kecuali angka persentase terakhir
                            StringBuilder namaBuilder = new StringBuilder();
                            for (int i = 0; i < parts.length - 1; i++) {
                                if (i > 0) {
                                    namaBuilder.append(" ");
                                }
                                namaBuilder.append(parts[i]);
                            }
                            namaDiskon = namaBuilder.toString().trim();
                        } else if (parts.length == 1) {
                            // Jika hanya ada satu kata, kemungkinan format berbeda
                            // Coba ambil semua sebelum spasi terakhir dengan angka
                            int lastSpaceIdx = diskonPart.lastIndexOf(" ");
                            if (lastSpaceIdx > 0) {
                                namaDiskon = diskonPart.substring(0, lastSpaceIdx).trim();
                            }
                        }
                    }
                }

                // Hitung jumlah item (baris yang berisi data item)
                if (line.matches(".*\\d+\\s+Rp\\s+[0-9.,]+\\s+Rp\\s+[0-9.,]+.*")
                        && !line.contains("Item") && !line.contains("SUBTOTAL")
                        && !line.contains("DISKON") && !line.contains("TOTAL")) {
                    totalItem++;
                }
            }

            // Buat Order dummy untuk ditampilkan di list
            if (!customerName.isEmpty() && orderNumber > 0) {
                OrderSummary summary = new OrderSummary(
                        orderNumber,
                        customerName,
                        totalBayar,
                        namaDiskon,
                        totalItem,
                        file.getName()
                );
                return summary;
            }

            return null;
        }
    }

    /**
     * Menambahkan pesanan ke daftar dan simpan ke file
     */
    public void addOrder(Order order) {
        if (order != null && !order.isEmpty()) {
            daftarPesanan.add(order);
        }
    }

    /**
     * Menampilkan daftar semua pesanan
     */
    public void showOrders() {
        if (daftarPesanan.isEmpty()) {
            System.out.println("\n✗ Belum ada pesanan yang dibuat.");
            return;
        }

        System.out.println("\n" + "=".repeat(85));
        System.out.println("                        DAFTAR SEMUA PESANAN");
        System.out.println("=".repeat(85));
        System.out.printf("%-8s %-25s %-12s %-25s %-15s%n",
                "No.", "Pelanggan", "Total Item", "Total Bayar", "Diskon");
        System.out.println("-".repeat(85));

        for (Order order : daftarPesanan) {
            String diskonInfo = "Tidak ada";
            int totalItem = 0;

            if (order instanceof OrderSummary summary) {
                diskonInfo = summary.getNamaDiskon() != null ? summary.getNamaDiskon() : "Tidak ada";
                totalItem = summary.gettotalItem();
            } else {
                diskonInfo = order.getUsedDiscount() != null
                        ? order.getUsedDiscount().getName()
                        : "Tidak ada";
                totalItem = order.getOrderItem().size();
            }

            System.out.printf("%-8s %-25s %-12d Rp %-22s %-15s%n",
                    "#" + order.getOrderNumber(),
                    order.getCustomerName(),
                    totalItem,
                    formatter.format(order.calculateTotal()),
                    diskonInfo);
        }

        System.out.println("=".repeat(85));
        System.out.println("Total Pesanan: " + daftarPesanan.size());
        System.out.println("=".repeat(85) + "\n");
    }

    /**
     * Menampilkan detail pesanan tertentu dengan membaca dari file struk
     */
    public void showOrderDetail(Scanner scanner) {
        if (daftarPesanan.isEmpty()) {
            System.out.println("\n✗ Belum ada pesanan yang dibuat.");
            return;
        }

        showOrders();

        System.out.print("Masukkan nomor pesanan untuk melihat detail (atau 0 untuk kembali): ");
        try {
            int orderNumber = Integer.parseInt(scanner.nextLine().trim());

            if (orderNumber == 0) {
                return;
            }

            // Baca dan tampilkan file struk dari folder orders-file
            String namaFile = "./orderapp/storage/orders-file/struk_pesanan_" + orderNumber + ".txt";
            File file = new File(namaFile);

            if (!file.exists()) {
                System.out.println("✗ Struk pesanan #" + orderNumber + " tidak ditemukan!");
                return;
            }

            try ( // Tampilkan isi file struk
                    BufferedReader reader = new BufferedReader(new FileReader(file))) {
                String line;
                System.out.println();

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println();
            }

        } catch (NumberFormatException e) {
            System.out.println("✗ Input tidak valid!");
        } catch (IOException e) {
            System.out.println("✗ Error membaca file: " + e.getMessage());
        }
    }

    /**
     * Mencari pesanan berdasarkan nomor
     */
    private Order searchOrder(int orderNumber) {
        for (Order order : daftarPesanan) {
            if (order.getOrderNumber() == orderNumber) {
                return order;
            }
        }
        return null;
    }

    /**
     * Menghitung total pendapatan dari semua pesanan
     */
    public double calculateTotalIncome() {
        double total = 0;
        for (Order order : daftarPesanan) {
            total += order.calculateTotal();
        }
        return total;
    }

    /**
     * Menampilkan statistik pesanan
     */
    public void showStat() {
        if (daftarPesanan.isEmpty()) {
            System.out.println("\n✗ Belum ada pesanan untuk ditampilkan statistiknya.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("                   STATISTIK PESANAN");
        System.out.println("=".repeat(60));
        System.out.println("Total Pesanan       : " + daftarPesanan.size());
        System.out.println("Total Pendapatan    : Rp " + formatter.format(calculateTotalIncome()));

        double rataRata = calculateTotalIncome() / daftarPesanan.size();
        System.out.println("Rata-rata per Order : Rp " + formatter.format(rataRata));
        System.out.println("=".repeat(60) + "\n");
    }

    public ArrayList<Order> getOrders() {
        return daftarPesanan;
    }

    /**
     * Inner class untuk menyimpan summary pesanan yang di-load dari file
     */
    private class OrderSummary extends Order {

        private final int totalItem;
        private final String namaFile;
        private final String namaDiskon;
        private int orderNumber;
        private double totalBayar;

        public OrderSummary(int orderNumber, String customerName, double total,
                String namaDiskon, int totalItem, String namaFile) {
            super(customerName, formatter);
            this.orderNumber = orderNumber;
            this.totalBayar = total;
            this.namaDiskon = namaDiskon;
            this.totalItem = totalItem;
            this.namaFile = namaFile;
        }

        public int gettotalItem() {
            return totalItem;
        }

        public String getNamaFile() {
            return namaFile;
        }

        public String getNamaDiskon() {
            return namaDiskon;
        }

        @Override
        public int getOrderNumber() {
            return orderNumber;
        }

        @Override
        public double calculateTotal() {
            return totalBayar;
        }
    }
}
