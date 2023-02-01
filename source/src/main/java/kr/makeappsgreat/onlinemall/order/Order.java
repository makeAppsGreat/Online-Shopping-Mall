package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.model.BaseEntity;
import kr.makeappsgreat.onlinemall.order.transaction.Transaction;
import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
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
    @Setter
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

    @OneToOne(mappedBy = "order")
    private ShippingInfo shippingInfo;

    private String memo;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime orderDate;

    public void addItem(OrderDetail item) {
        if (items == null) items = new ArrayList<>();

        items.add(item);
        item.setOrder(this);
    }

    public void addTransaction(Transaction transaction) {
        if (transactions == null) transactions = new ArrayList<>();

        transactions.add(transaction);
        transaction.setOrder(this);
    }

    public void setShippingInfo(ShippingInfo shippingInfo) {
        if (shippingInfo == null) throw new NullPointerException("Unexpected usage : ShippingInfo is null.");

        this.shippingInfo = shippingInfo;
        shippingInfo.setOrder(this);
    }
}
