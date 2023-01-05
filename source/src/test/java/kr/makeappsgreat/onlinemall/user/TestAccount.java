package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import kr.makeappsgreat.onlinemall.config.SecurityConfig;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;

class TestAccount {

    private static PasswordEncoder passwordEncoder = new SecurityConfig().passwordEncoder();
    private static ModelMapper modelMapper = new ApplicationConfig().modelMapper();

    public static Account getWithNotEncodedPassword() { return get("김가연", "account", false); }
    public static Account get() { return get("김가연", "account", true); }
    public static Account get(String name, String username) { return get(name, username, true); }

    private static Account get(String name, String username, boolean encodePassword) {
        AccountRequest request = new AccountRequest();
        request.setName(name);
        request.setUsername(username);
        request.setPassword("simple");

        Account account = modelMapper.map(request, Account.class);
        if (encodePassword) account.encodePassword(passwordEncoder);

        return account;
    }
}
