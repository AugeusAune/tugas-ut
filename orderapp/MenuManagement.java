package orderapp;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuManagement {

    private final ArrayList<Menu> menus;
    private final StringBuilder foodMenus;
    private final StringBuilder beverageMenus;
    private final NumberFormat formatter;

    public MenuManagement(NumberFormat formatter) {
        this.menus = new ArrayList<>();
        this.formatter = formatter;
        this.foodMenus = new StringBuilder();
        this.beverageMenus = new StringBuilder();
    }

    public void prepareMenu() {
        this.add("Ayam Bakar", 15_000, MenuCategory.MAKANAN);
        this.add("Es Teh", 5_000, MenuCategory.MINUMAN);
        this.add("Es Jeruk", 7_500, MenuCategory.MINUMAN);
        this.add("Nasi Gudeg", 12_500, MenuCategory.MAKANAN);
    }

    public void showMenuManagement(Scanner scanner) {
        boolean back = false;
        while (!back) {
            System.out.println("\n=== MENU MANAGEMENT ===");
            System.out.println("1. Lihat semua menu");
            System.out.println("2. Tambah menu");
            System.out.println("3. Edit menu");
            System.out.println("4. Hapus menu");
            System.out.println("0. Kembali");
            System.out.print("Pilih: ");

            int choice = getIntInput(scanner);

            switch (choice) {
                case 1 ->
                    this.printMenu();
                case 2 ->
                    this.addMenuViaInput(scanner);
                case 3 ->
                    this.editMenuViaInput(scanner);
                case 4 ->
                    this.deleteMenuViaInput(scanner);
                case 0 ->
                    back = true;
                default ->
                    System.out.println("Pilihan tidak valid.");
            }
        }
    }

    public ArrayList<Menu> getMenus() {
        return this.menus;
    }

    public void addMenuViaInput(Scanner scanner) {
        System.out.print("Masukkan nama menu: ");
        String name = this.getStringInput(scanner);

        System.out.print("Masukkan harga menu: ");
        double price = this.getDoubleInput(scanner);

        MenuCategory category = this.getCategoryInput(scanner);

        this.add(name, price, category);
        System.out.println("Menu \"" + name + "\" berhasil ditambahkan!");
    }

    public void editMenuViaInput(Scanner scanner) {
        System.out.print("Masukkan nama menu yang ingin diubah: ");
        String name = this.getStringInput(scanner);

        System.out.print("Masukkan nama baru: ");
        String newName = this.getStringInput(scanner);

        System.out.print("Masukkan harga baru: ");
        double newPrice = this.getDoubleInput(scanner);

        MenuCategory newCategory = this.getCategoryInput(scanner);

        this.edit(name, newName, newPrice, newCategory);
    }

    public void deleteMenuViaInput(Scanner scanner) {
        System.out.print("Masukkan nama menu yang ingin dihapus: ");
        String name = this.getStringInput(scanner);
        this.delete(name);
    }

    public void printMenu() {
        if (this.beverageMenus.isEmpty() || this.foodMenus.isEmpty()) {
            this.buildStringMenus();
        }

        System.out.println("                     MENU                    ");
        System.out.println("---------------------------------------------");
        System.out.println("                    MAKANAN                  ");
        System.out.println("---------------------------------------------");
        System.out.print(this.foodMenus.toString());

        System.out.println("---------------------------------------------");
        System.out.println("                    MINUMAN                  ");
        System.out.println("---------------------------------------------");
        System.out.print(this.beverageMenus.toString());
    }

    private void add(String name, double price, MenuCategory category) {
        this.menus.add(new Menu(name, price, category));
        this.clearStringMenus();
    }

    private void delete(String name) {
        boolean removed = this.menus.removeIf(menu -> menu.getName().equalsIgnoreCase(name));
        this.clearStringMenus();

        if (removed) {
            System.out.println("Menu \"" + name + "\" berhasil dihapus!");
            return;
        }

        System.out.println("Menu \"" + name + "\" tidak ditemukan.");
    }

    private void edit(String name, String newName, double newPrice, MenuCategory newCategory) {
        for (Menu menu : this.menus) {
            if (menu.getName().equalsIgnoreCase(name)) {
                menu.setName(newName);
                menu.setPrice(newPrice);
                menu.setCategory(newCategory);
                this.clearStringMenus();
                System.out.println("Menu \"" + name + "\" berhasil diperbarui!");
                return;
            }
        }
        System.out.println("Menu \"" + name + "\" tidak ditemukan.");
    }

    private void buildStringMenus() {
        for (Menu menu : this.menus) {
            String displayMenu = String.format("%-28s Rp. %s\n",
                    menu.getName(),
                    this.formatter.format(menu.getPrice()));

            if (menu.getCategory() == MenuCategory.MAKANAN) {
                this.foodMenus.append(displayMenu);
                continue;
            }

            if (menu.getCategory() == MenuCategory.MINUMAN) {
                this.beverageMenus.append(displayMenu);
            }
        }
    }

    private void clearStringMenus() {
        this.beverageMenus.setLength(0);
        this.foodMenus.setLength(0);
    }

    private MenuCategory getCategoryInput(Scanner scanner) {
        while (true) {
            System.out.println("Pilih kategori:");
            System.out.println("1. Makanan");
            System.out.println("2. Minuman");
            System.out.print("Pilih: ");

            int choice = getIntInput(scanner);

            MenuCategory category = switch (choice) {
                case 1 ->
                    MenuCategory.MAKANAN;
                case 2 ->
                    MenuCategory.MINUMAN;
                default ->
                    null;
            };

            if (category != null) {
                return category;
            }

            System.out.println("Kategori tidak valid! Coba lagi.\n");
        }
    }

    private String getStringInput(Scanner scanner) {
        String input = scanner.nextLine().trim();
        while (input.isEmpty()) {
            System.out.println("Input tidak boleh kosong! Coba lagi: ");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input harus berupa angka! Coba lagi: ");
            }
        }
    }

    private double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Input harga tidak valid! Coba lagi: ");
            }
        }
    }
}
