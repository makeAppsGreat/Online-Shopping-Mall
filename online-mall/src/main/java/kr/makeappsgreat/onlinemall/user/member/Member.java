package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.user.Account;
import kr.makeappsgreat.onlinemall.user.AccountRole;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.OneToOne;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@NoArgsConstructor @SuperBuilder
@Getter
public class Member extends Account {

    @NotEmpty @Email
    private String email; // Do set email to username.

    @OneToOne(mappedBy = "member", optional = false)
    private Agreement agreement;

    @Embedded
    @Valid
    private Address address;

    @NotBlank
    private String mobileNumber;

    private String phoneNumber;

    public void setAgreement(Agreement agreement) {
        this.agreement = agreement;
        agreement.setMember(this);
    }

    /** @TODO : Edit the method name. */
    public void foo(PasswordEncoder passwordEncoder) {
        if (getUsername() != null) throw new RuntimeException("Unexpected usage");

        setUsername(this.email);
        encodePassword(passwordEncoder);
        addRole(AccountRole.ROLE_USER);
    }
}
