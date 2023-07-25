package org.example.printer;

import org.example.domain.Trade;

public class TradePrinter implements Printer<Trade> {
    @Override
    public void print(Trade trade) {
        System.out.print(stringify(trade));
    }

    @Override
    public String stringify(Trade trade) {
        return "trade " +
                trade.getTakerOrder().getId() + "," +
                trade.getMakerOrder().getId() + "," +
                trade.getPrice() + "," +
                trade.getQuantity() + "\n";
    }
}
