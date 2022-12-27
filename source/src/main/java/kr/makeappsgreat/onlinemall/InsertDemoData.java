package kr.makeappsgreat.onlinemall;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.product.CategoryRepository;
import kr.makeappsgreat.onlinemall.product.ManufacturerRepository;
import kr.makeappsgreat.onlinemall.product.ProductRepository;
import kr.makeappsgreat.onlinemall.user.AccountRole;
import kr.makeappsgreat.onlinemall.user.member.Agreement;
import kr.makeappsgreat.onlinemall.user.member.AgreementRequest;
import kr.makeappsgreat.onlinemall.user.member.Member;
import kr.makeappsgreat.onlinemall.user.member.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class InsertDemoData implements ApplicationRunner {

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MemberService memberService;

    private final ModelMapper modelMapper;

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        log.info("Products saved... (Manufacturer : {}, Category : {}, Product : {})",
                manufacturerRepository.count(),
                categoryRepository.count(),
                productRepository.count());


        List<Member> members = new ArrayList<>();

        AgreementRequest request = new AgreementRequest();
        request.setTerms1(true);
        request.setTerms2(true);
        request.setTerms3(true);
        request.setMarketing(false);
        Agreement agreement = modelMapper.map(request, Agreement.class);

        Address address = new Address();
        address.setZipcode("42700");
        address.setAddress("대구광역시 달서구");

        Member member = Member.builder()
                .name("김가연")
                .email("makeappsgreat@gmail.com")
                .password("simple")
                .address(address)
                .phoneNumber("053-123-4567")
                .mobileNumber("010-1234-5678")
                .build();
        member.setAgreement(agreement);
        member.addRole(AccountRole.ROLE_ADMIN);
        members.add(member);


        members.forEach(m -> memberService.join(m));
        log.info("Members saved... (Member : {})", members.size());
    }
}
