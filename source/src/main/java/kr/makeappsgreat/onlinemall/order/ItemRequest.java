package kr.makeappsgreat.onlinemall.order;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Positive;
import java.util.List;

@Getter @Setter
public class ItemRequest<T extends ItemRequest> {

    @Positive
    protected Long productId;
    @Positive
    protected int quantity;
    protected List<T> options;
}
