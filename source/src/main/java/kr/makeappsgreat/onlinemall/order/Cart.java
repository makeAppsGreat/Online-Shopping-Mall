package kr.makeappsgreat.onlinemall.order;

import kr.makeappsgreat.onlinemall.user.member.Member;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.time.LocalDateTime;

@Entity
@Getter
public class Cart extends Item<Cart> {

    @ManyToOne
    @JoinColumn(name = "member_id")
    @Setter
    private Member member;

    @CreationTimestamp
    @Column(nullable = false)
    private LocalDateTime addedDate;
}
