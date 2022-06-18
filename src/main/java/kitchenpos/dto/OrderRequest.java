package kitchenpos.dto;

import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItemEntity;

import java.util.List;
import java.util.stream.Collectors;

public class OrderRequest {
    private Long orderTableId;
    private List<OrderLineItemRequest> orderLineItems;

    public OrderRequest(Long orderTableId, List<OrderLineItemRequest> orderLineItems) {
        this.orderTableId = orderTableId;
        this.orderLineItems = orderLineItems;
    }

    protected OrderRequest() {
    }

    public Order toOrder() {
        return new Order(orderTableId);
    }

    public List<OrderLineItemEntity> toOrderLineItems() {
        return orderLineItems.stream()
                             .map(OrderLineItemRequest::toOrderLineItem)
                             .collect(Collectors.toList());
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public List<OrderLineItemRequest> getOrderLineItems() {
        return orderLineItems;
    }
}
