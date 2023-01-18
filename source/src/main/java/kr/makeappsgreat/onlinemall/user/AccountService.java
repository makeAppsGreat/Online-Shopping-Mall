package kr.makeappsgreat.onlinemall.user;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService<T extends Account> {

    protected final AccountRepository<T> accountRepository;
    protected final PasswordEncoder passwordEncoder;
    private final MessageSource messageSource;

    public boolean isDuplicatedUser(String username) {
        return accountRepository.existsByUsername(username);
    }

    public T join(T account) {
        account.encodePassword(passwordEncoder);

        verifyAccount(account);
        return accountRepository.save(account);
    }

    public void changePassword(Account account, String oldPassword, String passwordToChange) {
        T entity = accountRepository.findById(account.getId()).get();

        if (!passwordEncoder.matches(oldPassword, entity.getPassword())) {
            throw new BadCredentialsException(
                    messageSource.getMessage(
                            "account.password-not-match",
                            null,
                            LocaleContextHolder.getLocale()));
        }

        entity.changePassword(passwordEncoder, passwordToChange);
        accountRepository.save(entity);
    }

    /**
     * Do call this method before saving account in #join
     */
    protected void verifyAccount(T account) {
        String username = account.getUsername();

        if (isDuplicatedUser(username))
            throw new DuplicateKeyException(String.format("Duplicated username { username : '%s' }", username));
    }
}
