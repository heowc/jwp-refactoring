package kitchenpos.fixture;

import java.math.BigDecimal;
import java.util.List;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Validator;
import kitchenpos.menus.menu.domain.Menu;
import kitchenpos.menus.menu.domain.MenuProducts;
import kitchenpos.menus.menu.dto.MenuProductRequest;
import kitchenpos.menus.menu.dto.MenuRequest;

public class MenuFixture {

    private MenuFixture() {
    }

    public static Menu of(
        final Long id,
        final String name,
        final long price,
        final MenuProducts menuProducts,
        final Long menuGroupId,
        final Validator<Menu> menuValidator
    ) {
        return new Menu(
            id,
            new Name(name),
            new Price(BigDecimal.valueOf(price)),
            menuProducts,
            menuGroupId,
            menuValidator
        );
    }

    public static Menu of(
        final String name,
        final long price,
        final MenuProducts menuProducts,
        final Long menuGroupId,
        final Validator<Menu> menuValidator
    ) {
        return of(null, name, price, menuProducts, menuGroupId, menuValidator);
    }

    public static MenuRequest ofRequest(
        final String name,
        final BigDecimal price,
        final List<MenuProductRequest> menuProducts,
        final Long menuGroupId
    ) {
        return new MenuRequest(name, price, menuProducts, menuGroupId);
    }

    public static MenuRequest ofRequest(
        final String name,
        final long price,
        final List<MenuProductRequest> menuProducts,
        final Long menuGroupId
    ) {
        return ofRequest(name, BigDecimal.valueOf(price), menuProducts, menuGroupId);
    }
}