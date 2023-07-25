package org.example.domain;

import org.example.repository.OrderRepository;
import org.example.repository.TradeRepository;

import java.util.Comparator;
import java.util.PriorityQueue;

public class OrderBook {
    private PriorityQueue<Order> bids;
    private PriorityQueue<Order> asks;
    private final TradeRepository tradeRepository;
    private final OrderRepository orderRepository;
    private static OrderBook orderBook;

    private OrderBook() {
        this.asks = new PriorityQueue<>(getAsksComparator());
        this.bids = new PriorityQueue<>(getBidsComparator());
        this.tradeRepository = TradeRepository.getRepo();
        this.orderRepository = OrderRepository.getRepo();
    }

    private Comparator<Order> getBidsComparator() {
        return (o1, o2) -> {
            int r = Long.compare(o2.getPrice(), o1.getPrice()); // asks lower price -> higher priority
            if (r == 0) {
                return Long.compare(o1.getOrderSequenceNumber(), o2.getOrderSequenceNumber()); // if same price -> lower number, higher priority
            } else return r;
        };
    }

    private Comparator<Order> getAsksComparator() {
        return (o1, o2) -> {
            int r = Long.compare(o1.getPrice(), o2.getPrice());  // bids higher price -> higher priority
            if (r == 0) {
                return Long.compare(o1.getOrderSequenceNumber(), o2.getOrderSequenceNumber());  // if same price -> lower number, higher priority
            } else return r;
        };
    }

    public static OrderBook getOrderBook() {
        if (orderBook == null) orderBook = new OrderBook();
        return orderBook;
    }

    private long getMatchedQuantity(Order taker, Order maker) {
        // a sell order matches bids with more/equal price (better price for sell)
        // a bid order matched asks with less/equal price (better price for buy)
        if (
                (taker.getSide().equals(OrderSide.S) && taker.getPrice() <= maker.getPrice()) ||
                (taker.getSide().equals(OrderSide.B) && taker.getPrice() >= maker.getPrice())
        ) return Long.min(taker.getUnmatchedQuantity(), maker.getUnmatchedQuantity());
        return 0;
    }

    public Order match(Order taker) {
        PriorityQueue<Order> makerList = taker.getSide().equals(OrderSide.S) ? bids: asks;
        if (makerList.peek() != null) {
            boolean stopMatching = false;
            while (!stopMatching) {
                Order maker = makerList.peek();
                long matchedQuantity = getMatchedQuantity(taker, maker);
                if (matchedQuantity > 0) {
                    taker.decreaseUnmatchedQuantity(matchedQuantity); orderRepository.update(taker);
                    maker.decreaseUnmatchedQuantity(matchedQuantity); orderRepository.update(maker);
                    tradeRepository.add(new Trade(taker, maker, matchedQuantity));
                    if (maker.isCompleted()) makerList.poll(); // remove completed maker
                    if (taker.isCompleted()) stopMatching = true; //taker was fully matched
                    if (makerList.peek() == null ) stopMatching = true; //no more makers to check matching
                } else stopMatching = true; //best maker didn't match so no need to check others
            }
        }
        return taker;
    }

    public void place(Order taker) {
        orderRepository.add(taker);
        //try matching first
        taker = match(taker);
        //now add to order-book if not completed
        PriorityQueue<Order> orderSideList = taker.getSide().equals(OrderSide.S) ? asks: bids;
        if (!taker.isCompleted()) orderSideList.add(taker);
    }

    public PriorityQueue<Order> getBids() {
        return bids;
    }

    public PriorityQueue<Order> getAsks() {
        return asks;
    }

    public void reset() {
        // asks lower price -> higher priority
        this.asks = new PriorityQueue<>(getAsksComparator());
        // bids higher price -> higher priority
        this.bids = new PriorityQueue<>(getBidsComparator());
    }
}
