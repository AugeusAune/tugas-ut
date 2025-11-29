package orderapp.menu;

/**
 * Enum untuk kategori menu Memudahkan pengelolaan kategori dengan type-safe
 */
public enum MenuCategory {
    MAKANAN("Makanan"),
    MINUMAN("Minuman"),
    DISKON("Diskon");

    private final String displayName;

    MenuCategory(String displayName) {
        this.displayName = displayName;
    }

    public String getDisplayName() {
        return displayName;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
