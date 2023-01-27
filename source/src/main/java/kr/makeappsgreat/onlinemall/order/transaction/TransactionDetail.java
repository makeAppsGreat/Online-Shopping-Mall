package kr.makeappsgreat.onlinemall.order.transaction;

import kr.makeappsgreat.onlinemall.model.BaseEntity;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToOne;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
public class TransactionDetail extends BaseEntity {

    @OneToOne
    protected Transaction transaction;

    protected int amount;

    @NotNull
    protected LocalDateTime transactionDate;
}
