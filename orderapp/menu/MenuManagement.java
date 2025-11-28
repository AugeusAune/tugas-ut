package orderapp.menu;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;

public class MenuManagement {

    private final ArrayList<MenuItem> menus;
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
        this.add("Ayam Bakar", 15_000, "bakaran", MenuCategory.MAKANAN);
        this.add("Ayam Penyet", 20_000, "default", MenuCategory.MAKANAN);
        this.add("Nasi Gudeg", 12_500, "default", MenuCategory.MAKANAN);
        this.add("Nasi Ayam", 13_500, "default", MenuCategory.MAKANAN);
        this.add("Es Teh", 5_000, "default", MenuCategory.MINUMAN);
        this.add("Es Jeruk", 7_500, "default", MenuCategory.MINUMAN);
        this.add("Pop Ice", 4_500, "default", MenuCategory.MINUMAN);
        this.add("Air Putih", 3_500, "default", MenuCategory.MINUMAN);
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

    public ArrayList<MenuItem> getMenus() {
        return this.menus;
    }

    public void addMenuViaInput(Scanner scanner) {
        System.out.print("Masukkan nama menu: ");
        String name = this.getStringInput(scanner);

        System.out.print("Masukkan harga menu: ");
        double price = this.getDoubleInput(scanner);

        MenuCategory category = this.getCategoryInput(scanner);

        System.out.printf("Masukkan jenis %s: ", MenuCategory.MAKANAN == category ? "Makanan" : "Minuman");
        String type = this.getStringInput(scanner);

        this.add(name, price, type, category);
        System.out.println("MenuItem \"" + name + "\" berhasil ditambahkan!");
    }

    public void editMenuViaInput(Scanner scanner) {
        if (menus.isEmpty()) {
            System.out.println("Tidak ada menu tersedia.");
            return;
        }

        System.out.println("\n=== DAFTAR MENU ===");
        for (int i = 0; i < menus.size(); i++) {
            MenuItem menu = menus.get(i);
            System.out.printf("%d. %s - Rp. %s (%s)\n",
                    i + 1,
                    menu.getName(),
                    formatter.format(menu.getPrice()),
                    menu.getCategory());
        }

        System.out.print("\nMasukkan nomor menu yang ingin diubah: ");
        int menuNumber = getMenuNumberInput(scanner, menus.size());

        if (menuNumber == -1) {
            return;
        }

        MenuItem selectedMenu = menus.get(menuNumber - 1);

        System.out.print("Masukkan nama baru: ");
        String newName = getStringInput(scanner);

        System.out.print("Masukkan harga baru: ");
        double newPrice = getDoubleInput(scanner);

        String type = this.getStringInput(scanner);

        MenuCategory newCategory = getCategoryInput(scanner);

        System.out.println("\n=== KONFIRMASI PERUBAHAN ===");
        System.out.printf("Dari: %s - Rp. %s (%s)\n", selectedMenu.getName(), formatter.format(selectedMenu.getPrice()), selectedMenu.getCategory());
        System.out.printf("Menjadi: %s - Rp. %s (%s)\n", newName, formatter.format(newPrice), newCategory);

        System.out.print("\nApakah Anda yakin? (Ya/Tidak): ");

        String confirmation = getConfirmationInput(scanner);

        if (confirmation.equalsIgnoreCase("Ya")) {
            selectedMenu.setName(newName);
            selectedMenu.setPrice(newPrice);
            selectedMenu.setType(type);
            clearStringMenus();
            System.out.println("\nMenu berhasil diperbarui!");
        } else {
            System.out.println("\nPerubahan dibatalkan.");
        }
    }

