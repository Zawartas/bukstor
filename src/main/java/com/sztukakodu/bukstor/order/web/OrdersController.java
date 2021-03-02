package com.sztukakodu.bukstor.order.web;

import com.sztukakodu.bukstor.catalog.application.port.CatalogUseCase;
import com.sztukakodu.bukstor.order.application.port.OrderUseCase;
import com.sztukakodu.bukstor.order.application.port.OrderUseCase.PlaceOrderCommand;
import com.sztukakodu.bukstor.order.application.port.OrderUseCase.PlaceOrderResponse;
import com.sztukakodu.bukstor.order.application.port.QueryOrderUseCase;
import com.sztukakodu.bukstor.order.domain.Order;
import com.sztukakodu.bukstor.order.domain.OrderItem;
import com.sztukakodu.bukstor.order.domain.OrderStatus;
import com.sztukakodu.bukstor.order.domain.Recipient;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {

    private final OrderUseCase orders;
    private final QueryOrderUseCase queryOrder;
    private final CatalogUseCase catalog;

    @GetMapping
    public List<Order> getAllOrders() {
        return queryOrder.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getById(@PathVariable Long id) {
        return queryOrder
                .findById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<PlaceOrderResponse> addOrder(@Valid @RequestBody RestCreateOrderCommand command) {

        PlaceOrderCommand order = PlaceOrderCommand.builder()
                .recipient(command.getRecipient())
                .items(command.getOrderedBooks(catalog))
                .build();

        PlaceOrderResponse response = orders.placeOrder(order);
        if (!response.isSuccess()) {
            String message = String.join(", ", response.getErrors());
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, message);
        }
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void changeOrderStatus(@PathVariable Long id, @RequestBody OrderStatus updatedStatus) {
        queryOrder.findById(id).ifPresent(order -> order.setStatus(updatedStatus));

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        orders.removeOrderById(id);
    }

    @Value
    private static class RestCreateOrderCommand {

        String name, phone, street, city, zipCode, email;

        @Size(min = 1)
        List<OrderedBook> orderedBooks;

        Recipient getRecipient() {
            return Recipient.builder().name(name).phone(phone).street(street)
                    .city(city).zipCode(zipCode).email(email)
                    .build();
        }

        List<OrderItem> getOrderedBooks(CatalogUseCase catalog) {
            return orderedBooks.stream()
                    .map(orderedBook ->
                            OrderItem.builder()
                                .book(catalog.findById(orderedBook.getId())
                                        .orElseThrow(() -> new NoSuchElementException(
                                                "No book with id: " + orderedBook.getId())))
                                .quantity(orderedBook.getQuantity())
                                .build())
                    .collect(Collectors.toList());
        }
    }

    @Value
    private static class OrderedBook {
        Long id;
        @Positive
        int quantity;
    }
}