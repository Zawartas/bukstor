package com.sztukakodu.bukstor.order.domain;

import com.sztukakodu.bukstor.catalog.domain.Book;
import lombok.Value;

@Value
public class OrderItem {
    Book book;
    int quantity;
}
