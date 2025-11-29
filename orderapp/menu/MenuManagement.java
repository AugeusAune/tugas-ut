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

/**
 * Kelas MenuManagement untuk mengelola semua menu restoran
 */
public class MenuManagement {

    private final ArrayList<MenuItem> menus;
    private final StringBuilder foodMenus;
    private final StringBuilder beverageMenus;
    private final StringBuilder discountMenus;
    private final NumberFormat formatter;

    public MenuManagement(NumberFormat formatter) {
        this.menus = new ArrayList<>();
        this.formatter = formatter;
        this.foodMenus = new StringBuilder();
        this.beverageMenus = new StringBuilder();
        this.discountMenus = new StringBuilder();
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

        // Tambah diskon default
        this.addDiscount("Member 10%", 10);
        this.addDiscount("Promo Spesial", 15);
    }

    public void showMenuManagement(Scanner scanner) {
        boolean back = false;
        while (!back) {
            try {
                System.out.println("\n=== MENU MANAGEMENT ===");
                System.out.println("1. Lihat semua menu");
                System.out.println("2. Tambah menu");
                System.out.println("3. Edit menu");
                System.out.println("4. Hapus menu");
                System.out.println("5. Tambah diskon");
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
                    case 5 ->
                        this.addDiscountViaInput(scanner);
                    case 0 ->
                        back = true;
                    default ->
                        System.out.println("✗ Pilihan tidak valid.");
                }
            } catch (Exception e) {
                System.out.println("✗ Terjadi kesalahan: " + e.getMessage());
            }
        }
    }

    public ArrayList<MenuItem> getMenus() {
        return this.menus;
    }

    public void addMenuViaInput(Scanner scanner) {
        try {
            System.out.print("Masukkan nama menu: ");
            String name = this.getStringInput(scanner);

            System.out.print("Masukkan harga menu: ");
            double price = this.getDoubleInput(scanner);

            if (price < 0) {
                throw new IllegalArgumentException("Harga tidak boleh negatif!");
            }

            MenuCategory category = this.getCategoryInput(scanner);

            System.out.printf("Masukkan jenis %s: ", MenuCategory.MAKANAN == category ? "Makanan" : "Minuman");
            String type = this.getStringInput(scanner);

            this.add(name, price, type, category);
            System.out.println("✓ Menu \"" + name + "\" berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Gagal menambahkan menu: " + e.getMessage());
        }
    }

    public void addDiscountViaInput(Scanner scanner) {
        try {
            System.out.print("Masukkan nama diskon: ");
            String name = this.getStringInput(scanner);

            System.out.print("Masukkan persentase diskon (%): ");
            double persen = this.getDoubleInput(scanner);

            if (persen < 0 || persen > 100) {
                throw new IllegalArgumentException("Persentase diskon harus antara 0-100!");
            }

            this.addDiscount(name, persen);
            System.out.println("✓ Diskon \"" + name + "\" berhasil ditambahkan!");
        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (Exception e) {
            System.out.println("✗ Gagal menambahkan diskon: " + e.getMessage());
        }
    }

    public void editMenuViaInput(Scanner scanner) {
        try {
            if (menus.isEmpty()) {
                System.out.println("✗ Tidak ada menu tersedia.");
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

            if (selectedMenu == null) {
                System.err.println("Menu yang dipilih tidak ada");
            }

            if (selectedMenu instanceof Diskon) {
                System.out.println("✗ Edit diskon belum didukung. Silakan hapus dan tambah ulang.");
                return;
            }

            System.out.print("Masukkan nama baru: ");
            String newName = getStringInput(scanner);

            System.out.print("Masukkan harga baru: ");
            double newPrice = getDoubleInput(scanner);

            if (newPrice < 0) {
                throw new IllegalArgumentException("Harga tidak boleh negatif!");
            }

            System.out.print("Masukkan jenis baru: ");
            String newType = this.getStringInput(scanner);

            System.out.println("\n=== KONFIRMASI PERUBAHAN ===");
            System.out.printf("Dari: %s - Rp. %s (%s)\n",
                    selectedMenu.getName(),
                    formatter.format(selectedMenu.getPrice()),
                    selectedMenu.getCategory());
            System.out.printf("Menjadi: %s - Rp. %s\n",
                    newName,
                    formatter.format(newPrice));

            System.out.print("\nApakah Anda yakin? (Ya/Tidak): ");
            String confirmation = getConfirmationInput(scanner);

            if (confirmation.equalsIgnoreCase("Ya")) {
                selectedMenu.setName(newName);
                selectedMenu.setPrice(newPrice);

                // Update type untuk Makanan atau Minuman
                switch (selectedMenu) {
                    case Makanan makanan ->
                        makanan.setType(newType);
                    case Minuman minuman ->
                        minuman.setType(newType);
                    default -> {
                    }
                }

                clearStringMenus();
                System.out.println("\n✓ Menu berhasil diperbarui!");
            } else {
                System.out.println("\n✓ Perubahan dibatalkan.");
            }
        } catch (IllegalArgumentException e) {
            System.out.println("✗ " + e.getMessage());
        } catch (IndexOutOfBoundsException e) {
            System.out.println("✗ Nomor menu tidak valid!");
        } catch (Exception e) {
            System.out.println("✗ Gagal mengedit menu: " + e.getMessage());
        }
    }

    public void deleteMenuViaInput(Scanner scanner) {
        try {
            if (menus.isEmpty()) {
                System.out.println("✗ Tidak ada menu tersedia.");
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
            System.out.printf("Menu yang akan dihapus: %s - Rp. %s (%s)\n",
                    selectedMenu.getName(),
                    formatter.format(selectedMenu.getPrice()),
                    selectedMenu.getCategory());
            System.out.print("\nApakah Anda yakin? (Ya/Tidak): ");

            String confirmation = getConfirmationInput(scanner);

            if (confirmation.equalsIgnoreCase("Ya")) {
                String menuName = selectedMenu.getName();
                menus.remove(menuNumber - 1);
                clearStringMenus();
                System.out.println("\n✓ Menu \"" + menuName + "\" berhasil dihapus!");
            } else {
                System.out.println("\n✓ Penghapusan dibatalkan.");
            }
        } catch (IndexOutOfBoundsException e) {
            System.out.println("✗ Nomor menu tidak valid!");
        } catch (Exception e) {
            System.out.println("✗ Gagal menghapus menu: " + e.getMessage());
        }
    }

    public void printMenu() {
        try {
            if (menus.isEmpty()) {
                System.out.println("\n✗ Menu masih kosong!");
                return;
            }

            if (this.beverageMenus.isEmpty() && this.foodMenus.isEmpty()) {
                this.buildStringMenus();
            }

            System.out.println("\n" + "=".repeat(50));
            System.out.println("                     MENU RESTORAN");
            System.out.println("=".repeat(50));

            if (this.foodMenus.length() > 0) {
                System.out.println("\n                    MAKANAN");
                System.out.println("-".repeat(50));
                System.out.print(this.foodMenus.toString());
            }

            if (this.beverageMenus.length() > 0) {
                System.out.println("\n                    MINUMAN");
                System.out.println("-".repeat(50));
                System.out.print(this.beverageMenus.toString());
            }

            if (this.discountMenus.length() > 0) {
                System.out.println("\n                    DISKON");
                System.out.println("-".repeat(50));
                System.out.print(this.discountMenus.toString());
            }

            System.out.println("=".repeat(50));
        } catch (Exception e) {
            System.out.println("✗ Gagal menampilkan menu: " + e.getMessage());
        }
    }

    /**
     * Print menu tanpa menampilkan diskon (untuk proses order)
     */
    public void printMenuWithoutDiscount() {
        try {
            if (menus.isEmpty()) {
                System.out.println("\n✗ Menu masih kosong!");
                return;
            }

            // Build menu strings tanpa diskon
            StringBuilder tempFoodMenus = new StringBuilder();
            StringBuilder tempBeverageMenus = new StringBuilder();

            for (MenuItem menu : this.menus) {
                if ("Makanan".equals(menu.getCategory())) {
                    String displayMenu = String.format("%-28s Rp. %s\n",
                            menu.getName(),
                            this.formatter.format(menu.getPrice()));
                    tempFoodMenus.append(displayMenu);
                } else if ("Minuman".equals(menu.getCategory())) {
                    String displayMenu = String.format("%-28s Rp. %s\n",
                            menu.getName(),
                            this.formatter.format(menu.getPrice()));
                    tempBeverageMenus.append(displayMenu);
                }
            }

            System.out.println("\n" + "=".repeat(50));
            System.out.println("                     MENU RESTORAN");
            System.out.println("=".repeat(50));

            if (tempFoodMenus.length() > 0) {
                System.out.println("\n                    MAKANAN");
                System.out.println("-".repeat(50));
                System.out.print(tempFoodMenus.toString());
            }

            if (tempBeverageMenus.length() > 0) {
                System.out.println("\n                    MINUMAN");
                System.out.println("-".repeat(50));
                System.out.print(tempBeverageMenus.toString());
            }

            System.out.println("=".repeat(50));
        } catch (Exception e) {
            System.out.println("✗ Gagal menampilkan menu: " + e.getMessage());
        }
    }

    public void saveToFile(String filename) throws IOException {
        BufferedWriter bw = null;
        try {
            bw = new BufferedWriter(new FileWriter(filename));
            for (MenuItem it : this.menus) {
                bw.write(it.toFileString());
                bw.newLine();
            }
            System.out.println("✓ Menu berhasil disimpan ke file: " + filename);
        } catch (IOException e) {
            System.out.println("✗ Error menyimpan menu: " + e.getMessage());
            throw e;
        } finally {
            if (bw != null) {
                try {
                    bw.close();
                } catch (IOException e) {
                    System.out.println("✗ Error menutup file: " + e.getMessage());
                }
            }
        }
    }

    public void loadFromFile(String filename) throws IOException {
        this.menus.clear();

        File f = new File(filename);
        if (!f.exists()) {
            System.out.println("File menu belum ada. Menggunakan menu default.");
            return;
        }

        BufferedReader br = null;
        try {
            br = new BufferedReader(new FileReader(f));
            String line;
            int count = 0;
            int errorCount = 0;

            while ((line = br.readLine()) != null) {
                try {
                    if (line.trim().isEmpty()) {
                        continue;
                    }

                    String[] parts = line.split(";");
                    if (parts.length < 3) {
                        errorCount++;
                        continue;
                    }

                    String tipe = parts[0];
                    String nama = parts[1];
                    double price = Double.parseDouble(parts[2]);

                    switch (tipe) {
                        case "M" -> {
                            if (parts.length >= 4) {
                                String type = parts[3];
                                this.menus.add(new Makanan(nama, price, type));
                                count++;
                            }
                        }
                        case "N" -> {
                            if (parts.length >= 4) {
                                String type = parts[3];
                                this.menus.add(new Minuman(nama, price, type));
                                count++;
                            }
                        }
                        case "D" -> {
                            this.menus.add(new Diskon(nama, price));
                            count++;
                        }
                        default ->
                            errorCount++;
                    }
                } catch (NumberFormatException e) {
                    errorCount++;
                    System.out.println("✗ Error format data pada baris: " + line);
                } catch (Exception e) {
                    errorCount++;
                    System.out.println("✗ Error memproses baris: " + line);
                }
            }

            System.out.println("✓ Berhasil memuat " + count + " item dari file");
            if (errorCount > 0) {
                System.out.println("⚠ " + errorCount + " baris gagal dimuat");
            }
        } catch (IOException e) {
            System.out.println("✗ Error membaca file: " + e.getMessage());
            throw e;
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException e) {
                    System.out.println("✗ Error menutup file: " + e.getMessage());
                }
            }
        }

        clearStringMenus();
    }

    public MenuItem searchMenu(String nama) throws Exception {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama menu tidak boleh kosong!");
        }

        for (MenuItem item : menus) {
            if (item.getName().equalsIgnoreCase(nama.trim())) {
                return item;
            }
        }
        throw new Exception("Menu '" + nama + "' tidak ditemukan!");
    }

    public Diskon searchDiscount(String nama) throws Exception {
        if (nama == null || nama.trim().isEmpty()) {
            throw new IllegalArgumentException("Nama diskon tidak boleh kosong!");
        }

        for (MenuItem item : menus) {
            if (item instanceof Diskon && item.getName().equalsIgnoreCase(nama.trim())) {
                return (Diskon) item;
            }
        }
        throw new Exception("Diskon '" + nama + "' tidak ditemukan!");
    }

    public ArrayList<Diskon> getDiscounts() {
        ArrayList<Diskon> daftarDiskon = new ArrayList<>();
        for (MenuItem item : menus) {
            if (item instanceof Diskon diskon) {
                daftarDiskon.add(diskon);
            }
        }
        return daftarDiskon;
    }

    private void add(String name, double price, String type, MenuCategory category) {
        if (category == MenuCategory.MAKANAN) {
            this.menus.add(new Makanan(name, price, type));
        } else if (category == MenuCategory.MINUMAN) {
            this.menus.add(new Minuman(name, price, type));
        }
        this.clearStringMenus();
    }

    private void addDiscount(String name, double persenDiskon) {
        this.menus.add(new Diskon(name, persenDiskon));
        this.clearStringMenus();
    }

    @SuppressWarnings("ConvertToStringSwitch")
    private void buildStringMenus() {
        for (MenuItem menu : this.menus) {
            if (menu.getCategory().equals("Makanan")) {
                String displayMenu = String.format("%-28s Rp. %s\n",
                        menu.getName(),
                        this.formatter.format(menu.getPrice()));
                this.foodMenus.append(displayMenu);
                continue;

            }

            if (menu.getCategory().equals("Minuman")) {
                String displayMenu = String.format("%-28s Rp. %s\n",
                        menu.getName(),
                        this.formatter.format(menu.getPrice()));
                this.beverageMenus.append(displayMenu);
                continue;
            }

            if (menu.getCategory().equals("Diskon")) {
                Diskon d = (Diskon) menu;
                String diskonDisplay = String.format("%-28s %.0f%% off\n",
                        d.getName(), d.getPersenDiskon());
                this.discountMenus.append(diskonDisplay);
            }
        }
    }

    private void clearStringMenus() {
        this.beverageMenus.setLength(0);
        this.foodMenus.setLength(0);
        this.discountMenus.setLength(0);
    }

    private MenuCategory getCategoryInput(Scanner scanner) {
        while (true) {
            try {
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

                System.out.println("✗ Kategori tidak valid! Coba lagi.\n");
            } catch (Exception e) {
                System.out.println("✗ Error: " + e.getMessage());
            }
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
                String input = scanner.nextLine().trim();
                return Integer.parseInt(input);
            } catch (NumberFormatException e) {
                System.out.print("Input harus berupa angka! Coba lagi: ");
            }
        }
    }

    private double getDoubleInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();
                return Double.parseDouble(input);
            } catch (NumberFormatException e) {
                System.out.print("Input harga tidak valid! Coba lagi: ");
            }
        }
    }

    private int getMenuNumberInput(Scanner scanner, int maxNumber) {
        while (true) {
            try {
                int number = getIntInput(scanner);

                if (number >= 1 && number <= maxNumber) {
                    return number;
                }

                System.out.print("Nomor menu tidak valid! Coba lagi (1-" + maxNumber + "): ");
            } catch (Exception e) {
                System.out.print("Input tidak valid! Coba lagi: ");
            }
        }
    }

    private String getConfirmationInput(Scanner scanner) {
        while (true) {
            try {
                String input = scanner.nextLine().trim();

                if (input.equalsIgnoreCase("Ya") || input.equalsIgnoreCase("Tidak")) {
                    return input;
                }

                System.out.print("Input tidak valid! Masukkan 'Ya' atau 'Tidak': ");
            } catch (Exception e) {
                System.out.print("Input tidak valid! Coba lagi: ");
            }
        }
    }
}
