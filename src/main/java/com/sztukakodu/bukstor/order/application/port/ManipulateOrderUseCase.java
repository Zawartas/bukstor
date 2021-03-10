package com.sztukakodu.bukstor.order.application.port;

import com.sztukakodu.bukstor.commons.Either;
import com.sztukakodu.bukstor.order.domain.OrderItem;
import com.sztukakodu.bukstor.order.domain.OrderStatus;
import com.sztukakodu.bukstor.order.domain.Recipient;
import com.sztukakodu.bukstor.order.web.OrdersController.RestCreateOrderCommand;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.List;

public interface ManipulateOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    void deleteOrderById(Long id);

    UpdateStatusResponse updateOrderStatus(UpdateStatusCommand command);

    @Builder
    @Value
    @AllArgsConstructor
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> items;
        Recipient recipient;
    }

    @Value
    class UpdateStatusCommand {
        Long orderId;
        OrderStatus status;
    }

    class PlaceOrderResponse extends Either<String, Long> {

        public PlaceOrderResponse(boolean success, String left, Long right) {
            super(success, left, right);
        }

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, null, orderId);
        }

        public static PlaceOrderResponse failure(String error) {
            return new PlaceOrderResponse(false, error, null);
        }
    }

    class UpdateStatusResponse extends Either<String, OrderStatus> {
        public UpdateStatusResponse(boolean success, String left, OrderStatus right) {
            super(success, left, right);
        }

        public static UpdateStatusResponse success(OrderStatus status) {
            return new UpdateStatusResponse(true, null, status);
        }

        public static UpdateStatusResponse failure(String error) {
            return new UpdateStatusResponse(false, error, null);
        }
    }
}