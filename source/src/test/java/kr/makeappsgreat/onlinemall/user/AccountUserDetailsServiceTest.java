package kr.makeappsgreat.onlinemall.user;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = AccountUserDetailsService.class)
class AccountUserDetailsServiceTest {

    @Autowired
    private AccountUserDetailsService userDetailsService;

    @MockBean
    private AccountRepository<Account> accountRepository;

    private Account account;

    @BeforeEach
    void mock() {
        account = TestAccount.get();
        account.addRole(AccountRole.ROLE_USER);

        given(accountRepository.findByUsername(account.getUsername())).willReturn(Optional.of(account));
    }

    @Test
    void loadUserByUsername() {
        // Given
        String username = account.getUsername();

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getAuthorities()).hasSizeGreaterThan(0);
    }
}