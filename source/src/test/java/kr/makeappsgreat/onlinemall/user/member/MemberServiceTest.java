package kr.makeappsgreat.onlinemall.user.member;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@SpringBootTest
class MemberServiceTest {

    @Autowired
    MemberService memberService;

    @Autowired
    ModelMapper modelMapper;

    @Test
    void join() {
        Member member = TestMember.getTestMember(modelMapper);
        memberService.join(member);
    }

    @Test
    void join_withNullAgreement_Fail() {
        Member member = new Member();

        assertThatExceptionOfType(NullPointerException.class)
                .isThrownBy(() -> memberService.join(member))
                .withNoCause()
                .withMessage("Unexpected usage");
    }
}