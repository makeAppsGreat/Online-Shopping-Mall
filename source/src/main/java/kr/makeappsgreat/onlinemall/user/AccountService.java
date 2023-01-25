package kr.makeappsgreat.onlinemall.user;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    protected final ModelMapper modelMapper;
    private final MessageSource messageSource;

    public T retrieve(T account) {
        return accountRepository.findById(account.getId()).get();
    }

    public boolean isDuplicatedUser(String username) {
        return accountRepository.existsByUsername(username);
    }

    public T join(T account) {
        account.encodePassword(passwordEncoder);

        verifyAccount(account);
        return accountRepository.save(account);
    }

    public T changePassword(T account, String oldPassword, String passwordToChange) {
        T entity = this.retrieve(account);

        if (!passwordEncoder.matches(oldPassword, entity.getPassword())) {
            throw new BadCredentialsException(
                    messageSource.getMessage(
                            "account.password-not-match",
                            null,
                            LocaleContextHolder.getLocale()));
        }

        entity.changePassword(passwordEncoder, passwordToChange);


        return accountRepository.save(entity);
    }

    public <S extends AccountRequest> T updateProfile(T account, S request) {
        T entity = this.retrieve(account);
        modelMapper.map(request, entity);

        return accountRepository.save(entity);
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
