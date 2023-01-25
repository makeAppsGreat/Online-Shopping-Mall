package kr.makeappsgreat.onlinemall.user.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;

@Getter @Setter
public class AgreementRequest {

    @NotNull @AssertTrue
    private Boolean terms1;
    @NotNull @AssertTrue
    private Boolean terms2;
    @NotNull @AssertTrue
    private Boolean terms3;
    @Valid
    private MarketingRequest marketing;
}
