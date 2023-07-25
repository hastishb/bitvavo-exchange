package org.example;

import org.example.domain.Order;
import org.example.domain.OrderBook;
import org.example.printer.OrderBookPrinter;
import org.example.printer.TradePrinter;
import org.example.repository.TradeRepository;
import org.junit.jupiter.api.Test;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static org.assertj.core.api.Assertions.assertThat;

public class PrinterTest {
    private String getMD5(String s) {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("MD5");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        md.update(s.getBytes());
        byte[] digest = md.digest();
        return HexFormat.of().formatHex(digest);
    }
    @Test //test Bitvavo test case
    void givenOrdersFromBitvavo_whenAddToOrderBook_thenCorrectMD5() {
        TradePrinter tradePrinter = new TradePrinter();
        OrderBookPrinter orderBookPrinter = new OrderBookPrinter();
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("10000,B,98,25500"));
        orderBook.place(new Order("10005,S,105,20000"));
        orderBook.place(new Order("10001,S,100,500"));
        orderBook.place(new Order("10002,S,100,10000"));
        orderBook.place(new Order("10003,B,99,50000"));
        orderBook.place(new Order("10004,S,103,100"));
        StringBuilder s = new StringBuilder();
        TradeRepository.getRepo().findAll().forEach(trade -> s.append(tradePrinter.stringify(trade)));
        s.append(orderBookPrinter.stringify(orderBook));
        assertThat(getMD5(s.toString())).isEqualTo("8ff13aad3e61429bfb5ce0857e846567");

        orderBook.place(new Order("10006,B,105,16000"));
        StringBuilder s2 = new StringBuilder();
        TradeRepository.getRepo().findAll().forEach(trade -> s2.append(tradePrinter.stringify(trade)));

        s2.append(orderBookPrinter.stringify(orderBook));
        assertThat(getMD5(s2.toString())).isEqualTo("ce8e7e5ab26ab5a7db6b7d30759cf02e");
    }
}
