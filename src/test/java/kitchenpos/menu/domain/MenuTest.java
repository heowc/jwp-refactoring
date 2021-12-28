package kitchenpos.menu.domain;

import kitchenpos.common.fixtrue.MenuFixture;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.common.fixtrue.MenuProductFixture;
import kitchenpos.common.fixtrue.ProductFixture;
import kitchenpos.product.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.assertj.core.api.ThrowableAssert.ThrowingCallable;

@DisplayName("메뉴 테스트")
class MenuTest {

    MenuGroup 두마리치킨;
    Product 후라이드치킨;
    MenuProduct 후라이드_후라이드_메뉴_상품;

    @BeforeEach
    void setUp() {
        후라이드치킨 = ProductFixture.of("후라이드치킨", BigDecimal.valueOf(16000));
        두마리치킨 = MenuGroupFixture.from("두마리치킨");
        후라이드_후라이드_메뉴_상품 = MenuProductFixture.of(후라이드치킨, 2);
    }

    @Test
    void 메뉴_등록() {
        Menu 후라이드_후라이드 = MenuFixture.of("후라이드+후라이드",
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_후라이드_메뉴_상품)));

        Assertions.assertThat(후라이드_후라이드).isNotNull();
    }

    @Test
    void 메뉴_등록_시_가격은_0원_이상이어야_한다() {
        // given - when
        ThrowingCallable throwingCallable = () -> MenuFixture.of(
                "후라이드+후라이드",
                BigDecimal.valueOf(-1),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_후라이드_메뉴_상품)));

        // then
        assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(throwingCallable).withMessage("상품의 가격은 0원 이상 이어야 합니다.");
    }

    @Test
    void 메뉴_등록_시_메뉴명은_필수이다() {
        // given - when
        ThrowingCallable throwingCallable = () -> MenuFixture.of(null,
                BigDecimal.valueOf(31000),
                두마리치킨,
                MenuProducts.from(Collections.singletonList(후라이드_후라이드_메뉴_상품)));

        // then
        assertThatExceptionOfType(NullPointerException.class).isThrownBy(throwingCallable).withMessage("메뉴명은 필수입니다.");
    }

    @DisplayName("등록하려는 `메뉴`의 가격은 `메뉴 상품`들의 수량 * `상품`의 가격을 모두 더한 금액보다 작아야한다")
    @Test
    void 등록하려는_메뉴의_가격은_메뉴_상품_들의_가격을_모두_더한_금액보다_작아야한다() {
        // given - when
        ThrowingCallable throwingCallable = () -> {
            MenuFixture.of("후라이드+후라이드",
                    BigDecimal.valueOf(33000),
                    두마리치킨,
                    MenuProducts.from(Collections.singletonList(후라이드_후라이드_메뉴_상품)));
        };

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(throwingCallable)
                .withMessage("메뉴의 가격은 메뉴 상품들의 수량 * 상품의 가격을 모두 더한 금액 보다 작거나 같아야 합니다.");
    }
}
