package kr.makeappsgreat.onlinemall.user.member;

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
    @NotBlank
    private String mobileNumber;
    private String phoneNumber;
}
