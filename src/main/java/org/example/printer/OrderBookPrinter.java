package org.example.printer;

import org.example.domain.Order;
import org.example.domain.OrderBook;
import org.example.domain.OrderSide;

import java.text.NumberFormat;
import java.util.PriorityQueue;

public class OrderBookPrinter implements Printer<OrderBook> {
    private final int ORDER_PRICE_MAX_LENGTH = 6;
    private final int ORDER_QUANTITY_MAX_LENGTH = 11;

    @Override
    public void print(OrderBook orderBook) {
        System.out.println(stringify(orderBook));
    }

    @Override
    public String stringify(OrderBook orderBook) {
        StringBuilder s = new StringBuilder();
        var asks = new PriorityQueue<>(orderBook.getAsks());
        var bids = new PriorityQueue<>(orderBook.getBids());
        boolean print = true;
        while (print) {
            s.append(stringify(bids.poll()));
            s.append(" | ");
            s.append(stringify(asks.poll()));
            s.append("\n");
            print = bids.peek() != null || asks.peek() != null;
        }
        return s.toString();
    }

    private String stringify(Order order) {
        if (order == null) {
            return String.format("%1$"+18+"s","");
        }
        String formattedPrice = String.format("%1$" + ORDER_PRICE_MAX_LENGTH + "s" , order.getPrice());
        String formattedQuantity = String.format("%1$" + ORDER_QUANTITY_MAX_LENGTH + "s" , NumberFormat.getInstance().format(order.getUnmatchedQuantity()));
        if (order.getSide().equals(OrderSide.B))
            return formattedQuantity + " " + formattedPrice;
        else
            return formattedPrice + " " + formattedQuantity;
    }
}
