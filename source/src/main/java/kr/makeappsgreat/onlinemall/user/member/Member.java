package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.user.Account;
import kr.makeappsgreat.onlinemall.user.AccountRole;
import lombok.Getter;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Entity
@PrimaryKeyJoinColumn(name = "account_id")
@Getter
public class Member extends Account {

    @NotNull @NotEmpty
    @Email
    private String email; // Do set email to username.

    @OneToOne(mappedBy = "member", optional = false, fetch = FetchType.LAZY)
    @NotNull
    private Agreement agreement;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "zipcode", column = @Column(nullable = false)),
            @AttributeOverride(name = "address", column = @Column(nullable = false))
    })
    @Valid
    private Address address;

    @NotNull @NotBlank
    private String mobileNumber;

    private String phoneNumber;

    public void setAgreement(Agreement agreement) {
        if (agreement == null) throw new NullPointerException("Unexpected usage : Agreement is null.");

        this.agreement = agreement;
        agreement.setMember(this);
    }

    public void adaptToAccount(PasswordEncoder passwordEncoder) {
        setUsername(this.email);
        encodePassword(passwordEncoder);
        addRole(AccountRole.ROLE_USER);
    }
}
