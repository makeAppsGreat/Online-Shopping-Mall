package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.order.Item;
import kr.makeappsgreat.onlinemall.product.ProductController;
import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Getter
public class Cart extends Item<Cart> {

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateDate;

    @Transient
    private int total;

    @Transient
    private String link;

    /**
     * Sum quantity of this and all children.
     */
    public int sumQuantity() {
        int quantity = this.quantity;
        if (this.options != null) {
            for (Cart option : this.options) quantity += option.getQuantity();
        }

        return quantity;
    }

    public void initForService() {
        this.total = this.quantity * this.product.getPrice();
        this.link = ProductController.PRODUCT_DETAIL_PREFIX + this.product.getId();

        if (this.options != null) {
            for (Cart item : options) {
                item.total = item.quantity * item.product.getPrice();
                item.link = ProductController.PRODUCT_DETAIL_PREFIX + item.product.getId();
            }
        }
    }
}
