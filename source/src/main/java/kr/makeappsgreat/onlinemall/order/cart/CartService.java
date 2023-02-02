package kr.makeappsgreat.onlinemall.order.cart;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final EntityManager entityManager;

    public void addToCart(Cart cart) {
        System.out.println(">> 100 " + cartRepository.count());
        Optional<Cart> optionalCart = cartRepository.findByMemberAndProduct(cart.getMember(), cart.getProduct());

        if (optionalCart.isPresent()) {
            Cart savedCart = optionalCart.get();
            System.out.println(">> isContains : " + entityManager.contains(savedCart));
        } else {
            cartRepository.save(cart);
        }

        System.out.println(">> 200 " + cartRepository.count());
    }
}
