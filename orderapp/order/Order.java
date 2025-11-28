package orderapp.order;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Scanner;
import orderapp.menu.MenuItem;

public class Order {

    private final Scanner scanner;

    private final ArrayList<OrderItem> items;

    private final ArrayList<MenuItem> menus;

    private OrderReceipt receipt;

    private String customerName;

    public Order(Scanner scanner, ArrayList<MenuItem> menus) {
        this.items = new ArrayList<>();
        this.menus = menus;
        this.scanner = scanner;
    }

    public String getCustomerName() {
        return this.customerName;
    }

    public ArrayList<OrderItem> getOrderItems() {
        return this.items;
    }

    public Scanner getScanner() {
        return this.scanner;
    }

    public void run() {
        this.askOrder();

        this.askCustomerName();

        this.printStruct();
    }

    private void askOrder() {
        if (!this.items.isEmpty()) {
            if (!this.askContinue()) {
                return;
            }
        }

        System.out.println("Masukan Pesanan anda: ");
        String menuName = this.scanner.nextLine().trim();

        Optional<MenuItem> menu = this.searchMenu(menuName);

        if (menu.isEmpty()) {
            System.out.println("Menu tidak ditemukan, silahkan coba isi kembali");
            this.askOrder();
            return;
        }

        this.items.add(new OrderItem(menu.get(), this.askQuantity()));
        this.askOrder();
    }

    private Boolean askContinue() {
        System.out.print("Apakah anda akan tambah pesanan? (y/n): ");
        String askContinue = this.scanner.nextLine().trim();

        if (askContinue.equalsIgnoreCase("n")) {
            return false;
        }

        if (askContinue.equalsIgnoreCase("y")) {
            return true;
        }

        System.out.println("Input tidak valid. Harap masukkan 'y' atau 'n'.");
        return this.askContinue();
    }

    private Integer askQuantity() {
        System.out.println("Masukan Quantity: ");
        String quantity = this.scanner.nextLine().trim();

        try {
            Integer quantityNumber = Integer.valueOf(quantity);

            if (quantityNumber <= 0) {
                System.out.println("Kuantitas harus berupa angka bulat positif (lebih dari 0).");
                return this.askQuantity();
            }

            return quantityNumber;
        } catch (NumberFormatException e) {
            System.out.println("Format Quantity hanya boleh angka");
            return this.askQuantity();
        }
    }

    private Optional<MenuItem> searchMenu(String menuName) {
        for (MenuItem menu : menus) {
            if (menu.getName().equalsIgnoreCase(menuName)) {
                return Optional.of(menu);
            }
        }

        return Optional.empty();
    }

    private void askCustomerName() {
        System.out.println("Masukan nama customer: ");
        this.customerName = this.scanner.nextLine().trim();
    }

    public void printStruct() {
        if (this.receipt != null) {
            this.receipt.print();
            return;
        }
        this.receipt = new OrderReceipt(this);
        this.receipt.print();
    }
}
