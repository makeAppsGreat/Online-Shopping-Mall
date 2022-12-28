package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.model.NamedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Getter
public class Account extends NamedEntity {

    @Column(unique = true)
    @NotBlank
    @Setter(AccessLevel.PROTECTED)
    private String username;

    @NotNull
    private String password;

    @ElementCollection
    @Enumerated(EnumType.STRING)
    private Set<AccountRole> roles = new HashSet<>();

    @CreationTimestamp
    private LocalDateTime registeredDate;

    public void encodePassword(PasswordEncoder passwordEncoder) {
        if (password.startsWith("{bcrypt}"))
            throw new IllegalArgumentException(String.format("Already encoded password { username : \"%s\" }", username));

        password = passwordEncoder.encode(password);
    }

    public void addRole(AccountRole role) {
        roles.add(role);
    }
}
