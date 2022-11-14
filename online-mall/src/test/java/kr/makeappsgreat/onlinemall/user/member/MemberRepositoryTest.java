package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import kr.makeappsgreat.onlinemall.model.Address;
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
    MemberRepository memberRepository;

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
            Member member = createAMember();

            // When
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
            // When & Then
            assertThatExceptionOfType(DataAccessException.class).isThrownBy(() -> {
                memberRepository.saveAll(List.of(createAMember(), createAMember()));
                memberRepository.findAll();
            });
        }

    }

    @Nested
    class Retrieve {

        @BeforeEach
        void saveTestMember() {
            memberRepository.deleteAll();
            memberRepository.save(createAMember());
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

    private final String username = "makeappsgreat@gmail.com";
    private Member createAMember() {
        when(passwordEncoder.encode(anyString()))
                .thenAnswer(invocation -> invocation.getArgument(0, String.class));

        AgreementRequest request = AgreementRequest.builder()
                .terms1(true)
                .terms2(true)
                .terms3(true)
                .marketing(false)
                .build();
        Agreement agreement = modelMapper.map(request, Agreement.class);

        Member member = Member.builder()
                .name("김가연")
                .email(username)
                .password("simple")
                .agreement(agreement)
                .address(Address.builder()
                        .zipcode("42731")
                        .address("대구광역시 달서구")
                        .build())
                .phoneNumber("053-123-4567")
                .mobileNumber("010-1234-5678")
                .build()
                .foo(passwordEncoder);

        agreement.setMember(member);
        agreementRepository.save(agreement);


        return member;
    }
}