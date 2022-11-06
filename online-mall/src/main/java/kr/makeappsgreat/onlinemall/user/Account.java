package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NoArgsConstructor @SuperBuilder
@Getter
public class Account extends NamedEntity {

    @Column(unique = true)
    @NotEmpty
    private String username;

    @NotNull
    private String password;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    @Builder.Default
    private Set<AccountRole> roles = new HashSet<>();

    @Builder.Default
    private LocalDateTime registeredDate = LocalDateTime.now();

    public void encodePassword(PasswordEncoder passwordEncoder) {
        if (password.startsWith("{bcrypt}"))
            throw new IllegalArgumentException(String.format("Already encoded password { username : \"%s\" }", username));

        password = passwordEncoder.encode(password);
    }

    public void addRole(AccountRole role) {
        roles.add(role);
    }

    protected void setUsername(String username) {
        this.username = username;
    }
}
