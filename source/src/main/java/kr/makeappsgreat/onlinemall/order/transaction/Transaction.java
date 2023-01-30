package kr.makeappsgreat.onlinemall.order.transaction;

import kr.makeappsgreat.onlinemall.model.BaseEntity;
import kr.makeappsgreat.onlinemall.order.Order;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;

@Entity
@Getter
public class Transaction extends BaseEntity {

    @ManyToOne
    @NotNull
    @Setter
    private Order order;
    private String orderDesc;

    @OneToOne(mappedBy = "transaction", optional = false)
    @NotNull
    private TransactionDetail transactionDetail;

    private int subTotal;
    private int deliveryFee;

    public void setTransactionDetail(TransactionDetail detail) {
        if (detail == null) throw new NullPointerException("Unexpected usage : TransactionDetail is null.");

        this.transactionDetail = detail;
        detail.setTransaction(this);
    }
}
