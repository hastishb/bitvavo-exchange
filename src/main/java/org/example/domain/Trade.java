package org.example.domain;

public class Trade {
    private final Order takerOrder;
    private final Order makerOrder;
    private final long price;
    private final long quantity;

    public Trade(Order takerOrder, Order makerOrder, long matchedQuantity) {
        this.takerOrder = takerOrder;
        this.makerOrder = makerOrder;
        this.price = makerOrder.getPrice();
        this.quantity = matchedQuantity;
    }

    public Order getTakerOrder() {
        return takerOrder;
    }

    public Order getMakerOrder() {
        return makerOrder;
    }

    public long getPrice() {
        return price;
    }

    public long getQuantity() {
        return quantity;
    }
}
