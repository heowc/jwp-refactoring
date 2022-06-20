package kitchenpos.menu.acceptance;

import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuProductResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록됨;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록됨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 신메뉴;
    private ProductResponse 강정치킨;
    private ProductResponse 후라이드;

    @DisplayName("메뉴 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> menu() {
        return Stream.of(
                dynamicTest("메뉴을 등록한다.", () -> {
                    신메뉴 = 메뉴_그룹_등록됨("신메뉴");
                    강정치킨 = 상품_등록됨("강정치킨", BigDecimal.valueOf(15_000L));
                    후라이드 = 상품_등록됨("후라이드", BigDecimal.valueOf(15_000L));

                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정1,후라이드1치킨",
                                                                           BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId(), 강정치킨, 후라이드);

                    메뉴_생성됨(response);
                }),
                dynamicTest("가격이 0미만의 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(-1),
                                                                           신메뉴.getId(), 강정치킨);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("이름이 없는 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청(null, BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId(), 강정치킨);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("메뉴 그룹 없이 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(15_000L),
                                                              null, 강정치킨);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("상품 없이 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId());

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("존재하지 않는 상품이 포함된 메뉴을 등록한다.", () -> {
                    ProductResponse 존재하지_않는_상품 = new ProductResponse(Long.MAX_VALUE, "존재하지 않는 상품", BigDecimal.TEN);

                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("강정치킨", BigDecimal.valueOf(15_000L),
                                                                           신메뉴.getId(), 존재하지_않는_상품);

                    메뉴_생성_실패됨(response);
                }),
                dynamicTest("상품 가격보다 비싼 메뉴을 등록한다.", () -> {
                    ResponseEntity<MenuResponse> response = 메뉴_생성_요청("비싼 강정치킨", BigDecimal.valueOf(18_000L),
                                                                           신메뉴.getId(), 강정치킨);

                    메뉴_생성_실패됨(response);
                }),

                dynamicTest("메뉴 목록을 조회한다.", () -> {
                    ResponseEntity<List<MenuResponse>> response = 메뉴_목록_조회_요청();

                    메뉴_목록_응답됨(response);
                    메뉴_목록_확인됨(response, "강정1,후라이드1치킨");
                    메뉴_목록_메뉴에_메뉴_상품이_포함됨(response, 강정치킨, 후라이드);
                })
        );
    }

    public static MenuResponse 메뉴_등록됨(String name, BigDecimal price, Long menuGroupId,
                                           ProductResponse... products) {
        return 메뉴_생성_요청(name, price, menuGroupId, products).getBody();
    }

    public static ResponseEntity<MenuResponse> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                              ProductResponse... products) {
        Map<String, Object> request = new HashMap<>();
        request.put("name", name);
        request.put("price", price);
        request.put("menuGroupId", menuGroupId);
        request.put("menuProducts", toMenuProducts(products));
        return restTemplate().postForEntity("/api/menus", request, MenuResponse.class);
    }

    private static List<MenuProductRequest> toMenuProducts(ProductResponse... products) {
        return Arrays.stream(products)
                     .map(p -> new MenuProductRequest(p.getId(), 1L)).collect(Collectors.toList());
    }

    public static ResponseEntity<List<MenuResponse>> 메뉴_목록_조회_요청() {
        return restTemplate().exchange("/api/menus", HttpMethod.GET, null,
                                       new ParameterizedTypeReference<List<MenuResponse>>() {});
    }

    public static void 메뉴_생성됨(ResponseEntity<MenuResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
    }

    public static void 메뉴_생성_실패됨(ResponseEntity<MenuResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    public static void 메뉴_목록_응답됨(ResponseEntity<List<MenuResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    public static void 메뉴_목록_확인됨(ResponseEntity<List<MenuResponse>> response, String... names) {
        List<String> productNames = response.getBody()
                                            .stream()
                                            .map(MenuResponse::getName)
                                            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }

    public static void 메뉴_목록_메뉴에_메뉴_상품이_포함됨(ResponseEntity<List<MenuResponse>> response,
                                                           ProductResponse... products) {
        List<Long> actualProductIds = response.getBody()
                                              .stream()
                                              .flatMap(menu -> menu.getMenuProducts().stream())
                                              .map(MenuProductResponse::getProductId)
                                              .collect(Collectors.toList());
        List<Long> expectedProductIds = Arrays.stream(products)
                                              .map(ProductResponse::getId)
                                              .collect(Collectors.toList());
        assertThat(actualProductIds).containsExactlyElementsOf(expectedProductIds);
    }
}