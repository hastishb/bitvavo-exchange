package org.example;

import org.example.domain.OrderBook;
import org.example.domain.Trade;
import org.example.printer.OrderBookPrinter;
import org.example.printer.Printer;
import org.example.printer.TradePrinter;
import org.example.reader.OrderBookFileReader;
import org.example.reader.OrderBookReader;
import org.example.repository.TradeRepository;

public class Main {
    public static void main(String[] args) {
        try {
            if (args.length == 0) throw new RuntimeException("SOURCE_NOT_PROVIDED");
            OrderBookReader orderBookReader = new OrderBookFileReader();
            orderBookReader.read(args[0]);

            Printer<OrderBook> orderBookPrinter = new OrderBookPrinter();
            Printer<Trade> tradePrinter = new TradePrinter();
            TradeRepository.getRepo().findAll().forEach(tradePrinter::print);
            orderBookPrinter.print(OrderBook.getOrderBook());

        } catch (RuntimeException exception) {
            System.out.println("ERROR: " + exception.getMessage());
        }
    }
}