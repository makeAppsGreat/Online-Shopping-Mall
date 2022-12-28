package kr.makeappsgreat.onlinemall.user.member;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
public class Agreement {

    @Id
    private Long id;

    @OneToOne
    @JoinColumn(name = "member_id")
    @MapsId
    @Setter
    private Member member;

    // 필수
    @NotNull @AssertTrue
    private Boolean terms1; // 이용약관
    @NotNull @AssertTrue
    private Boolean terms2; // 개인정보 수집 이용에 관한 동의
    @NotNull @AssertTrue
    private Boolean terms3; // 개인정보 제3자 제공에 관한 동의
    private LocalDateTime acceptanceDate = LocalDateTime.now();

    // 선택
    @NotNull
    private Boolean marketing;
    private LocalDateTime updateDate = acceptanceDate;

    public LocalDateTime updateMarketingAgreement(boolean marketing) {
        if (this.marketing != marketing) {
            this.marketing = marketing;
            updateDate = LocalDateTime.now();
        }

        return updateDate;
    }
}
