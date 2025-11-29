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
    public void loadPesananDariFile() {
        daftarPesanan.clear();

        // Cari semua file struk_pesanan_*.txt
        File currentDir = new File(".");
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
                Order order = loadOrderDariStruk(file);
                if (order != null) {
                    daftarPesanan.add(order);
                    berhasil++;
                }
            } catch (Exception e) {
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
    private Order loadOrderDariStruk(File file) throws IOException {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            String line;

            String namaPelanggan = "";
            int nomorPesanan = 0;
            double totalBayar = 0;
            String namaDiskon = null;
            int jumlahItem = 0;

            while ((line = reader.readLine()) != null) {
                line = line.trim();

                // Parse nomor pesanan
                if (line.startsWith("No. Pesanan : #")) {
                    String nomor = line.replace("No. Pesanan : #", "").trim();
                    nomorPesanan = Integer.parseInt(nomor);
                }

                // Parse nama pelanggan
                if (line.startsWith("Pelanggan   :")) {
                    namaPelanggan = line.replace("Pelanggan   :", "").trim();
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

                // Parse diskon jika ada
                if (line.contains("DISKON (") && line.contains("%)")) {
                    int start = line.indexOf("DISKON (") + 8;
                    int end = line.indexOf("%)", start);
                    if (start > 7 && end > start) {
                        String diskonPart = line.substring(start, end).trim();
                        // Ambil nama diskon saja (tanpa persentase)
                        int lastSpace = diskonPart.lastIndexOf(" ");
                        if (lastSpace > 0) {
                            namaDiskon = diskonPart.substring(0, lastSpace).trim();
                        }
                    }
                }

                // Hitung jumlah item (baris yang berisi data item)
                if (line.matches(".*\\d+\\s+Rp\\s+[0-9.,]+\\s+Rp\\s+[0-9.,]+.*")
                        && !line.contains("Item") && !line.contains("SUBTOTAL")
                        && !line.contains("DISKON") && !line.contains("TOTAL")) {
                    jumlahItem++;
                }
            }

            // Buat Order dummy untuk ditampilkan di list
            if (!namaPelanggan.isEmpty() && nomorPesanan > 0) {
                OrderSummary summary = new OrderSummary(
                        nomorPesanan,
                        namaPelanggan,
                        totalBayar,
                        namaDiskon,
                        jumlahItem,
                        file.getName()
                );
                return summary;
            }

            return null;
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }

    /**
     * Menambahkan pesanan ke daftar dan simpan ke file
     */
    public void tambahPesanan(Order order) {
        if (order != null && !order.isEmpty()) {
            daftarPesanan.add(order);
        }
    }

    /**
     * Menampilkan daftar semua pesanan
     */
    public void tampilkanDaftarPesanan() {
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

            if (order instanceof OrderSummary) {
                OrderSummary summary = (OrderSummary) order;
                diskonInfo = summary.getNamaDiskon() != null ? summary.getNamaDiskon() : "Tidak ada";
                totalItem = summary.getJumlahItem();
            } else {
                diskonInfo = order.getDiskonDipakai() != null
                        ? order.getDiskonDipakai().getName()
                        : "Tidak ada";
                totalItem = order.getItemPesanan().size();
            }

            System.out.printf("%-8s %-25s %-12d Rp %-22s %-15s%n",
                    "#" + order.getNomorPesanan(),
                    order.getNamaPelanggan(),
                    totalItem,
                    formatter.format(order.hitungTotal()),
                    diskonInfo);
        }

        System.out.println("=".repeat(85));
        System.out.println("Total Pesanan: " + daftarPesanan.size());
        System.out.println("=".repeat(85) + "\n");
    }

    /**
     * Menampilkan detail pesanan tertentu dengan membaca dari file struk
     */
    public void tampilkanDetailPesanan(Scanner scanner) {
        if (daftarPesanan.isEmpty()) {
            System.out.println("\n✗ Belum ada pesanan yang dibuat.");
            return;
        }

        tampilkanDaftarPesanan();

        System.out.print("Masukkan nomor pesanan untuk melihat detail (atau 0 untuk kembali): ");
        try {
            int nomorPesanan = Integer.parseInt(scanner.nextLine().trim());

            if (nomorPesanan == 0) {
                return;
            }

            // Baca dan tampilkan file struk
            String namaFile = "struk_pesanan_" + nomorPesanan + ".txt";
            File file = new File(namaFile);

            if (!file.exists()) {
                System.out.println("✗ Struk pesanan #" + nomorPesanan + " tidak ditemukan!");
                return;
            }

            // Tampilkan isi file struk
            BufferedReader reader = null;
            try {
                reader = new BufferedReader(new FileReader(file));
                String line;
                System.out.println();

                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                System.out.println();
            } finally {
                if (reader != null) {
                    reader.close();
                }
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
    private Order cariPesanan(int nomorPesanan) {
        for (Order order : daftarPesanan) {
            if (order.getNomorPesanan() == nomorPesanan) {
                return order;
            }
        }
        return null;
    }

    /**
     * Menghitung total pendapatan dari semua pesanan
     */
    public double hitungTotalPendapatan() {
        double total = 0;
        for (Order order : daftarPesanan) {
            total += order.hitungTotal();
        }
        return total;
    }

    /**
     * Menampilkan statistik pesanan
     */
    public void tampilkanStatistik() {
        if (daftarPesanan.isEmpty()) {
            System.out.println("\n✗ Belum ada pesanan untuk ditampilkan statistiknya.");
            return;
        }

        System.out.println("\n" + "=".repeat(60));
        System.out.println("                   STATISTIK PESANAN");
        System.out.println("=".repeat(60));
        System.out.println("Total Pesanan       : " + daftarPesanan.size());
        System.out.println("Total Pendapatan    : Rp " + formatter.format(hitungTotalPendapatan()));

        double rataRata = hitungTotalPendapatan() / daftarPesanan.size();
        System.out.println("Rata-rata per Order : Rp " + formatter.format(rataRata));
        System.out.println("=".repeat(60) + "\n");
    }

    public ArrayList<Order> getDaftarPesanan() {
        return daftarPesanan;
    }

    /**
     * Inner class untuk menyimpan summary pesanan yang di-load dari file
     */
    private class OrderSummary extends Order {

        private final int jumlahItem;
        private final String namaFile;
        private final String namaDiskon;

        public OrderSummary(int nomorPesanan, String namaPelanggan, double total,
                String namaDiskon, int jumlahItem, String namaFile) {
            super(namaPelanggan, formatter);
            this.nomorPesanan = nomorPesanan;
            this.totalBayar = total;
            this.namaDiskon = namaDiskon;
            this.jumlahItem = jumlahItem;
            this.namaFile = namaFile;
        }

        public int getJumlahItem() {
            return jumlahItem;
        }

        public String getNamaFile() {
            return namaFile;
        }

        public String getNamaDiskon() {
            return namaDiskon;
        }

        @Override
        public double hitungTotal() {
            return totalBayar;
        }

        private int nomorPesanan;
        private double totalBayar;
    }
}
