package orderapp.order;

import orderapp.menu.MenuItem;


public class OrderItem {

    private final MenuItem menu;

    private final Integer quantity;

    public OrderItem(MenuItem menu, Integer quantity) {
        this.menu = menu;
        this.quantity = quantity;
    }

    public MenuItem getMenu() {
        return this.menu;
    }

    public Integer getQuantity() {
        return this.quantity;
    }
}
