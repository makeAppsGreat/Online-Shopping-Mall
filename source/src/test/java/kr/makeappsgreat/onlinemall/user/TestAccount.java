package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.config.MyModelMapper;
import org.modelmapper.ModelMapper;

class TestAccount {

    private static ModelMapper modelMapper = new MyModelMapper();

    public static Account get() {
        AccountRequest request = new AccountRequest();
        request.setName("김가연");
        request.setUsername("testaccount");
        request.setPassword("simple");

        Account account = modelMapper.map(request, Account.class);
        account.addRole(AccountRole.ROLE_USER);

        return account;
    }
}
