package orderapp;

import orderapp.menu.*;
import orderapp.order.Order;
import orderapp.order.OrderManagement;
import java.util.Scanner;
import java.text.NumberFormat;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

/**
 * Kelas utama untuk menjalankan aplikasi Manajemen Restoran Menerapkan semua
 * konsep OOP: Abstraksi, Inheritance, Encapsulation, Polymorphism Exception
 * Handling, File I/O, Struktur Keputusan, dan Pengulangan
 */
public class App {

    private MenuManagement menuManagement;
    private OrderManagement orderManagement;
    private Scanner scanner;
    private NumberFormat formatter;
    private final String MENU_FILE = "menu_restoran.txt";

    public void run() {
        scanner = new Scanner(System.in);
        formatter = NumberFormat.getInstance();
        menuManagement = new MenuManagement(formatter);
        orderManagement = new OrderManagement(formatter);

        // Header aplikasi
        System.out.println("\n" + "=".repeat(60));
        System.out.println("          🍽️  SISTEM MANAJEMEN RESTORAN  🍽️");
        System.out.println("=".repeat(60));

        // Memuat atau membuat menu
        try {
            menuManagement.loadFromFile(MENU_FILE);
            if (menuManagement.getMenus().isEmpty()) {
                System.out.println("Membuat menu default...");
                menuManagement.prepareMenu();
            }
        } catch (IOException e) {
            System.out.println("Membuat menu default...");
            menuManagement.prepareMenu();
        }

        // Memuat pesanan yang tersimpan
        System.out.println("\nMemuat pesanan tersimpan...");
        orderManagement.loadPesananDariFile();

        // Loop menu utama (Struktur Pengulangan)
        boolean running = true;
        while (running) {
            try {
                tampilkanMenuUtama();

                System.out.print("\n➤ Pilih menu (1-8): ");
                int pilihan = getIntInput();

                // Struktur keputusan (switch-case)
                switch (pilihan) {
                    case 1:
                        menuManagement.printMenu();
                        break;
                    case 2:
                        menuManagement.showMenuManagement(scanner);
                        break;
                    case 3:
                        buatPesanan();
                        break;
                    case 4:
                        lihatSemuaPesanan();
                        break;
                    case 5:
                        lihatDetailPesanan();
                        break;
                    case 6:
                        lihatStatistikPesanan();
                        break;
                    case 7:
                        simpanMenu();
                        break;
                    case 8:
                        // Simpan menu sebelum keluar
                        simpanMenu();
                        System.out.println("\n✓ Terima kasih telah menggunakan aplikasi!");
                        System.out.println("Sampai jumpa! 👋\n");
                        running = false;
                        break;
                    default:
                        System.out.println("✗ Pilihan tidak valid! Silakan pilih 1-8.");
                }

            } catch (NumberFormatException e) {
                System.out.println("✗ Input harus berupa angka!");
            } catch (Exception e) {
                System.out.println("✗ Terjadi kesalahan: " + e.getMessage());
            }
        }

        scanner.close();
    }

    // Menampilkan menu utama
    private void tampilkanMenuUtama() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("                    MENU UTAMA RESTORAN");
        System.out.println("=".repeat(60));
        System.out.println("1. Lihat Menu Restoran");
        System.out.println("2. Kelola Menu (Tambah/Edit/Hapus)");
        System.out.println("3. Buat Pesanan Baru");
        System.out.println("4. Lihat Semua Pesanan");
        System.out.println("5. Lihat Detail Pesanan");
        System.out.println("6. Lihat Statistik Pesanan");
        System.out.println("7. Simpan Menu ke File");
        System.out.println("8. Keluar");
        System.out.println("=".repeat(60));
    }

    /**
     * Fitur untuk membuat pesanan dengan mekanisme baru: 1. Input nama
     * pelanggan 2. Loop: Pesan item -> Mau pesan lagi? (Y/N) 3. Mau pakai
     * diskon? (Y/N) 4. Tampilkan dan simpan struk
     */
    private void buatPesanan() {
        try {
            // Cek apakah menu kosong
            if (menuManagement.getMenus().isEmpty()) {
                System.out.println("\n✗ Menu masih kosong! Tambahkan item terlebih dahulu.");
                return;
            }

            System.out.println("\n" + "-".repeat(60));
            System.out.println("                  BUAT PESANAN BARU");
            System.out.println("-".repeat(60));

            // Input nama pelanggan
            System.out.print("Nama pelanggan: ");
            String namaPelanggan = scanner.nextLine().trim();

            while (namaPelanggan.isEmpty()) {
                System.out.print("Nama tidak boleh kosong! Masukkan nama: ");
                namaPelanggan = scanner.nextLine().trim();
            }

            // Buat objek Order dan proses pesanan
            Order order = new Order(namaPelanggan, formatter);
            order.prosesOrder(scanner, menuManagement);

            // Simpan order ke OrderManagement jika tidak kosong
            if (!order.isEmpty()) {
                orderManagement.tambahPesanan(order);
            }

        } catch (Exception e) {
            System.out.println("✗ Gagal membuat pesanan: " + e.getMessage());
        }
    }

    /**
     * Menampilkan daftar semua pesanan yang pernah dibuat
     */
    private void lihatSemuaPesanan() {
        orderManagement.tampilkanDaftarPesanan();
    }

    /**
     * Menampilkan detail pesanan tertentu berdasarkan nomor
     */
    private void lihatDetailPesanan() {
        orderManagement.tampilkanDetailPesanan(scanner);
    }

    /**
     * Menampilkan statistik pesanan (total pesanan, total pendapatan,
     * rata-rata)
     */
    private void lihatStatistikPesanan() {
        orderManagement.tampilkanStatistik();
    }

    // Fitur untuk menyimpan menu ke file
    private void simpanMenu() {
        try {
            menuManagement.saveToFile(MENU_FILE);
        } catch (IOException e) {
            System.out.println("✗ Gagal menyimpan menu: " + e.getMessage());
        }
    }

    // Helper method untuk input integer dengan exception handling
    private int getIntInput() {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Input harus berupa angka! Coba lagi: ");
            }
        }
    }
}
