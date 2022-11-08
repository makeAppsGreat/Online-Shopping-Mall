package kr.makeappsgreat.onlinemall;

import kr.makeappsgreat.onlinemall.model.Address;
import kr.makeappsgreat.onlinemall.product.*;
import kr.makeappsgreat.onlinemall.user.member.Agreement;
import kr.makeappsgreat.onlinemall.user.member.AgreementRepository;
import kr.makeappsgreat.onlinemall.user.member.Member;
import kr.makeappsgreat.onlinemall.user.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.ApplicationArguments;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;
    private final MemberRepository memberRepository;
    private final AgreementRepository agreementRepository;

    private final PasswordEncoder passwordEncoder;

    private static final String ROOT_PATH_OF_IMAGES = "/images/product/";

    @Transactional
    @Override
    public void run(ApplicationArguments args) {
        List<Manufacturer> manufacturers = List.of(
                Manufacturer.of("MAFRA"),
                Manufacturer.of("Pro Staff"),
                Manufacturer.of("MAROLEX")
        );
        manufacturerRepository.saveAll(manufacturers);

        // 프리워시, 본세차, LSP, 세차용품
        List<Category> categories = List.of(
                Category.of("프리워시"),
                Category.of("본세차"),
                Category.of("LSP"),
                Category.of("세차용품")
        );
        categoryRepository.saveAll(categories);

        List<Product> products = new ArrayList<>();
        products.add(Product.builder()
                .name("라보코스메티카 프리머스 1000mL")
                .price(28_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(0))
                .imageLink(ROOT_PATH_OF_IMAGES + "b0d9d2e0.jpeg")
                .simpleDetail("약알칼리성 프리워시, 스프레이/폼건 가용가능")
                .build());

        products.add(Product.builder()
                .name("라보코스메티카 프리머스 4.5L")
                .price(98_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(0))
                .imageLink(ROOT_PATH_OF_IMAGES + "03e4d408.jpeg")
                .simpleDetail("약알칼리성 프리워시, 스프레이/폼건 가용가능")
                .build());

        products.add(Product.builder()
                .name("마프라 휠앤타이어클리너 400mL")
                .price(14_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(1))
                .imageLink(ROOT_PATH_OF_IMAGES + "01d61812.jpeg")
                .simpleDetail("변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거")
                .build());

        products.add(Product.builder()
                .name("마프라 휠앤타이어클리너 4.5L")
                .price(74_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(1))
                .simpleDetail("변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거")
                .build());

        products.add(Product.builder()
                .name("라보코스메티카 퓨리피카 1000mL")
                .price(40_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(0))
                .imageLink(ROOT_PATH_OF_IMAGES + "fd00b760.jpeg")
                .simpleDetail("석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸")
                .build());

        products.add(Product.builder()
                .name("라보코스메티카 퓨리피카 4.5L")
                .price(108_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(0))
                .imageLink(ROOT_PATH_OF_IMAGES + "006b50b8.jpeg")
                .simpleDetail("석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸")
                .build());

        products.add(Product.builder()
                .name("라보코스메티카 셈퍼 500mL")
                .price(30_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(1))
                .imageLink(ROOT_PATH_OF_IMAGES + "06f36c22.jpeg")
                .simpleDetail("희석비 깡패, 뛰어난 윤할력의 중성 카 샴푸")
                .build());

        products.add(Product.builder()
                .name("라보코스메티카 비트레오 250g")
                .price(38_000)
                .manufacturer(manufacturers.get(0))
                .category(categories.get(1))
                .imageLink(ROOT_PATH_OF_IMAGES + "80e5387c.jpeg")
                .simpleDetail("라보코스메티카 유막제거+글라스 폴리쉬")
                .build());

        products.add(Product.builder()
                .name("흑광 고체왁스 180g")
                .price(24_900)
                .manufacturer(manufacturers.get(1))
                .category(categories.get(2))
                .imageLink(ROOT_PATH_OF_IMAGES + "42bded84.jpeg")
                .simpleDetail("차량 본연의 색을 더욱 깊고 진하게!, 블랙 계열 차량 전용 왁스")
                .build());

        products.add(Product.builder()
                .name("백광 고체왁스 200g")
                .price(24_900)
                .manufacturer(manufacturers.get(1))
                .category(categories.get(2))
                .imageLink(ROOT_PATH_OF_IMAGES + "45577e20.jpeg")
                .simpleDetail("차량 본연의 색을 더욱 깊고 진하게!, 화이트 계열 차량 전용 왁스")
                .build());

        products.add(Product.builder()
                .name("CC 워터골드 480mL")
                .price(29_000)
                .manufacturer(manufacturers.get(1))
                .category(categories.get(2))
                .imageLink(ROOT_PATH_OF_IMAGES + "489a760a.jpeg")
                .simpleDetail("젖은 상태 OK!, 광택/코팅/발수, 연마제 無")
                .build());

        products.add(Product.builder()
                .name("인더스트리 에르고 EPDM 3000")
                .price(41_000)
                .manufacturer(manufacturers.get(2))
                .category(categories.get(3))
                .imageLink(ROOT_PATH_OF_IMAGES + "dfe75a2e.png")
                .simpleDetail("마로렉스 알칼리 내성 압축분무기")
                .build());

        productRepository.saveAll(products);

        products.get(0).getOptions().add(products.get(1));
        products.get(2).getOptions().add(products.get(3));
        products.get(4).getOptions().add(products.get(5));


        log.info("Products saved... (Manufacturer : {}, Category : {}, Product : {})",
                manufacturerRepository.count(),
                categoryRepository.count(),
                productRepository.count());


        List<Member> members = new ArrayList<>();

        Agreement agreement = Agreement.builder()
                .terms1(true)
                .terms2(true)
                .terms3(true)
                .build();
        agreement.updateMarketingAgreement(false);

        members.add(Member.builder()
                .name("김가연")
                .email("makeappsgreat@gmail.com")
                .password("simple")
                .agreement(agreement)
                .address(Address.builder()
                        .zipcode("42731")
                        .address("대구광역시 달서구")
                        .build())
                .phoneNumber("053-123-4567")
                .mobileNumber("010-1234-5678")
                .build()
                .foo(passwordEncoder));

        agreement.setMember(members.get(0));
        agreementRepository.save(agreement);


        memberRepository.saveAll(members);
        log.info("Members saved... (Member : {})", memberRepository.count());
    }
}
