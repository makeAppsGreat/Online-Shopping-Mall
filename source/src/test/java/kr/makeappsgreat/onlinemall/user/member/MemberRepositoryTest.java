package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.config.MyModelMapper;
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
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.dao.DataAccessException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@DataJpaTest(includeFilters = @ComponentScan.Filter(classes = MyModelMapper.class, type = FilterType.ASSIGNABLE_TYPE))
class MemberRepositoryTest {

    @Autowired
    AccountRepository<Member> memberRepository;

    @Autowired
    AgreementRepository agreementRepository;

    @Autowired
    ModelMapper modelMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @Nested
    class Save{

        @Test
        void save() {
            // Given
            Member member = createTestMember();

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
            Member member = createTestMember();
            String duplicatedEmail = member.getEmail();

            Address address = new Address();
            address.setZipcode("42700");
            address.setAddress("대구광역시 달서구");

            Member member2 = new TestMember(modelMapper, "김나연", duplicatedEmail).getMember();

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

        private String username;

        @BeforeEach
        void saveTestMember() {
            Member member = createTestMember();
            username = member.getUsername();

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
    private Member createTestMember() {
        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0, String.class));

        Member member = new TestMember(modelMapper).getMember();
        member.adaptToAccount(passwordEncoder);

        return member;
    }
}