    public void deleteMenuViaInput(Scanner scanner) {
        if (menus.isEmpty()) {
            System.out.println("Tidak ada menu tersedia.");
            return;
        }

        System.out.println("\n=== DAFTAR MENU ===");
        for (int i = 0; i < menus.size(); i++) {
            MenuItem menu = menus.get(i);
            System.out.printf("%d. %s - Rp. %s (%s)\n",
                    i + 1,
                    menu.getName(),
                    formatter.format(menu.getPrice()),
                    menu.getCategory());
        }

        System.out.print("\nMasukkan nomor menu yang ingin dihapus: ");
        int menuNumber = getMenuNumberInput(scanner, menus.size());

        if (menuNumber == -1) {
            return;
        }

        MenuItem selectedMenu = menus.get(menuNumber - 1);

        System.out.println("\n=== KONFIRMASI PENGHAPUSAN ===");
        System.out.printf("MenuItem yang akan dihapus: %s - Rp. %s (%s)\n", selectedMenu.getName(), formatter.format(selectedMenu.getPrice()), selectedMenu.getCategory());
        System.out.print("\nApakah Anda yakin? (Ya/Tidak): ");

        String confirmation = getConfirmationInput(scanner);

        if (confirmation.equalsIgnoreCase("Ya")) {
            String menuName = selectedMenu.getName();
            menus.remove(menuNumber - 1);
            clearStringMenus();
            System.out.println("\nMenu \"" + menuName + "\" berhasil dihapus!");
        } else {
            System.out.println("\nPenghapusan dibatalkan.");
        }
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

    public void saveToFile(String filename) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (MenuItem it : this.menus) {
                String line = "";

                if (it instanceof Makanan m) {
                    line = String.format("M;%s;%.2f;%s;%s", m.getName(), m.getPrice(), m.getCategory(), m.getType());
                }

                if (it instanceof Minuman n) {
                    line = String.format("N;%s;%.2f;%s;%s", n.getName(), n.getPrice(), n.getCategory(), n.getType());
                }

                if (it instanceof Diskon d) {
                    line = String.format("D;%s;%.2f;%s;%.2f", d.getName(), d.getPrice(), d.getCategory(), d.getDiskonPercent());
                }

                bw.write(line);
                bw.newLine();
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        this.menus.clear();

        File f = new File(filename);
        if (!f.exists()) {
            return;
        }
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                if (line.trim().isEmpty()) {
                    continue;
                }
                String[] parts = line.split(";");
                if (parts.length < 5) {
                    continue;
                }

                String tipe = parts[0];
                String nama = parts[1];
                double harga = Double.parseDouble(parts[2]);
                String type = parts[3];
                String extra = parts[4];

                switch (tipe) {
                    case "M" ->
                        this.menus.add(new Makanan(nama, harga, type));
                    case "N" ->
                        this.menus.add(new Minuman(nama, harga, type));
                    case "D" -> {
                        double pct = Double.parseDouble(extra);
                        this.menus.add(new Diskon(nama, harga, type, pct));
                    }
                    default -> {
                    }
                }
            }
        }
    }

    private void add(String name, double price, String type, MenuCategory category) {
        if (category == MenuCategory.MAKANAN) {
            this.menus.add(new Makanan(name, price, type));
        }

        if (category == MenuCategory.MINUMAN) {
            this.menus.add(new Minuman(name, price, type));
        }

        this.clearStringMenus();
    }

    private void buildStringMenus() {
        for (MenuItem menu : this.menus) {
            String displayMenu = String.format("%-28s Rp. %s\n",
                    menu.getName(),
                    this.formatter.format(menu.getPrice()));

            if ("Makanan".equals(menu.getCategory())) {
                this.foodMenus.append(displayMenu);
                continue;
            }

            if ("Minuman".equals(menu.getCategory())) {
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
            System.out.print("Input tidak boleh kosong! Coba lagi: ");
            input = scanner.nextLine().trim();
        }
        return input;
    }

    private int getIntInput(Scanner scanner) {
        while (true) {
            try {
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Input harus berupa angka! Coba lagi: ");
            }
        }
    }

    private double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                return Double.parseDouble(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.print("Input harga tidak valid! Coba lagi: ");
            }
        }
    }

    private int getMenuNumberInput(Scanner scanner, int maxNumber) {
        while (true) {
            int number = getIntInput(scanner);

            if (number >= 1 && number <= maxNumber) {
                return number;
            }

            System.out.print("Nomor menu tidak valid! Coba lagi (1-" + maxNumber + "): ");
        }
    }

    private String getConfirmationInput(Scanner scanner) {
        while (true) {
            String input = scanner.nextLine().trim();

            if (input.equalsIgnoreCase("Ya") || input.equalsIgnoreCase("Tidak")) {
                return input;
            }

            System.out.print("Input tidak valid! Masukkan 'Ya' atau 'Tidak': ");
        }
    }
}
