package kr.makeappsgreat.onlinemall.user;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class AccountServiceTest {

    @Autowired
    AccountService accountService;

    @Test
    public void loadUserByUsername() {
        // Given
        String username = "makeappsgreat@gmail.com";

        // When
        UserDetails userDetails = accountService.loadUserByUsername(username);

        // Then
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(userDetails.getAuthorities()).hasSizeGreaterThan(0);
    }
}