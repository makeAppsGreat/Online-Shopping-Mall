package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import kr.makeappsgreat.onlinemall.config.SecurityConfig;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

class TestAccount {

    private static PasswordEncoder passwordEncoder = new SecurityConfig().passwordEncoder();
    private static ModelMapper modelMapper = new ApplicationConfig().modelMapper();

    public static Account get() { return get("김가연", "account"); }
    public static Account get(String name, String username) {
        AccountRequest request = new AccountRequest();
        request.setName(name);
        request.setUsername(username);
        request.setPassword("simple");

        Account account = modelMapper.map(request, Account.class);
        account.encodePassword(passwordEncoder);
        account.addRole(AccountRole.ROLE_USER);
        account.addRole(AccountRole.ROLE_ADMIN);

        return account;
    }

    public static PasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
