package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.user.AccountRepository;
import kr.makeappsgreat.onlinemall.user.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends AccountService<Member> {

    private final AgreementRepository agreementRepository;

    @Autowired
    public MemberService(AccountRepository<Member> accountRepository,
                         PasswordEncoder passwordEncoder,
                         AgreementRepository agreementRepository) {
        super(accountRepository, passwordEncoder);
        this.agreementRepository = agreementRepository;
    }

    @Override
    public Member join(Member member) {
        Agreement agreement = member.getAgreement();
        if (agreement == null) throw new NullPointerException("Unexpected usage");
        member.adaptToAccount(passwordEncoder);

        verifyAccount(member);

        agreementRepository.save(agreement);
        return accountRepository.save(member);
    }
}
