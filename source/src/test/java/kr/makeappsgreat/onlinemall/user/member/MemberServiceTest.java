package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.user.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.AdditionalAnswers.returnsFirstArg;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@SpringBootTest(classes = MemberService.class)
class MemberServiceTest {

    @Autowired
    private MemberService memberService;

    @MockBean
    private PasswordEncoder passwordEncoder;

    @MockBean
    private AccountRepository<Member> accountRepository;

    @MockBean
    private AgreementRepository agreementRepository;

    @SpyBean
    private ModelMapper modelMapper;

    @BeforeEach
    void mock() {
        given(passwordEncoder.encode(anyString())).will(returnsFirstArg());
        given(accountRepository.existsByUsername(anyString())).willReturn(false);
        given(accountRepository.save(any(Member.class))).will(returnsFirstArg());
    }

    @Test
    void join() {
        // When
        Member member = TestMember.getWithNotAdaptToAccount();
        Member joinedMember = memberService.join(member);

        // Then
        assertThat(joinedMember).isNotNull();
    }

    @Test
    void join_nullAgreement_throwException() {
        Member member = new Member();

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> memberService.join(member))
                .withNoCause()
                .withMessage("Unexpected usage : Agreement is null.");
    }
}