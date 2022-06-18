package kitchenpos.dto;

import kitchenpos.domain.MenuEntity;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class MenuResponse {
    private Long id;
    private String name;
    private BigDecimal price;
    private Long menuGroupId;
    private List<MenuProductResponse> menuProducts;

    protected MenuResponse(Long id, String name, BigDecimal price, Long menuGroupId,
                           List<MenuProductResponse> menuProducts) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menuGroupId = menuGroupId;
        this.menuProducts = menuProducts;
    }

    protected MenuResponse() {
    }

    public static List<MenuResponse> of(List<MenuEntity> menus) {
        return menus.stream()
                    .map(MenuResponse::of)
                    .collect(Collectors.toList());
    }

    public static MenuResponse of(MenuEntity menu) {
        return new MenuResponse(menu.getId(), menu.getName().getValue(), menu.getPrice().getValue(),
                                menu.getMenuGroupId(), MenuProductResponse.of(menu.getMenuProducts()));
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public Long getMenuGroupId() {
        return menuGroupId;
    }

    public List<MenuProductResponse> getMenuProducts() {
        return menuProducts;
    }
}