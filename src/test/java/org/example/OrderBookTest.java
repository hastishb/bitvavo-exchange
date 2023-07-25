package org.example;

import org.example.domain.Order;
import org.example.domain.OrderBook;
import org.example.domain.OrderSide;
import org.example.repository.OrderRepository;
import org.example.repository.TradeRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.PriorityQueue;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class OrderBookTest {

    @AfterEach
    void reset() {
        TradeRepository.getRepo().deleteAll();
        OrderRepository.getRepo().deleteAll();
        OrderBook.getOrderBook().reset();
    }

    @Test
    void givenOrder_whenSameIdAlreadyExists_thenThrowError() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,B,100,100"));
        orderBook.place(new Order("2,S,100,100"));
        Order order = new Order(1, OrderSide.S,105,200);

        //when
        Exception exception = assertThrows(RuntimeException.class, () -> orderBook.place(order));

        //then
        assertThat(exception.getMessage()).isEqualTo("DUPLICATE_ORDER_ID: 1");
        assertThat(OrderRepository.getRepo().findAll().size()).isEqualTo(2);
        assertThat(OrderRepository.getRepo().findById(1L).isCompleted()).isTrue();
        assertThat(OrderRepository.getRepo().findById(2L).isCompleted()).isTrue();
    }

    // TEST MATCHING - NO ADD
    @Test
    void givenBidOrderAndEmptyAsks_whenAddOrder_thenNoTrade() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        Order order = new Order("1,B,100,100");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(0);
        assertThat(orderBook.getBids().size()).isEqualTo(1);
        assertThat(orderBook.getBids().size()).isEqualTo(1);
        assertThat(orderBook.getAsks().size()).isEqualTo(0);
    }

    @Test
    void givenBidOrderCheaperThanBestAsk_thenAddOrder_thenNoTrade() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,100,100"));
        Order order = new Order("2,B,99,100");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(0);
        assertThat(order.getUnmatchedQuantity()).isEqualTo(100);
    }


    @Test
    void givenBidOrderMoreQuantityThanAskSamePrice_thenAddOrder_thenMatchAndBidIncompleteAndAskComplete() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,100,100"));
        Order order = new Order("2,B,100,200");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).getTakerOrder().getId()).isEqualTo(2);
        assertThat(trades.get(0).getTakerOrder().isCompleted()).isEqualTo(false);
        assertThat(trades.get(0).getMakerOrder().getId()).isEqualTo(1);
        assertThat(trades.get(0).getMakerOrder().isCompleted()).isEqualTo(true);
        assertThat(trades.get(0).getPrice()).isEqualTo(100);
        assertThat(trades.get(0).getQuantity()).isEqualTo(100);

        assertThat(orderBook.getAsks().size()).isEqualTo(0);
        assertThat(orderBook.getBids().size()).isEqualTo(1);
        assertThat(orderBook.getBids().peek().getUnmatchedQuantity()).isEqualTo(100);
    }

    @Test
    void givenBidOrderMoreQuantityThanAskLessPrice_thenAddOrder_thenMatchAndBidIncompleteAndAskComplete() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,99,100"));
        Order order = new Order("2,B,100,200");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).getTakerOrder().getId()).isEqualTo(2);
        assertThat(trades.get(0).getTakerOrder().isCompleted()).isEqualTo(false);
        assertThat(trades.get(0).getMakerOrder().getId()).isEqualTo(1);
        assertThat(trades.get(0).getMakerOrder().isCompleted()).isEqualTo(true);
        assertThat(trades.get(0).getPrice()).isEqualTo(99);
        assertThat(trades.get(0).getQuantity()).isEqualTo(100);

        assertThat(orderBook.getAsks().size()).isEqualTo(0);
        assertThat(orderBook.getBids().size()).isEqualTo(1);
        assertThat(orderBook.getBids().peek().getUnmatchedQuantity()).isEqualTo(100);
    }

    @Test
    void givenBidOrderSameQuantitySamePriceAsk_thenAddOrder_thenMatchBidCompleteAndAskComplete() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,100,100"));
        Order order = new Order("2,B,100,100");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).getTakerOrder().getId()).isEqualTo(2);
        assertThat(trades.get(0).getTakerOrder().isCompleted()).isEqualTo(true);
        assertThat(trades.get(0).getMakerOrder().getId()).isEqualTo(1);
        assertThat(trades.get(0).getMakerOrder().isCompleted()).isEqualTo(true);
        assertThat(trades.get(0).getPrice()).isEqualTo(100);
        assertThat(trades.get(0).getQuantity()).isEqualTo(100);

        assertThat(orderBook.getAsks().size()).isEqualTo(0);
        assertThat(orderBook.getBids().size()).isEqualTo(0);
    }

    @Test
    void givenBidOrderSameQuantityLessPriceAsk_thenAddOrder_thenMatchBidCompleteAndAskComplete() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,99,100"));
        Order order = new Order("2,B,100,100");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).getTakerOrder().getId()).isEqualTo(2);
        assertThat(trades.get(0).getTakerOrder().isCompleted()).isEqualTo(true);
        assertThat(trades.get(0).getMakerOrder().getId()).isEqualTo(1);
        assertThat(trades.get(0).getMakerOrder().isCompleted()).isEqualTo(true);
        assertThat(trades.get(0).getPrice()).isEqualTo(99);
        assertThat(trades.get(0).getQuantity()).isEqualTo(100);

        assertThat(orderBook.getAsks().size()).isEqualTo(0);
        assertThat(orderBook.getBids().size()).isEqualTo(0);
    }

    @Test
    void givenBidOrderLessQuantityAskLessPrice_thenAddOrder_thenMatchAndBidCompleteAndAskIncomplete() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,99,200"));
        Order order = new Order("2,B,100,100");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(1);
        assertThat(trades.get(0).getTakerOrder().getId()).isEqualTo(2);
        assertThat(trades.get(0).getTakerOrder().isCompleted()).isEqualTo(true);
        assertThat(trades.get(0).getMakerOrder().getId()).isEqualTo(1);
        assertThat(trades.get(0).getMakerOrder().isCompleted()).isEqualTo(false);
        assertThat(trades.get(0).getPrice()).isEqualTo(99);
        assertThat(trades.get(0).getQuantity()).isEqualTo(100);

        assertThat(orderBook.getAsks().size()).isEqualTo(1);
        assertThat(orderBook.getAsks().peek().getUnmatchedQuantity()).isEqualTo(100);
        assertThat(orderBook.getBids().size()).isEqualTo(0);
    }

    @Test
    void givenBidOrderAsksLessPriceToMorePrice_whenAddOrder_thenMatchMultipleAsksWithRemainingAsks() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,100,100"));
        orderBook.place(new Order("2,S,100,200"));
        orderBook.place(new Order("3,S,102,300"));
        orderBook.place(new Order("4,S,105,100"));
        orderBook.place(new Order("5,S,110,100"));
        orderBook.place(new Order("6,S,111,200"));
        Order order = new Order("7,B,110,900");

        //when
        orderBook.place(order);

        //then
        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(5);

        assertThat(orderBook.getAsks().size()).isEqualTo(1);
        Order highestPriorityAskOrder = orderBook.getAsks().peek();
        assertThat(highestPriorityAskOrder).isNotNull();
        assertThat(highestPriorityAskOrder.getId()).isEqualTo(6);
        assertThat(highestPriorityAskOrder.getUnmatchedQuantity()).isEqualTo(200);

        assertThat(orderBook.getBids().size()).isEqualTo(1);
        Order highestPriorityBidOrder = orderBook.getBids().peek();
        assertThat(highestPriorityBidOrder).isNotNull();
        assertThat(highestPriorityBidOrder.getId()).isEqualTo(7);
        assertThat(highestPriorityBidOrder.getUnmatchedQuantity()).isEqualTo(100);
    }

    @Test
    void test() {
        OrderBook orderBook = OrderBook.getOrderBook();
        orderBook.place(new Order("1,S,100,100"));
        orderBook.place(new Order("2,S,100,200"));
        orderBook.place(new Order("3,S,102,300"));
        orderBook.place(new Order("4,S,105,100"));
        orderBook.place(new Order("5,S,110,100"));
        orderBook.place(new Order("6,S,111,200"));

        Order order = new Order("7,B,111,900");
        orderBook.place(order);

        var trades = TradeRepository.getRepo().findAll();
        assertThat(trades.size()).isEqualTo(6);

        assertThat(orderBook.getAsks().size()).isEqualTo(1);
        Order highestPriorityAskOrder = orderBook.getAsks().peek();
        assertThat(highestPriorityAskOrder).isNotNull();
        assertThat(highestPriorityAskOrder.getId()).isEqualTo(6);
        assertThat(highestPriorityAskOrder.getUnmatchedQuantity()).isEqualTo(100);

        assertThat(orderBook.getBids().size()).isEqualTo(0);
    }

    @Test
    void givenEmptyOrderBook_whenPlaceBidOrders_TheCorrectPriority() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();

        //when
        orderBook.place(new Order("1,B,100,100"));
        orderBook.place(new Order("2,B,99,100"));
        orderBook.place(new Order("3,B,102,100"));
        orderBook.place(new Order("4,B,100,100"));
        orderBook.place(new Order("5,B,97,100"));
        orderBook.place(new Order("6,B,105,100"));

        //then
        var bids = new PriorityQueue<>(orderBook.getBids());
        assertThat(bids.poll().getId()).isEqualTo(6);
        assertThat(bids.poll().getId()).isEqualTo(3);
        assertThat(bids.poll().getId()).isEqualTo(1);
        assertThat(bids.poll().getId()).isEqualTo(4);
        assertThat(bids.poll().getId()).isEqualTo(2);
        assertThat(bids.poll().getId()).isEqualTo(5);
        assertThat(bids.poll()).isNull();
    }

    @Test
    void givenEmptyOrderBook_whenPlaceAskOrders_TheCorrectPriority() {
        //given
        OrderBook orderBook = OrderBook.getOrderBook();

        //when
        orderBook.place(new Order("1,S,100,100"));
        orderBook.place(new Order("2,S,99,100"));
        orderBook.place(new Order("3,S,102,100"));
        orderBook.place(new Order("4,S,100,100"));
        orderBook.place(new Order("5,S,97,100"));
        orderBook.place(new Order("6,S,105,100"));

        //then
        var asks = new PriorityQueue<>(orderBook.getAsks());
        assertThat(asks.poll().getId()).isEqualTo(5);
        assertThat(asks.poll().getId()).isEqualTo(2);
        assertThat(asks.poll().getId()).isEqualTo(1);
        assertThat(asks.poll().getId()).isEqualTo(4);
        assertThat(asks.poll().getId()).isEqualTo(3);
        assertThat(asks.poll().getId()).isEqualTo(6);
        assertThat(asks.poll()).isNull();
    }
}
