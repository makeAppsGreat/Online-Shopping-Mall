package kr.makeappsgreat.onlinemall.user.member;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@RequiredArgsConstructor
@Getter
public class Agreement {

    @Id @Setter
    private Long id;

    @OneToOne
    @JoinColumn(name = "id")
    @MapsId
    @Setter
    private Member member;

    // 필수
    @AssertTrue
    private final boolean terms; // 이용약관
    @AssertTrue
    private final boolean terms2; // 개인정보 수집 이용에 관한 동의
    @AssertTrue
    private final boolean terms3; // 개인정보 제3자 제공에 관한 동의
    private LocalDateTime acceptanceDate = LocalDateTime.now();

    // 선택
    @NotNull
    private Boolean marketing;
    @NotNull
    private LocalDateTime updateDate;

    public void updateMarketingAgreement(boolean marketing) {
        this.marketing = marketing;
        updateDate = LocalDateTime.now();
    }
}
