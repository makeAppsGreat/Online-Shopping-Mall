package kr.makeappsgreat.onlinemall.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AccountService<T extends Account> implements UserDetailsService {

    protected final AccountRepository<T> accountRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Account account = accountRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        return new AccountUserDetails(account);
    }

    public boolean isDuplicatedUser(String username) {
        return accountRepository.existsByUsername(username);
    }

    public T join(T account) {
        String username = account.getUsername();
        if (isDuplicatedUser(username)) throw new DuplicateKeyException("Duplicated username : " + username);

        return accountRepository.save(account);
    }
}
