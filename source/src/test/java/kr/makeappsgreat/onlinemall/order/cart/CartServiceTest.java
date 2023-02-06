package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.user.member.Member;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest//(classes = CartService.class)
@ActiveProfiles("service")
class CartServiceTest {

    @Autowired private CartService cartService;
    @Autowired private CartRepository cartRepository;

    private Member member;

    @BeforeEach
    void init() {
        // Given
        member = cartRepository.findAll().stream()
                .filter(cart -> cart.getMember() != null)
                .findFirst()
                .get()
                .getMember();
    }

    /**
     * Test fetching objects without @Transactional
     */
    @Test
    void listCart() {
        // When
        List<Cart> cart = cartService.listCart(member);

        // Then
        assertThat(cart.get(0).getProduct()).isNotNull();
    }
}