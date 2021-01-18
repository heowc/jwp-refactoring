package kitchenpos.tablegroup.ui;

import kitchenpos.tablegroup.application.OrderTableGroupService;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
public class OrderTableGroupRestController {

    private final OrderTableGroupService orderTableGroupService;

    public OrderTableGroupRestController(final OrderTableGroupService orderTableGroupService) {
        this.orderTableGroupService = orderTableGroupService;
    }

    @PostMapping("/api/table-groups")
    public ResponseEntity<TableGroupResponse> create(@RequestBody final TableGroupRequest request) {
        final TableGroupResponse created = orderTableGroupService.create(request);
        final URI uri = URI.create("/api/table-groups/" + created.getId());
        return ResponseEntity.created(uri).body(created);
    }

    @DeleteMapping("/api/table-groups/{tableGroupId}")
    public ResponseEntity<Void> ungroup(@PathVariable final Long tableGroupId) {
        orderTableGroupService.ungroup(tableGroupId);
        return ResponseEntity.noContent().build();
    }
}