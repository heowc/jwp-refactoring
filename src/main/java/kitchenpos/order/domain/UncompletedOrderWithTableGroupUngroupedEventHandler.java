package kitchenpos.order.domain;

import kitchenpos.core.domain.DomainService;
import kitchenpos.table.domain.TableGroupUngroupedEvent;
import kitchenpos.table.exception.CannotUngroupException;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@DomainService
public class UncompletedOrderWithTableGroupUngroupedEventHandler {

    private final OrderRepository orderRepository;

    public UncompletedOrderWithTableGroupUngroupedEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @EventListener
    @Transactional
    public void handle(TableGroupUngroupedEvent event) {
        List<Long> orderTableIds = event.getOrderTableIds();
        if (hasUncompletedOrder(orderTableIds)) {
            throw new CannotUngroupException();
        }
    }

    private boolean hasUncompletedOrder(List<Long> orderTableIds) {
        return orderRepository.existsByOrderTableIdInAndOrderStatusIn(orderTableIds,
                                                                      OrderStatus.getUncompletedStatuses());
    }
}