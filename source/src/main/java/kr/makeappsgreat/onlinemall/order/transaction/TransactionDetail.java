package kr.makeappsgreat.onlinemall.order.transaction;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
public class TransactionDetail implements Serializable {

    @Id
    protected Long id;

    @OneToOne
    @JoinColumn(name = "transaction_id")
    @MapsId
    @Setter
    protected Transaction transaction;

    protected int amount;
    protected int tax;
    protected int serviceFee;
    protected int vat;

    @NotNull
    protected LocalDateTime transactionDate;
}
