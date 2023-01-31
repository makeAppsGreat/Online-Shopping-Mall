package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.user.member.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {

    Page<Cart> findByMember(Member member, Pageable pageable);
}
