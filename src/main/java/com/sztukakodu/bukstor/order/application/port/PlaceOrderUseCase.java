package com.sztukakodu.bukstor.order.application.port;

import com.sztukakodu.bukstor.order.domain.OrderItem;
import com.sztukakodu.bukstor.order.domain.Recipient;
import lombok.Builder;
import lombok.Singular;
import lombok.Value;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public interface PlaceOrderUseCase {

    PlaceOrderResponse placeOrder(PlaceOrderCommand command);

    @Builder
    @Value
    class PlaceOrderCommand {
        @Singular
        List<OrderItem> items;
        Recipient recipient;
    }

    @Value
    class PlaceOrderResponse {
        boolean success;
        Long orderId;
        List<String> errors;

        public static PlaceOrderResponse success(Long orderId) {
            return new PlaceOrderResponse(true, orderId, Collections.emptyList());
        }

        public static PlaceOrderResponse failure(String ... errors) {
            return new PlaceOrderResponse(false, null, Arrays.asList(errors));
        }
    }
}
