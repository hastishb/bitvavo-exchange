package org.example;

import org.example.domain.OrderBook;
import org.example.printer.OrderBookPrinter;
import org.example.reader.OrderBookFileReader;
import org.example.reader.OrderBookReader;
import org.example.repository.OrderRepository;
import org.example.repository.TradeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ReaderTest {

    @BeforeEach
    void init() {
        TradeRepository.getRepo().deleteAll();
        OrderRepository.getRepo().deleteAll();
        OrderBook.getOrderBook().reset();
    }

    @Test
    void testOrderBookFileReader() {
        OrderBookReader orderBookReader = new OrderBookFileReader();
        orderBookReader.read("src/test/resources/orders.txt");
        OrderBookPrinter printer = new OrderBookPrinter();
        assertThat(OrderBook.getOrderBook().getAsks().peek()).isNotNull();
        assertThat(OrderBook.getOrderBook().getBids().peek()).isNotNull();
        assertThat(TradeRepository.getRepo().findAll().size()).isEqualTo(4);
    }

    @Test
    void testFileNotFound() {
        OrderBookReader orderBookReader = new OrderBookFileReader();

        //when
        Exception exception = assertThrows(RuntimeException.class, () -> orderBookReader.read("file.txt"));

        //then
        assertThat(exception.getMessage()).isEqualTo("file not found");
    }

    @Test
    void testInvalidFileExtension() {
        OrderBookReader orderBookReader = new OrderBookFileReader();

        //when
        Exception exception = assertThrows(RuntimeException.class, () -> orderBookReader.read("file.x"));

        //then
        assertThat(exception.getMessage()).isEqualTo("invalid file extension");
    }
}
