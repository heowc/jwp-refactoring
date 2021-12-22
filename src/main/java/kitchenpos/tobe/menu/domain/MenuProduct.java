package kitchenpos.tobe.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.tobe.common.domain.Price;
import kitchenpos.tobe.common.domain.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    private Price price;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(
        final Long seq,
        final Long productId,
        final Price price,
        final Quantity quantity
    ) {
        this.seq = seq;
        this.productId = productId;
        this.price = price;
        this.quantity = quantity;
    }

    public MenuProduct(
        final Long productId,
        final Price price,
        final Quantity quantity
    ) {
        this(null, productId, price, quantity);
    }

    public Price calculateTotalPrice() {
        return price.multiply(quantity);
    }
}
