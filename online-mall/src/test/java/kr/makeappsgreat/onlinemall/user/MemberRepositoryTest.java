package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.model.Address;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataAccessException;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;

@DataJpaTest
class MemberRepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Nested
    class Save{
        @Test
        void save() {
            // Given
            Member member = createAMember();

            // When
            Member savedMember = memberRepository.save(member);

            // Then
            assertThat(member.getName()).isEqualTo(savedMember.getName());
            assertThat(member.getUsername()).isEqualTo(savedMember.getUsername());
            assertThat(member.getPhoneNumber()).isEqualTo(savedMember.getPhoneNumber());
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
            memberRepository.save(createAMember());
        }

        @Test
        void findAll() {
            // Given & When
            List<Member> all = memberRepository.findAll();

            // Then
            assertThat(all.size()).isGreaterThan(0);
        }

    }

    private Member createAMember() {
        return Member.builder()
                .name("김가연")
                .username("makeappsgreat@gmail.com")
                .password("simple")
                .address(Address.builder()
                        .zipcode("42731")
                        .address("대구광역시 달서구")
                        .address2("")
                        .build())
                .phoneNumber("053-123-4567")
                .mobileNumber("010-1234-5678")
                .build();
    }
}