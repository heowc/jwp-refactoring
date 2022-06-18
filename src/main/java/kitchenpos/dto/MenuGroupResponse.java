package kitchenpos.dto;

import kitchenpos.domain.MenuGroup;
import java.util.List;
import java.util.stream.Collectors;

public class MenuGroupResponse {
    private Long id;
    private String name;

    protected MenuGroupResponse(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    protected MenuGroupResponse() {
    }

    public static MenuGroupResponse of(MenuGroup menuGroup) {
        return new MenuGroupResponse(menuGroup.getId(), menuGroup.getName().getValue());
    }

    public static List<MenuGroupResponse> of(List<MenuGroup> menuGroups) {
        return menuGroups.stream()
                         .map(MenuGroupResponse::of)
                         .collect(Collectors.toList());
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
