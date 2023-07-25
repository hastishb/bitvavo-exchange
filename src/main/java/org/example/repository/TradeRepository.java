package org.example.repository;

import org.example.domain.Trade;

import java.util.ArrayList;
import java.util.List;

public class TradeRepository {
    private List<Trade> trades;
    private static TradeRepository repo;

    private TradeRepository() {
        trades = new ArrayList<>();
    }

    public static TradeRepository getRepo() {
        if (repo == null) repo = new TradeRepository();
        return repo;
    }

    public void add(Trade trade) {
        trades.add(trade);
    }

    public List<Trade> findAll() {
        return trades;
    }

    public void deleteAll() {
        trades = new ArrayList<>();
    }
}
