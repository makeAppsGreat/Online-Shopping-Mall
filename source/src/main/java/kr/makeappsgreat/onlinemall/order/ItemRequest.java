package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.product.Product;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.List;

@Getter @Setter
public class ItemRequest {

    @NotNull
    protected Product product;
    @Positive
    protected int quantity;
    protected List<ItemRequest> options;

    public void setProduct(Long product) {
        this.product = Product.of(product);
    }
}
