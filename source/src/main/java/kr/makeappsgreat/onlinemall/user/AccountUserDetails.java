package kr.makeappsgreat.onlinemall.user;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.stream.Collectors;

public class AccountUserDetails extends User {

    private Account account;

    public AccountUserDetails(Account account) {
        super(account.getUsername(),
                account.getPassword(),
                account.getRoles()
                        .stream().map(accountRole -> new SimpleGrantedAuthority(accountRole.name()))
                        .collect(Collectors.toSet()));

        this.account = account;
    }

    public Account getAccount() {
        return account;
    }
}
