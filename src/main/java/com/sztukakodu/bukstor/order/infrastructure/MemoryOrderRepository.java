package com.sztukakodu.bukstor.order.infrastructure;

import com.sztukakodu.bukstor.order.domain.Order;
import com.sztukakodu.bukstor.order.domain.OrderRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class MemoryOrderRepository implements OrderRepository {

    private final Map<Long, Order> storage = new ConcurrentHashMap<>();
    private final AtomicLong ID_NEXT_LONG = new AtomicLong(0L);

    @Override
    public Order save(Order order) {
        if (order.getId() == null) {
            order.setId(ID_NEXT_LONG.getAndIncrement());
            order.setCreatedAt(LocalDateTime.now());
        }
        storage.put(order.getId(), order);
        return order;
    }

    @Override
    public List<Order> findAll() {
        return new ArrayList<>(storage.values());
    }
}
