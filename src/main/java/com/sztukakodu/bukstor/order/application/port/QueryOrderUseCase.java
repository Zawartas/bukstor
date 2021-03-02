package com.sztukakodu.bukstor.order.application.port;

import com.sztukakodu.bukstor.order.domain.Order;

import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {
    List<Order> findAll();

    Optional<Order> findById(Long id);

}
