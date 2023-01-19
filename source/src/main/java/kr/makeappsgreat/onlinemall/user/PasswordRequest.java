package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.common.constraints.VerifyPassword;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotEmpty;

@VerifyPassword(passwordPropertyName = "newPassword", passwordVerifyPropertyName = "newPasswordVerify")
@Getter @Setter
public class PasswordRequest {

    @NotEmpty
    private String oldPassword;
    @NotEmpty
    private String newPassword;
    private String newPasswordVerify;
}
