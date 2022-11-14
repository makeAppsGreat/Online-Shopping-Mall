package kr.makeappsgreat.onlinemall.user.member;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.AssertTrue;

@Builder
@Getter @Setter
public class AgreementRequest {

    @AssertTrue
    private Boolean terms1;
    @AssertTrue
    private Boolean terms2;
    @AssertTrue
    private Boolean terms3;
    private Boolean marketing;
}
