package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.product.Product;
import kr.makeappsgreat.onlinemall.user.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Optional<Cart> findByMemberAndProduct(Member member, Product product);

    Page<Cart> findByMember(Member member, Pageable pageable);
}
