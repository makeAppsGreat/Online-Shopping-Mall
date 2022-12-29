package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.config.MyModelMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.validation.ConstraintViolationException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository<Account> accountRepository;

    private PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private ModelMapper modelMapper = new MyModelMapper();

    @Nested
    class Create {

        @Test
        void save() {
            // Given
            Account account = createTestAccount();
            long count = accountRepository.count();
            LocalDateTime start = LocalDateTime.now();

            // When
            System.out.println(account.getPassword());
            Account savedAccount = accountRepository.save(account);

            // Then
            assertThat(savedAccount).isNotNull();
            assertThat(accountRepository.count() - count).isEqualTo(1L);
            assertThat(savedAccount.getRegisteredDate()).isAfter(start);
        }

        @Test
        void save_withNoData_throwException() {
            // Given
            Account account = new Account();

            // When & Then
            assertThatExceptionOfType(ConstraintViolationException.class)
                    .isThrownBy(() -> accountRepository.save(account))
                    .withMessageStartingWith("Validation failed for classes");
        }

    }

    @Nested
    class Retrieve {

        private String username;

        @BeforeEach
        void saveTestAccount() {
            // Given
            Account account = createTestAccount();
            username = account.getUsername();

            accountRepository.deleteAll();
            accountRepository.save(account);
        }

        @Test
        void findAll() {
            // When
            List<Account> all = accountRepository.findAll();

            // Then
            assertThat(all).hasSizeGreaterThan(0);
            assertThat(all).allSatisfy(account -> assertThat(account.getRoles()).containsAnyOf(AccountRole.values()));
        }

        @Test
        void findByUsername(){
            // When
            Optional<Account> account = accountRepository.findByUsername(username);

            // Then
            assertThat(account).isPresent();
        }
    }

    private Account createTestAccount() {
        Account account = TestAccount.get();
        account.encodePassword(passwordEncoder);

        return account;
    }
}