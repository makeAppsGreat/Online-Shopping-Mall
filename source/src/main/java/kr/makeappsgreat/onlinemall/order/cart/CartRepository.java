package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.product.Product;
import kr.makeappsgreat.onlinemall.user.member.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberAndProduct(Member member, Product product);

    List<Cart> findByMemberOrderByUpdateDate(Member member);
}
