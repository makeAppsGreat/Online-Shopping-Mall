package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.user.AccountRepository;
import kr.makeappsgreat.onlinemall.user.AccountService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class MemberService extends AccountService<Member> {

    private final AgreementRepository agreementRepository;

    @Autowired
    public MemberService(AccountRepository<Member> accountRepository,
                         PasswordEncoder passwordEncoder,
                         MessageSource messageSource,
                         ModelMapper modelMapper,
                         AgreementRepository agreementRepository) {
        super(accountRepository, passwordEncoder, messageSource, modelMapper);
        this.agreementRepository = agreementRepository;
    }

    @Override
    public Member join(Member member) {
        Agreement agreement = member.getAgreement();
        if (agreement == null) throw new NullPointerException("Unexpected usage : Agreement is null.");
        member.adaptToAccount(passwordEncoder);

        verifyAccount(member);

        agreementRepository.save(agreement);
        return accountRepository.save(member);
    }
}
