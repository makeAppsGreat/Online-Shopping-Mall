package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.common.NamedRequest;
import kr.makeappsgreat.onlinemall.common.constraints.VerifyPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@VerifyPassword
@Getter @Setter
public class AccountRequest extends NamedRequest {

    @NotBlank
    private String username;
    @NotEmpty
    private String password;
    private String passwordVerify;
}
