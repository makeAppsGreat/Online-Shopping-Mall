package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.order.Item;
import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.UpdateTimestamp;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = {"member_id", "product_id"}))
@Getter
public class Cart extends Item<Cart> {

    @ManyToOne
    @JoinColumn(name = "member_id")
    @NotNull
    @Setter
    private Member member;

    @UpdateTimestamp
    @Column(nullable = false)
    private LocalDateTime updateDate;
}
