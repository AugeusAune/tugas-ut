package orderapp;

import java.text.NumberFormat;
import java.util.Scanner;

public class App {

    private final MenuManagement menuManagement;
    private final OrderManagement orderManagement;
    private final NumberFormat formatter;
    private final Scanner scanner;

    public App() {
        this.scanner = new Scanner(System.in);
        this.formatter = NumberFormat.getInstance();
        this.menuManagement = new MenuManagement(this.formatter);
        this.orderManagement = new OrderManagement(this.scanner, this.menuManagement);
    }

    public void run() {
        this.menuManagement.prepareMenu();
        boolean running = true;

        while (running) {
            System.out.println("\n===== APLIKASI ORDER RESTO =====");
            System.out.println("1. Menu Management");
            System.out.println("2. Order Management");
            System.out.println("0. Keluar");
            System.out.print("Pilih menu: ");

            int choice = getIntInput();

            switch (choice) {
                case 1 ->
                    this.menuManagement.showMenuManagement(scanner);
                case 2 ->
                    this.orderManagement.showOrderManagement();
                case 0 -> {
                    running = false;
                    System.out.println("Terima kasih! Aplikasi ditutup.");
                }
                default ->
                    System.out.println("Pilihan tidak valid, coba lagi.");
            }
        }
    }

    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }
}
