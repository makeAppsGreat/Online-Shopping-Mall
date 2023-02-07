package kr.makeappsgreat.onlinemall.order.cart;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
public class CartRequestWrapper {

    private List<CartRequest> list;

    public boolean add(CartRequest cartRequest) {
        if (list == null) list = new ArrayList<>();

        return list.add(cartRequest);
    }
}
