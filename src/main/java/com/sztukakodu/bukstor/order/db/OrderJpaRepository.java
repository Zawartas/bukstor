package com.sztukakodu.bukstor.order.db;

import com.sztukakodu.bukstor.order.domain.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderJpaRepository extends JpaRepository<Order, Long> {

}
