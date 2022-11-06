package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.model.Address;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

@Entity
@NoArgsConstructor @SuperBuilder
@Getter
public class Member extends Account {

    @NotEmpty @Email
    private String email; // Do set email to username.

    @Embedded
    private Address address;

    private String phoneNumber;

    @NotBlank
    private String mobileNumber;

    public Member foo(PasswordEncoder passwordEncoder) {
        setUsername(this.email);
        encodePassword(passwordEncoder);
        addRole(AccountRole.ROLE_USER);

        return this;
    }
}
