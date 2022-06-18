package kitchenpos.application;

import kitchenpos.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;

@Service
public class OrderTableServiceTestSupport {

    private final MenuServiceTestSupport menuServiceTestSupport;
    private final OrderRepository orderRepository;

    public OrderTableServiceTestSupport(MenuServiceTestSupport menuServiceTestSupport,
                                        OrderRepository orderRepository) {
        this.menuServiceTestSupport = menuServiceTestSupport;
        this.orderRepository = orderRepository;
    }

    @Transactional
    public OrderTableEntity 강정치킨_주문하기(OrderTableEntity orderTable) {
        MenuEntity 강정치킨 = menuServiceTestSupport.신메뉴_강정치킨_가져오기();
        OrderEntity 주문 = new OrderEntity(orderTable.getId());
        주문.addOrderLineItems(Arrays.asList(new OrderLineItemEntity(강정치킨.getId(), 1L)));
        orderRepository.save(주문);
        return orderTable;
    }
}
