package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.order.ItemRequest;
import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class CartRequest extends ItemRequest<CartRequest> {

    private String productName;
    private int productPrice;
    private String productImageLink;

    private int total;
    private String link;
    private Boolean select = true;
}
