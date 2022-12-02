package kr.makeappsgreat.onlinemall.user;

import lombok.RequiredArgsConstructor;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AccountService<T extends Account> {

    protected final AccountRepository<T> accountRepository;

    public boolean isDuplicatedUser(String username) {
        return accountRepository.existsByUsername(username);
    }

    public T join(T account) {
        verifyAccount(account);
        return accountRepository.save(account);
    }

    /**
     * Do call this method before #join
     * @TODO : Refactor this method
     *   * 코드 중복 회피, 상속된 클래스에서 부모 메소드 사용하도록.
     */
    protected void verifyAccount(T account) {
        String username = account.getUsername();

        if (isDuplicatedUser(username))
            throw new DuplicateKeyException(String.format("Duplicated username { username : \"%s\" }", username));
    }
}
