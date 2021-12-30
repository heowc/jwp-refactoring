package kitchenpos.orders.order.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.domain.Validator;
import kitchenpos.orders.order.domain.Order;
import kitchenpos.orders.order.domain.OrderLineItems;

public class OrderRequest {

    private final Long orderTableId;

    private final List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(final Long orderTableId, final List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    public Order toOrder(final Validator<Order> validator) {
        return new Order(
            getOrderTableId(),
            new OrderLineItems(
                getOrderLineItems().stream()
                    .map(OrderLineItemRequest::toOrderLineItem)
                    .collect(Collectors.toList())
            ),
            validator
        );
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}