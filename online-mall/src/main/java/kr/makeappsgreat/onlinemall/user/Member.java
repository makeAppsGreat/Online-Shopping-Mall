package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@RequiredArgsConstructor @SuperBuilder
@Getter @Setter
public class Member extends NamedEntity {

    @Column(unique = true)
    @NotBlank @Email
    private String email;

    @NotNull
    private String password;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<AccountRole> roles = Set.of(AccountRole.USER);

    @Embedded
    private Address address;

    private String phoneNumber;

    @NotBlank
    private String mobileNumber;
}
