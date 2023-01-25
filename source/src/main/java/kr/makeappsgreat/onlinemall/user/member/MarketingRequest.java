package kr.makeappsgreat.onlinemall.user.member;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotNull;

@Getter @Setter
public class MarketingRequest {

    @NotNull
    private Boolean acceptance;
}
