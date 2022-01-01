package kitchenpos.order.domain;

import kitchenpos.domain.DomainService;
import kitchenpos.domain.Validator;
import java.util.List;
import java.util.NoSuchElementException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;

@DomainService
public class OrderValidator implements Validator<Order> {

    private final OrderTableRepository orderTableRepository;
    private final MenuRepository menuRepository;

    public OrderValidator(
        final OrderTableRepository orderTableRepository,
        final MenuRepository menuRepository
    ) {
        this.orderTableRepository = orderTableRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public void validate(final Order order) {
        validateOrderMenu(order);
        validateOrderTable(order);
    }

    private void validateOrderMenu(final Order order) {
        final List<Long> menuIds = order.getMenuIds();
        if (menuIds.isEmpty()) {
            throw new IllegalArgumentException("1개 이상의 등록된 메뉴로 매장 주문을 등록할 수 있습니다.");
        }

        if (!menuRepository.existAll(menuIds)) {
            throw new NoSuchElementException("메뉴가 없으면 매장 주문을 등록할 수 없습니다.");
        }
    }

    private void validateOrderTable(final Order order) {
        final OrderTable orderTable = orderTableRepository.findById(order.getOrderTableId())
            .orElseThrow(() -> new NoSuchElementException("등록되지 않은 주문 테이블에 주문을 등록할 수 없습니다."));

        if (orderTable.isEmpty()) {
            throw new IllegalStateException("빈 테이블에는 매장 주문을 등록할 수 없습니다.");
        }
    }
}