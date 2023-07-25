package org.example.domain;

import org.example.validator.OrderValidator;

public class Order {
    private final long id;
    private final OrderSide side;
    private final long price;
    private final long quantity;
    private long unmatchedQuantity;
    public Order(long id, OrderSide side, long price, long quantity) {
        this.id = id;
        this.side = side;
        this.price = price;
        this.quantity = quantity;
        this.unmatchedQuantity = quantity;
    }

    public Order(String orderString) {
        OrderValidator.validate(orderString);
        String[] orderData = orderString.split(",");
        this.id = Long.parseLong(orderData[0]);
        this.side = OrderSide.valueOf(orderData[1]);
        this.price = Long.parseLong(orderData[2]);
        this.quantity = Long.parseLong(orderData[3]);
        this.unmatchedQuantity = this.quantity;
    }

    public long getId() {
        return id;
    }

    public OrderSide getSide() {
        return side;
    }

    public long getPrice() {
        return price;
    }


    public boolean isCompleted() {
        return unmatchedQuantity==0;
    }

    public void decreaseUnmatchedQuantity(long matchedQuantity) {
        unmatchedQuantity -= matchedQuantity;
    }

    public long getUnmatchedQuantity() {
        return unmatchedQuantity;
    }

}
