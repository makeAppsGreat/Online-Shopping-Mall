package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.model.BaseEntity;
import kr.makeappsgreat.onlinemall.order.transaction.Transaction;
import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.Getter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "order_info")
@Getter
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "orderer_id")
    private Member orderer;

    // private OrderDetail orderDetail;

    @OneToMany
    private List<Transaction> transactions;

    @PositiveOrZero
    private int grandTotal;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    @NotBlank
    private String receiver;

    @NotBlank
    private String contact;

    private String contact2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "dst_zipcode")),
            @AttributeOverride(name = "address", column = @Column(name = "dst_address")),
            @AttributeOverride(name = "address2", column = @Column(name = "dst_address2"))
    })
    @Valid
    private Address destination;

    private String memo;

    @CreationTimestamp
    private LocalDateTime orderDate;
}
