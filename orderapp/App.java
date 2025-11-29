package orderapp;

import orderapp.menu.*;
import orderapp.order.Order;
import orderapp.order.OrderManagement;
import java.util.Scanner;
import java.text.NumberFormat;
import java.io.IOException;

public class App {

    private MenuManagement menuManagement;
    private OrderManagement orderManagement;
    private Scanner scanner;
    private NumberFormat formatter;
    private final String MENU_FILE = "orderapp/storage/menu_restoran.txt";

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
        orderManagement.loadMenuFromFile();

        // Loop menu utama (Struktur Pengulangan)
        boolean running = true;
        while (running) {
            try {
                showAllMenu();

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
                        createOrder();
                        break;
                    case 4:
                        showAllOrders();
                        break;
                    case 5:
                        showOrderDetail();
                        break;
                    case 6:
                        showStatOrder();
                        break;
                    case 7:
                        saveMenu();
                        break;
                    case 8:
                        // Simpan menu sebelum keluar
                        saveMenu();
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
    private void showAllMenu() {
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
     * Fitur untuk membuat pesanan
     */
    private void createOrder() {
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
            String customerName = scanner.nextLine().trim();

            while (customerName.isEmpty()) {
                System.out.print("Nama tidak boleh kosong! Masukkan nama: ");
                customerName = scanner.nextLine().trim();
            }

            // Buat objek Order dan proses pesanan
            Order order = new Order(customerName, formatter);
            order.processOrder(scanner, menuManagement);

            // Simpan order ke OrderManagement jika tidak kosong
            if (!order.isEmpty()) {
                orderManagement.addOrder(order);
            }

        } catch (Exception e) {
            System.out.println("✗ Gagal membuat pesanan: " + e.getMessage());
        }
    }

    /**
     * Menampilkan daftar semua pesanan yang pernah dibuat
     */
    private void showAllOrders() {
        orderManagement.showOrders();
    }

    /**
     * Menampilkan detail pesanan tertentu berdasarkan nomor
     */
    private void showOrderDetail() {
        orderManagement.showOrderDetail(scanner);
    }

    /**
     * Menampilkan statistik pesanan (total pesanan, total pendapatan,
     * rata-rata)
     */
    private void showStatOrder() {
        orderManagement.showStat();
    }

    // Fitur untuk menyimpan menu ke file
    private void saveMenu() {
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
