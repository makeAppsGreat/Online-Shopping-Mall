package kr.makeappsgreat.onlinemall.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService<T extends Account> {

    protected final AccountRepository<T> accountRepository;
    protected final PasswordEncoder passwordEncoder;

    public boolean isDuplicatedUser(String username) {
        return accountRepository.existsByUsername(username);
    }

    public T join(T account) {
        account.encodePassword(passwordEncoder);
        account.addRole(AccountRole.ROLE_USER);

        verifyAccount(account);
        return accountRepository.save(account);
    }

    /**
     * Do call this method before saving account in #join
     */
    protected void verifyAccount(T account) {
        String username = account.getUsername();

        if (isDuplicatedUser(username))
            throw new DuplicateKeyException(String.format("Duplicated username { username : \"%s\" }", username));
    }
}
