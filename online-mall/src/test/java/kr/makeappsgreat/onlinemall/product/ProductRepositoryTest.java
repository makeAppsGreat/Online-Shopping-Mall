package kr.makeappsgreat.onlinemall.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    /**
     * @TODO : Do test removing option of product and check query.
     */

    @Autowired
    ManufacturerRepository manufacturerRepository;

    @Autowired
    CategoryRepository categoryRepository;

    @Autowired
    ProductRepository productRepository;
    
    private Product savedProduct;

    @BeforeEach
    void saveTestProductS() {
        Manufacturer m1 = new Manufacturer();
        m1.setName("MAFRA");
        manufacturerRepository.save(m1);

        // 프리워시, 본세차, LSP, 세차용품
        Category c1 = new Category();
        c1.setName("프리워시");
        categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setName("본세차");
        categoryRepository.save(c2);

        Product p1 = Product.builder()
                .name("라보코스메티카 프리머스 1000mL")
                .price(28_000)
                .manufacturer(m1)
                .category(c1)
                .simpleDetail("약알칼리성 프리워시, 스프레이/폼건 가용가능")
                .build();
        productRepository.save(p1);

        Product p2 = Product.builder()
                .name("라보코스메티카 프리머스 4.5L")
                .price(98_000)
                .manufacturer(m1)
                .category(c1)
                .simpleDetail("약알칼리성 프리워시, 스프레이/폼건 가용가능")
                .build();
        productRepository.save(p2);

        Product p3 = Product.builder()
                .name("마프라 휠앤타이어클리너 400mL")
                .price(14_000)
                .manufacturer(m1)
                .category(c2)
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
                .simpleDetail("석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸")
                .build();
        productRepository.save(p5);

        Product p6 = Product.builder()
                .name("라보코스메티카 퓨리피카 4.5L")
                .price(108_000)
                .manufacturer(m1)
                .category(c1)
                .simpleDetail("석회 및 물때만을 제거하여 무너진 비딩 재생 탁월한 산성 카 샴푸")
                .build();
        productRepository.save(p6);

        Product p7 = Product.builder()
                .name("라보코스메티카 셈퍼 500mL")
                .price(30_000)
                .manufacturer(m1)
                .category(c2)
                .simpleDetail("희석비 깡패, 뛰어난 윤할력의 중성 카 샴푸")
                .build();
        productRepository.save(p7);

        Product p8 = Product.builder()
                .name("라보코스메티카 비트레오 250g")
                .price(38_000)
                .manufacturer(m1)
                .category(c2)
                .simpleDetail("라보코스메티카 유막제거+글라스 폴리쉬")
                .build();
        productRepository.save(p8);

        p1.getOptions().add(p2);
        p3.getOptions().add(p4);
        p5.getOptions().add(p6);

        // Given
        Page<Product> one = productRepository.findAll(Pageable.ofSize(1));
        one.forEach(p -> savedProduct = p);
    }

    @Test @Disabled
    void selectAll() {
        // Then
        List<Product> all = productRepository.findAll();
        all.forEach(System.out::println);
    }

    @Test
    void findByManufacturer() throws Exception {
        // When
        Page<Product> product = productRepository.findByManufacturer(
                savedProduct.getManufacturer(),
                Pageable.ofSize(1));

        // Then
        product.forEach(p -> assertThat(p.getManufacturer()).isEqualTo(savedProduct.getManufacturer()));
    }

    @Test
    void findByCategory() throws Exception {
        // When
        Page<Product> product = productRepository.findByCategory(
                savedProduct.getCategory(),
                Pageable.ofSize(1));

        // Then
        product.forEach(p -> assertThat(p.getCategory()).isEqualTo(savedProduct.getCategory()));
    }

    @Test
    void findByManufacturerAndCategory() throws Exception {
        // When
        Page<Product> product = productRepository.findByManufacturerAndCategory(
                savedProduct.getManufacturer(),
                savedProduct.getCategory(),
                Pageable.ofSize(1));

        // Then
        product.forEach(p -> {
            assertThat(p.getManufacturer()).isEqualTo(savedProduct.getManufacturer());
            assertThat(p.getCategory()).isEqualTo(savedProduct.getCategory());
        });
    }

    @Test
    void findByNameContaining() {
        // Given
        String keyword = "라보코스메티카";

        // When
        Page<Product> product = productRepository.findByNameContaining(keyword, Pageable.ofSize(1));

        // Then
        product.forEach(p -> assertThat(p.getName().contains(keyword)).isTrue());
    }

    @Test
    void findByNameContainingOrSimpleDetailContaining() {
        // Given
        String keyword = "샴푸";

        // When
        Page<Product> product = productRepository.findByNameContainingOrSimpleDetailContaining(
                null,
                keyword,
                Pageable.ofSize(1));

        // Then
        product.forEach(p -> {
            assertThat(p.getName().contains(keyword) || p.getSimpleDetail().contains(keyword)).isTrue();
        });
    }

    /**
     * Need to validate keyword from controller
     */
    @Test
    void findByNameContainingOrSimpleDetailContaining_blank() {
        // Given
        String keyword = "";

        // When
        Page<Product> product = productRepository.findByNameContainingOrSimpleDetailContaining(
                keyword,
                keyword,
                Pageable.ofSize(10));

        // Then
        product.forEach(p -> {
            assertThat(p.getName().contains(keyword) || p.getSimpleDetail().contains(keyword)).isTrue();
        });
    }
}