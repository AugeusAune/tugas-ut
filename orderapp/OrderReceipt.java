package orderapp;

import java.text.NumberFormat;

public class OrderReceipt {

    private static final double VAT_RATE = 0.10;
    private static final double SERVICE_FEE = 20_000.0;
    private static final double MIN_PURCHASE_FOR_BOGO = 50_000.0;
    private static final double MIN_PURCHASE_FOR_DISCOUNT = 100_000.0;
    private static final double DISCOUNT_RATE = 0.10;

    private final Order order;
    private final NumberFormat formatter;
    private final StringBuilder foodDetails;
    private final StringBuilder beverageDetails;

    private double calculatedSubTotal;
    private double calculatedVat;
    private double grandTotal;

    public OrderReceipt(Order order) {
        this.order = order;
        this.formatter = NumberFormat.getInstance();
        this.foodDetails = new StringBuilder();
        this.beverageDetails = new StringBuilder();
    }

    public void print() {
        reset();

        buildDetailReceipt();

        double bogoDiscountAmount = 0;

        if (calculatedSubTotal > MIN_PURCHASE_FOR_BOGO) {
            bogoDiscountAmount = findCheapestBeveragePrice();
            calculatedSubTotal -= bogoDiscountAmount;
        }

        calculatedVat = calculatedSubTotal * VAT_RATE;
        grandTotal = calculatedSubTotal + calculatedVat + SERVICE_FEE;

        double discountAmount = 0;
        if (calculatedSubTotal > MIN_PURCHASE_FOR_DISCOUNT) {
            discountAmount = calculatedSubTotal * DISCOUNT_RATE;
            grandTotal -= discountAmount;
        }

        System.out.println("---------------------------------------------");
        System.out.println("            STRUK PEMBAYARAN ORDER           ");
        System.out.println("---------------------------------------------");
        System.out.printf("Nama Customer: %s%n", order.getCustomerName());
        System.out.println("---------------------------------------------");
        System.out.println("                 DETAIL ORDER                ");
        System.out.println("---------------------------------------------");

        if (foodDetails.length() > 0) {
            System.out.println("--- Makanan ---");
            System.out.print(foodDetails);
        }

        if (beverageDetails.length() > 0) {
            System.out.println("--- Minuman ---");
            System.out.print(beverageDetails);
        }

        System.out.println("---------------------------------------------");

        if (bogoDiscountAmount > 0) {
            System.out.printf("Promo Beli 1 Gratis 1: %19s%n", "-" + formatter.format(bogoDiscountAmount));
        }

        System.out.printf("Subtotal: %33s%n", formatter.format(calculatedSubTotal));
        System.out.printf("PPN (%d%%): %32s%n", (int) (VAT_RATE * 100), formatter.format(calculatedVat));

        if (discountAmount > 0) {
            System.out.printf("Diskon (10%%): %30s%n", "-" + formatter.format(discountAmount));
        }

        System.out.printf("Biaya Layanan: %27s%n", formatter.format(SERVICE_FEE));

        System.out.println("---------------------------------------------");
        System.out.printf("Grand Total: %29s%n", formatter.format(grandTotal));
        System.out.println("---------------------------------------------\n\n");
    }

    private void reset() {
        foodDetails.setLength(0);
        beverageDetails.setLength(0);
        calculatedSubTotal = 0;
        calculatedVat = 0;
        grandTotal = 0;
    }

    private void buildDetailReceipt() {
        for (OrderItem orderItem : order.getOrderItems()) {
            Menu menu = orderItem.getMenu();
            int quantity = orderItem.getQuantity();
            double itemTotal = menu.getPrice() * quantity;
            calculatedSubTotal += itemTotal;

            String displayLine = String.format("%-26s x %d = %s%n",
                    menu.getName(),
                    quantity,
                    formatter.format(itemTotal)
            );

            if (menu.getCategory() == MenuCategory.MAKANAN) {
                foodDetails.append(displayLine);
                continue;
            }

            if (menu.getCategory() == MenuCategory.MINUMAN) {
                beverageDetails.append(displayLine);
            }
        }
    }

    private double findCheapestBeveragePrice() {
        double cheapest = Double.MAX_VALUE;

        for (OrderItem item : order.getOrderItems()) {
            Menu menu = item.getMenu();
            if (menu.getCategory() == MenuCategory.MINUMAN && menu.getPrice() < cheapest) {
                cheapest = menu.getPrice();
            }
        }

        return (cheapest == Double.MAX_VALUE) ? 0 : cheapest;
    }
}
