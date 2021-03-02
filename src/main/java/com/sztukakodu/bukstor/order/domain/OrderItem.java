package com.sztukakodu.bukstor.order.domain;

import com.sztukakodu.bukstor.catalog.domain.Book;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class OrderItem {

    Book book;

    int quantity;
}
