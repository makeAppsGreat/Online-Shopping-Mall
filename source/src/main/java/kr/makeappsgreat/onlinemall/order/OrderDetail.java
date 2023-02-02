package kr.makeappsgreat.onlinemall.order;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.constraints.PositiveOrZero;

@Entity
@Getter
public class OrderDetail extends Item<OrderDetail> {

    @ManyToOne
    @Setter
    private Order order;

    @PositiveOrZero
    private int total;
}
