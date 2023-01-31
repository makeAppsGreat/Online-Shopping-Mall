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
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "order_info")
@Getter
public class Order extends BaseEntity {

    @ManyToOne
    @JoinColumn(name = "orderer_id")
    private Member orderer;

    @OneToMany(mappedBy = "order")
    private List<OrderDetail> items;

    @OneToMany(mappedBy = "order")
    private List<Transaction> transactions;

    @PositiveOrZero
    private int grandTotal;

    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus status;

    @NotNull @NotBlank
    private String receiver;

    @NotNull @NotBlank
    private String contact;

    private String contact2;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(name = "dst_zipcode", nullable = false)),
            @AttributeOverride(name = "address", column = @Column(name = "dst_address", nullable = false)),
            @AttributeOverride(name = "address2", column = @Column(name = "dst_address2"))
    })
    @Valid
    private Address destination;

    private String memo;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime orderDate;

    public void addItem(OrderDetail item) {
        if (items == null) items = new ArrayList<>();
        items.add(item);
    }

    public void addTransaction(Transaction transaction) {
        if (transactions == null) transactions = new ArrayList<>();
        transactions.add(transaction);
    }
}
