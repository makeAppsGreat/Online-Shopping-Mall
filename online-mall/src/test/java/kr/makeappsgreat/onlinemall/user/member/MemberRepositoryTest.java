package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.user.AccountRepository;
import kr.makeappsgreat.onlinemall.user.AccountRole;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    AccountRepository<Member> memberRepository;

    @Autowired
    AgreementRepository agreementRepository;

    @Mock
    PasswordEncoder passwordEncoder;

    private final ModelMapper modelMapper = ApplicationConfig.myModelMapper();

    @Nested
    class Save{

        @Test
        void save() {
            // Given
            Member member = getTestMember();

            // When
            agreementRepository.save(member.getAgreement());
            Member savedMember = memberRepository.save(member);
            memberRepository.findAll();

            // Then
            assertThat(member.getName()).isEqualTo(savedMember.getName());
            assertThat(member.getUsername()).isEqualTo(savedMember.getUsername());
            assertThat(member.getPhoneNumber()).isEqualTo(savedMember.getPhoneNumber());
            assertThat(member.getId()).isEqualTo(member.getAgreement().getId());
        }

        @Test
        void saveDuplicatedEmail() {
            // Given
            Member member = getTestMember();
            Member member2 = Member.builder()
                    .name("김나연")
                    .email(username)
                    .password("simple")
                    .address(Address.builder()
                            .zipcode("42700")
                            .address("대구광역시 달서구")
                            .build())
                    .mobileNumber("010-5678-9000")
                    .build();
            AgreementRequest request = new AgreementRequest();
            request.setTerms1(true);
            request.setTerms2(true);
            request.setTerms3(true);
            request.setMarketing(true);
            Agreement agreement = modelMapper.map(request, Agreement.class);
            member2.setAgreement(agreement);
            member2.foo(passwordEncoder);

            // When & Then
            assertThatExceptionOfType(DataAccessException.class)
                    .isThrownBy(() -> {
                        agreementRepository.save(member.getAgreement());
                        memberRepository.save(member);

                        agreementRepository.save(member2.getAgreement());
                        memberRepository.save(member2);

                        memberRepository.findAll();
                    });
        }

    }

    @Nested
    class Retrieve {

        @BeforeEach
        void saveTestMember() {
            Member member = getTestMember();

            agreementRepository.deleteAll();
            memberRepository.deleteAll();

            agreementRepository.save(member.getAgreement());
            memberRepository.save(member);
        }

        @Test
        void findAll() {
            // Given & When
            List<Member> all = memberRepository.findAll();

            // Then
            assertThat(all.size()).isGreaterThan(0);
            assertThat(all).allSatisfy((member) -> {
               assertThat(member.getRoles()).containsAnyOf(AccountRole.ROLE_USER, AccountRole.ROLE_ADMIN);
            });
        }

        @Test
        void findByUsername() {
            // When
            Optional<Member> member = memberRepository.findByUsername(username);

            // Then
            assertThat(member.isPresent()).isTrue();
        }

    }

    /** Wrap method for mocking */
    private Member getTestMember() {
        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0, String.class));

        Member member = MemberTest.getTestMember(modelMapper);
        member.foo(passwordEncoder);

        return member;
    }
    private final String username = MemberTest.getUsername();
}