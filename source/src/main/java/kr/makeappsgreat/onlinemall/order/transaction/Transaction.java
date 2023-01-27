package kr.makeappsgreat.onlinemall.order.transaction;

import kr.makeappsgreat.onlinemall.model.BaseEntity;
import kr.makeappsgreat.onlinemall.order.Order;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;

@Entity
@Getter
public class Transaction extends BaseEntity {

    @ManyToOne
    @NotNull
    private Order order;

    @OneToOne
    private TransactionDetail transactionDetail;

    private int subTotal;

    @Positive
    private int deliveryFee;
}
