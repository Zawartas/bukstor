package com.sztukakodu.bukstor.order.application;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.order.application.port.ManipulateOrderUseCase;
import com.sztukakodu.bukstor.order.db.OrderJpaRepository;
import com.sztukakodu.bukstor.order.domain.*;
import com.sztukakodu.bukstor.order.web.OrdersController.RestCreateOrderCommand;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ManipulatedOrderService implements ManipulateOrderUseCase {

    private final OrderJpaRepository repository;

    @Override
    public PlaceOrderResponse placeOrder(PlaceOrderCommand command) {
        Order order = Order.builder()
                .recipient(command.getRecipient())
                .items(command.getItems())
                .build();
        Order save = repository.save(order);
        return PlaceOrderResponse.success(save.getId());
    }

    @Override
    public void deleteOrderById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command) {
        return repository
                .findById(command.getOrderId())
                .map(order -> {
                    order.setStatus(command.getStatus());
                    repository.save(order);
                    return UpdateStatusResponse.success(order.getStatus());
                })
                .orElse(UpdateStatusResponse.failure("Order not found"));
    }

//    public List<OrderItem> orderedBooksToOrderItems(RestCreateOrderCommand command) {
//        return command.getOrderedBooks().stream()
//                .map(orderedBook ->
//                        OrderItem.builder()
//                                .book(catalog.findById(orderedBook.getId())
//                                        .orElseThrow(() -> new NoSuchElementException(
//                                                "No book with id: " + orderedBook.getId())))
//                                .quantity(orderedBook.getQuantity())
//                                .build())
//                .collect(Collectors.toList());
//    }
}
