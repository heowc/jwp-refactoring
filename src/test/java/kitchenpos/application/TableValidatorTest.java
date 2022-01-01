package kitchenpos.application;

import kitchenpos.common.exceptions.EmptyOrderException;
import kitchenpos.common.exceptions.OrderStatusNotProcessingException;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderRepository;
import kitchenpos.domain.OrderTable;
import kitchenpos.fixture.TestMenuFactory;
import kitchenpos.fixture.TestOrderFactory;
import kitchenpos.fixture.TestOrderTableFactory;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class TableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private TableValidator tableValidator;

    @DisplayName("주문 목록이 존재하지 않으면 해지할 수 없다.")
    @Test
    void validateUnGroup() {
        final OrderTable 주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, true);
        final OrderTable 빈_주문테이블 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, false);
        final List<OrderTable> 주문테이블_목록_조회됨 = Lists.newArrayList(주문테이블, 빈_주문테이블);
        final List<Order> 빈_주문_목록_조회됨 = new ArrayList<>();

        given(orderRepository.findByOrderTableIdIn(anyList())).willReturn(빈_주문_목록_조회됨);

        assertThatThrownBy(() -> tableValidator.validateUnGroup(주문테이블_목록_조회됨))
                .isInstanceOf(EmptyOrderException.class);
    }

    @DisplayName("주문의 상태가 Cooking, Meal 상태가 있으면 해지할 수 없다.")
    @Test
    void validateUnGroup2() {
        final Menu 메뉴1 = TestMenuFactory.메뉴_조회됨(1L, "메뉴", 50000, "메뉴그룹");
        final OrderTable 주문테이블1 = TestOrderTableFactory.주문_테이블_조회됨(1L, 10, true);
        final Order 주문1 = TestOrderFactory.주문_meal_조회됨(1, 1L, 주문테이블1.getId(), 메뉴1.getId(), 5);

        final Menu 메뉴2 = TestMenuFactory.메뉴_조회됨(2L, "메뉴", 50000, "메뉴그룹");
        final OrderTable 주문테이블2 = TestOrderTableFactory.주문_테이블_조회됨(2L, 10, true);
        final Order 주문2 = TestOrderFactory.주문_cooking_조회됨(1, 2L, 주문테이블2.getId(), 메뉴2.getId(), 5);

        final List<OrderTable> 주문테이블_목록_조회됨 = Lists.newArrayList(주문테이블1, 주문테이블2);
        final List<Order> 주문_목록_조회됨 = Lists.newArrayList(주문1, 주문2);

        given(orderRepository.findByOrderTableIdIn(anyList())).willReturn(주문_목록_조회됨);

        assertThatThrownBy(() -> tableValidator.validateUnGroup(주문테이블_목록_조회됨))
                .isInstanceOf(OrderStatusNotProcessingException.class);
    }
}
