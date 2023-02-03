package kr.makeappsgreat.onlinemall.order.cart;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ModelMapper modelMapper;

    public void addToCart(Cart cart) {
        Optional<Cart> optionalCart = cartRepository.findByMemberAndProduct(cart.getMember(), cart.getProduct());

        if (optionalCart.isPresent()) {
            Cart savedCart = optionalCart.get();
            savedCart.include(cart);

            cartRepository.save(savedCart);
        } else cartRepository.save(cart);
    }
}
