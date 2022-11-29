package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.user.AccountRepository;
import kr.makeappsgreat.onlinemall.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends AccountService<Member> {

    private final AgreementRepository agreementRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public MemberService(AccountRepository<Member> accountRepository,
                         AgreementRepository agreementRepository,
                         PasswordEncoder passwordEncoder) {
        super(accountRepository);
        this.agreementRepository = agreementRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Member join(Member account) {
        if (account.getAgreement() == null) throw new NullPointerException();

        account.foo(passwordEncoder);

        Agreement savedAgreement = agreementRepository.save(account.getAgreement());
        account.setAgreement(savedAgreement);


        return super.join(account);
    }
}
