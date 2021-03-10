package com.sztukakodu.bukstor.order.web;

import com.sztukakodu.bukstor.order.application.port.ManipulateOrderUseCase;
import com.sztukakodu.bukstor.order.application.port.ManipulateOrderUseCase.PlaceOrderCommand;
import com.sztukakodu.bukstor.order.application.port.ManipulateOrderUseCase.PlaceOrderResponse;
import com.sztukakodu.bukstor.order.application.port.ManipulateOrderUseCase.UpdateStatusCommand;
import com.sztukakodu.bukstor.order.application.port.QueryOrderUseCase;
import com.sztukakodu.bukstor.order.application.port.QueryOrderUseCase.RichOrder;
import com.sztukakodu.bukstor.order.domain.Order;
import com.sztukakodu.bukstor.order.domain.OrderItem;
import com.sztukakodu.bukstor.order.domain.OrderStatus;
import com.sztukakodu.bukstor.order.domain.Recipient;
import com.sztukakodu.bukstor.web.CreatedURI;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.Size;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/orders")
@AllArgsConstructor
public class OrdersController {

    private final ManipulateOrderUseCase orders;
    private final QueryOrderUseCase queryOrder;

    @GetMapping
    public List<RichOrder> getOrders() {
        return queryOrder.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<RichOrder> getById(@PathVariable Long id) {
        return queryOrder
                .findById(id)
                .map(book -> ResponseEntity.ok(book))
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<Object> createOrder(@Valid @RequestBody RestCreateOrderCommand command) {

        PlaceOrderCommand order = command.toPlaceOrderCommand();

        PlaceOrderResponse response = orders.placeOrder(order);
        return response.handle(
                orderId -> ResponseEntity.created(orderUri(orderId)).build(),
                error -> ResponseEntity.badRequest().body(error)
        );
    }

    URI orderUri(Long orderId) {
        return new CreatedURI("/" + orderId).uri();
    }

    @PatchMapping("/{id}/status")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus updatedStatus) {
        UpdateStatusCommand command = new UpdateStatusCommand(id, updatedStatus);
            orders.updateOrderStatus(command);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteById(@PathVariable Long id) {
        orders.deleteOrderById(id);
    }

    @Value
    public static class RestCreateOrderCommand {

        String name;
        String phone;
        String street;
        String city;
        String zipCode;
        String email;

        @Size(min = 1)
        List<OrderedBook> orderedBooks;

        Recipient toRecipient() {
            return new Recipient(name, phone, street, city, zipCode, email);
        }

        PlaceOrderCommand toPlaceOrderCommand() {
            List<OrderItem> orderItems = orderedBooks
                    .stream()
                    .map(item -> new OrderItem(item.id, item.quantity))
                    .collect(Collectors.toList());
            return new PlaceOrderCommand(orderItems, toRecipient());
        }
    }

    @Value
    public static class OrderedBook {
        Long id;
        @Positive
        int quantity;
    }
}