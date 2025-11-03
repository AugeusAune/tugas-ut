package orderapp;

public class OrderItem {

    private final Menu menu;

    private final Integer quantity;

    public OrderItem(Menu menu, Integer quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Integer getQuantity() {
        return this.quantity;
    }
}
