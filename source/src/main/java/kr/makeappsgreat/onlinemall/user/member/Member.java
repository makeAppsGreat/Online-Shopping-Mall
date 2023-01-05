package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.user.Account;
import kr.makeappsgreat.onlinemall.user.AccountRole;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Getter
public class Member extends Account {

    @NotEmpty @Email
    private String email; // Do set email to username.

    @OneToOne(mappedBy = "member", optional = false)
    @NotNull
    private Agreement agreement;

    @Embedded
    @Valid
    private Address address;

    @NotBlank
    private String mobileNumber;

    private String phoneNumber;

    public void setAgreement(Agreement agreement) {
        if (agreement == null) throw new NullPointerException("Unexpected usage : Agreement is null.");

        this.agreement = agreement;
        agreement.setMember(this);
    }

    public void adaptToAccount(PasswordEncoder passwordEncoder) {
        if (getUsername() != null) throw new RuntimeException("Unexpected usage : Already password encoded.");

        setUsername(this.email);
        encodePassword(passwordEncoder);
        addRole(AccountRole.ROLE_USER);
    }
}
