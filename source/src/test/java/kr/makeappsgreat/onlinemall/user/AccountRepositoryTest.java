package kr.makeappsgreat.onlinemall.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static kr.makeappsgreat.onlinemall.common.Constants.H2_DUPLICATE_KEY;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@DataJpaTest
class AccountRepositoryTest {

    @Autowired
    private AccountRepository<Account> accountRepository;

    @Nested
    class Create {

        @Test
        void save() {
            // Given
            Account account = TestAccount.get();
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
        void save_hasNoData_throwException() {
            // Given
            Account account = new Account();

            // When & Then
            try {
                accountRepository.save(account);
            } catch (ConstraintViolationException e) {
                assertThat(e.getConstraintViolations()).hasSize(3);
            }
        }

        @Test
        void save_duplicatedUsername_throwException() {
            // Given
            Account account = TestAccount.get();
            Account duplicatedAccount = TestAccount.get("김나연", account.getUsername());

            accountRepository.save(account);

            // When & Then
            assertThatExceptionOfType(DataAccessException.class)
                    .isThrownBy(() -> accountRepository.save(duplicatedAccount))
                    .withStackTraceContaining(H2_DUPLICATE_KEY);
        }

        @Test
        void save_notEncodedPassword_throwException() throws NoSuchMethodException {
            // Given
            Account account = TestAccount.get();

            // When & Then
            try {
                accountRepository.save(account);
            } catch (ConstraintViolationException e) {
                Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
                assertThat(violations).hasSize(1);

                ConstraintViolation<?> violation = violations.iterator().next();
                assertThat(violation.getPropertyPath().toString()).isEqualTo("password");
                assertThat(violation.getMessageTemplate()).isEqualTo(
                        Pattern.class.getMethod("message").getDefaultValue()
                );
            }
        }
    }

    @Nested
    class Retrieve {

        private String username;

        @BeforeEach
        void saveTestAccount() {
            // Given
            Account account = TestAccount.get();
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
}