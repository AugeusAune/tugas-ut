package orderapp;

import java.util.ArrayList;
import java.util.Scanner;

public class OrderManagement {

    private final MenuManagement menuManagement;
    private final ArrayList<Order> orders;
    private final Scanner scanner;

    public OrderManagement(Scanner scanner, MenuManagement menuManagement) {
        this.menuManagement = menuManagement;
        this.scanner = scanner;
        this.orders = new ArrayList<>();
    }

    public void showOrderManagement() {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== ORDER MANAGEMENT ===");
            System.out.println("1. Buat Order Baru");
            System.out.println("2. Lihat Semua Order");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            int choice = this.getIntInput();

            switch (choice) {
                case 1 ->
                    this.runNewOrder();
                case 2 ->
                    this.printAllOrders();
                case 0 ->
                    back = true;
                default ->
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    private void runNewOrder() {
        System.out.println("=============================================");
        System.out.println("                MULAI ORDER BARU             ");
        System.out.println("=============================================");

        this.menuManagement.printMenu();

        Order order = new Order(this.scanner, this.menuManagement.getMenus());
        order.run();

        this.orders.add(order);
        System.out.println("Order berhasil dibuat!");
    }

    private void printAllOrders() {
        if (this.orders.isEmpty()) {
            System.out.println("Belum ada order yang dibuat.");
            return;
        }

        System.out.println("=============================================");
        System.out.println("                DAFTAR SEMUA ORDER           ");
        System.out.println("=============================================");

        int index = 1;
        for (Order order : this.orders) {
            System.out.printf("Order #%d:\n", index++);
            order.printStruct();
            System.out.println("---------------------------------------------");
        }

        System.out.printf("Total order: %d\n", this.orders.size());
    }

    private int getIntInput() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Input harus berupa angka!");
            return -1;
        }
    }
}
