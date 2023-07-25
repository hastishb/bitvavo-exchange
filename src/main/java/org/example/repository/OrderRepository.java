package org.example.repository;

import org.example.domain.Order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderRepository {
    private Map<Long, Order> orders;
    private static OrderRepository repo;

    private OrderRepository() {
        orders = new HashMap<>();
    }

    public static OrderRepository getRepo() {
        if (repo == null) repo = new OrderRepository();
        return repo;
    }

    public void add(Order order) {
        if (orders.get(order.getId()) != null) throw new RuntimeException("DUPLICATE_ORDER_ID: " + order.getId());
        orders.put(order.getId(), order);
    }

    public void update(Order order) {
        if (orders.get(order.getId()) != null) orders.put(order.getId(), order);
    }

    public void deleteAll() {
        orders = new HashMap<>();
    }

    public List<Order> findAll() {
        return orders.values().stream().toList();
    }

    public Order findById(long id) {
        return orders.get(id);
    }
}
