package com.sztukakodu.bukstor.order.application.port;

import com.sztukakodu.bukstor.order.domain.Order;

import java.util.List;

public interface QueryOrderUseCase {
    List<Order> findAll();
}
