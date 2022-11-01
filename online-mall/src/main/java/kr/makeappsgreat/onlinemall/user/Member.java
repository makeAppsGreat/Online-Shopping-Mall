package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Set;

@Entity
@NoArgsConstructor @SuperBuilder
@Getter @Setter
public class Member extends NamedEntity {

    @Column(unique = true)
    @NotEmpty @Email
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

    @Builder.Default
    private LocalDateTime registeredDate = LocalDateTime.now();
}
