package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.user.Account;
import kr.makeappsgreat.onlinemall.user.AccountRepository;
import kr.makeappsgreat.onlinemall.user.AccountRole;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    private AccountRepository<Member> memberRepository;

    @Autowired
    private AgreementRepository agreementRepository;

    @Nested
    class Create {

        @Test
        void save() {
            // Given
            Member member = TestMember.get();
            long agreementCount = agreementRepository.count();
            long memberCount = memberRepository.count();

            // When
            agreementRepository.save(member.getAgreement());
            Member savedMember = memberRepository.save(member);

            // Then
            assertThat(agreementRepository.count() - agreementCount).isEqualTo(1L);
            assertThat(memberRepository.count() - memberCount).isEqualTo(1L);
            assertThat(savedMember).isNotNull();
            assertThat(savedMember.getUsername()).isNotNull();
            assertThat(savedMember.getPassword()).containsPattern(Account.PASSWORD_REGEXP);
            assertThat(savedMember.getRoles()).contains(AccountRole.ROLE_USER);
        }
    }
}