package com.sztukakodu.bukstor.order.application.port;

import com.sztukakodu.bukstor.catalog.domain.Book;
import com.sztukakodu.bukstor.order.domain.Order;
import com.sztukakodu.bukstor.order.domain.OrderStatus;
import com.sztukakodu.bukstor.order.domain.Recipient;
import lombok.Value;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface QueryOrderUseCase {

    List<RichOrder> findAll();

    Optional<RichOrder> findById(Long id);

    @Value
    class RichOrder {

        Long id;
        OrderStatus status;
        List<RichOrderItem> items;
        Recipient recipient;
        LocalDateTime createdAt;

        public BigDecimal totalPrice() {
            return items.stream()
                    .map(item -> item.getBook().getPrice().multiply(new BigDecimal(item.getQuantity())))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
    }

    @Value
    class RichOrderItem {
        Book book;
        int quantity;
    }
}
