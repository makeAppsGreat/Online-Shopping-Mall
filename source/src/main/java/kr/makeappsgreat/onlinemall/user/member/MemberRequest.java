package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.common.constraints.EditProfileGroup;
import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.user.AccountRequest;
import lombok.Getter;
import lombok.Setter;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Getter @Setter
public class MemberRequest extends AccountRequest {

    @NotEmpty @Email
    private String email;
    @Valid
    private Address address;
    @NotBlank(groups = EditProfileGroup.class)
    private String mobileNumber;
    private String phoneNumber;
}
