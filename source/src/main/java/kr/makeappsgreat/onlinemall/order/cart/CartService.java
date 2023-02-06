package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    public List<Cart> listCart(Member member) {
        List<Cart> cart = cartRepository.findByMemberOrderByUpdateDate(member);
        for (Cart item : cart) item.initForService();

        return cart;
    }

    /**
     * @return sum of added quantity.
     */
    public int addToCart(Cart cart) {
        Optional<Cart> optionalCart = cartRepository.findByMemberAndProduct(cart.getMember(), cart.getProduct());

        if (optionalCart.isPresent()) {
            Cart savedCart = optionalCart.get();
            savedCart.include(cart);

            cartRepository.save(savedCart);
        } else cartRepository.save(cart);

        return cart.sumQuantity();
    }
}
