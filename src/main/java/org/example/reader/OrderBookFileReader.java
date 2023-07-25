package org.example.reader;

import org.example.domain.Order;
import org.example.domain.OrderBook;
import org.example.validator.OrderValidator;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class OrderBookFileReader implements OrderBookReader{
    @Override
    public void read(String source) {
        if (!source.endsWith(".txt")) throw new ReaderException("invalid file extension");
        try {
            File file = new File(System.getProperty("user.dir") + "/" + source);
            Scanner scanner = new Scanner(file);
            OrderBook orderBook = OrderBook.getOrderBook();
            int lineNumber = 1;
            while (scanner.hasNextLine()) {
                String orderLine = scanner.nextLine();
                Order order = new Order(orderLine);
                orderBook.place(order);
                lineNumber++;
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            throw new ReaderException("file not found");
        }
    }
}
