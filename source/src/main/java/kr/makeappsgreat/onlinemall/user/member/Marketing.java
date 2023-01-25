package kr.makeappsgreat.onlinemall.user.member;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Embeddable;
import javax.validation.constraints.NotNull;

@Embeddable
@Getter @Setter
@EqualsAndHashCode
public class Marketing {

    @NotNull
    private Boolean acceptance;
}
