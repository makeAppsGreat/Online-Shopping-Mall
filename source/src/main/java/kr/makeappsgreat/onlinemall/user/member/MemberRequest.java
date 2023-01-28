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
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

@Getter @Setter
public class MemberRequest extends AccountRequest {

    @NotNull @NotEmpty
    @Email
    private String email;
    @Valid
    private Address address;
    @NotNull @NotBlank(groups = {Default.class, EditProfileGroup.class})
    private String mobileNumber;
    private String phoneNumber;
}
