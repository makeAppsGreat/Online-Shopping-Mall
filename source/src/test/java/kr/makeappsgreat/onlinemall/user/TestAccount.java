package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import org.modelmapper.ModelMapper;

class TestAccount {

    private static ModelMapper modelMapper = new ApplicationConfig().modelMapper();

    public static Account get() { return get("김가연", "testaccount"); }
    public static Account get(String name, String username) {
        AccountRequest request = new AccountRequest();
        request.setName(name);
        request.setUsername(username);
        request.setPassword("simple");

        Account account = modelMapper.map(request, Account.class);
        account.addRole(AccountRole.ROLE_USER);

        return account;
    }
}
