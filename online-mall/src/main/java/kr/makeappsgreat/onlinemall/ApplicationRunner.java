package kr.makeappsgreat.onlinemall;

import kr.makeappsgreat.onlinemall.product.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApplicationRunner implements org.springframework.boot.ApplicationRunner {

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;

    private final String ROOT_PATH_OF_ASSETS = "/assets/product/";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Manufacturer m1 = new Manufacturer();
        m1.setName("MAFRA");
        manufacturerRepository.save(m1);

        Manufacturer m2 = new Manufacturer();
        m2.setName("Pro Staff");
        manufacturerRepository.save(m2);

        Manufacturer m3 = new Manufacturer();
        m3.setName("MAROLEX");
        manufacturerRepository.save(m3);

        // 프리워시, 본세차, LSP, 세차용품
        Category c1 = new Category();
        c1.setName("프리워시");
        categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setName("본세차");
        categoryRepository.save(c2);

        Category c3 = new Category();
        c3.setName("LSP");
        categoryRepository.save(c3);

        Category c4 = new Category();
        c4.setName("세차용품");
        categoryRepository.save(c4);

        Product p1 = Product.builder()
                .name("라보코스메티카 프리머스 1000mL")
                .price(28_000)
                .manufacturer(m1)
                .category(c1)
                .imageLink(ROOT_PATH_OF_ASSETS + "b0d9d2e0.jpeg")
                .simpleDetail("약알칼리성 프리워시, 스프레이/폼건 가용가능")
                .build();
        productRepository.save(p1);

        Product p2 = Product.builder()
                .name("라보코스메티카 프리머스 4.5L")
                .price(98_000)
                .manufacturer(m1)
                .category(c1)
                .imageLink(ROOT_PATH_OF_ASSETS + "03e4d408.jpeg")
                .simpleDetail("약알칼리성 프리워시, 스프레이/폼건 가용가능")
                .build();
        productRepository.save(p2);

        Product p3 = Product.builder()
                .name("마프라 휠앤타이어클리너 400mL")
                .price(14_000)
                .manufacturer(m1)
                .category(c2)
                .imageLink(ROOT_PATH_OF_ASSETS + "01d61812.jpeg")
                .simpleDetail("변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거")
                .build();
        productRepository.save(p3);

        Product p4 = Product.builder()
                .name("마프라 휠앤타이어클리너 4.5L")
                .price(74_000)
                .manufacturer(m1)
                .category(c2)
                .simpleDetail("변색된 타이어를 본연의 색으로, 터치 없이 휠 분진 제거")
                .build();
        productRepository.save(p4);

        Product p5 = Product.builder()
                .name("라보코스메티카 퓨리피카 1000mL")
                .price(40_000)
                .manufacturer(m1)
                .category(c1)
                .imageLink(ROOT_PATH_OF_ASSETS + "fd00b760.jpeg")
                .simpleDetail("석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸")
                .build();
        productRepository.save(p5);

        Product p6 = Product.builder()
                .name("라보코스메티카 퓨리피카 4.5L")
                .price(108_000)
                .manufacturer(m1)
                .category(c1)
                .imageLink(ROOT_PATH_OF_ASSETS + "006b50b8.jpeg")
                .simpleDetail("석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸")
                .build();
        productRepository.save(p6);

        Product p7 = Product.builder()
                .name("라보코스메티카 셈퍼 500mL")
                .price(30_000)
                .manufacturer(m1)
                .category(c2)
                .imageLink(ROOT_PATH_OF_ASSETS + "06f36c22.jpeg")
                .simpleDetail("희석비 깡패, 뛰어난 윤할력의 중성 카 샴푸")
                .build();
        productRepository.save(p7);

        Product p8 = Product.builder()
                .name("라보코스메티카 비트레오 250g")
                .price(38_000)
                .manufacturer(m1)
                .category(c2)
                .imageLink(ROOT_PATH_OF_ASSETS + "80e5387c.jpeg")
                .simpleDetail("라보코스메티카 유막제거+글라스 폴리쉬")
                .build();
        productRepository.save(p8);

        Product p9 = Product.builder()
                .name("흑광 고체왁스 180g")
                .price(24_900)
                .manufacturer(m2)
                .category(c3)
                .imageLink(ROOT_PATH_OF_ASSETS + "42bded84.jpeg")
                .simpleDetail("차량 본연의 색을 더욱 깊고 진하게!, 블랙 계열 차량 전용 왁스")
                .build();
        productRepository.save(p9);


        Product p10 = Product.builder()
                .name("백광 고체왁스 200g")
                .price(24_900)
                .manufacturer(m2)
                .category(c3)
                .imageLink(ROOT_PATH_OF_ASSETS + "45577e20.jpeg")
                .simpleDetail("차량 본연의 색을 더욱 깊고 진하게!, 화이트 계열 차량 전용 왁스")
                .build();
        productRepository.save(p10);

        Product p11 = Product.builder()
                .name("CC 워터골드 480mL")
                .price(29_000)
                .manufacturer(m2)
                .category(c3)
                .imageLink(ROOT_PATH_OF_ASSETS + "489a760a.jpeg")
                .simpleDetail("젖은 상태 OK!, 광택/코팅/발수, 연마제 無")
                .build();
        productRepository.save(p11);

        Product p12 = Product.builder()
                .name("인더스트리 에르고 EPDM 3000")
                .price(41_000)
                .manufacturer(m3)
                .category(c4)
                .imageLink(ROOT_PATH_OF_ASSETS + "dfe75a2e.png")
                .simpleDetail("마로렉스 알칼리 내성 압축분무기")
                .build();
        productRepository.save(p12);

        p1.getOptions().add(p2);
        p3.getOptions().add(p4);
        p5.getOptions().add(p6);

        log.info("Products saved... Manufacturer : {}, Category : {}, Product : {}",
                manufacturerRepository.count(),
                categoryRepository.count(),
                productRepository.count());
    }
}
