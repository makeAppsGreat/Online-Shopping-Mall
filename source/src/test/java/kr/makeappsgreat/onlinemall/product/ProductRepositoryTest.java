package kr.makeappsgreat.onlinemall.product;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@TestMethodOrder(MethodOrderer.DisplayName.class)
class ProductRepositoryTest {

    /**
     * @TODO Do test removing option of product and check query.
     */

    @Autowired
    ProductRepository productRepository;
    
    private Product savedProduct;

    @BeforeAll
    @Sql("classpath:data-h2.sql")
    static void saveTestProducts() { }

    @BeforeEach
    void getTestProduct() {
        // Given
        Page<Product> one = productRepository.findAll(Pageable.ofSize(1));
        one.forEach(p -> savedProduct = p);
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
        Page<Product> product = productRepository.findByNameContainingIgnoreCase(keyword, Pageable.ofSize(1));

        // Then
        product.forEach(p -> assertThat(p.getName().contains(keyword)).isTrue());
    }

    @Test
    void findByNameContainingOrSimpleDetailContaining() {
        // Given
        String keyword = "샴푸";

        // When
        Page<Product> product = productRepository.findByNameContainingIgnoreCaseOrSimpleDetailContainingIgnoreCase(
                null,
                keyword,
                Pageable.ofSize(1));

        // Then
        product.forEach(p -> {
            assertThat(p.getName().contains(keyword) || p.getSimpleDetail().contains(keyword)).isTrue();
        });
    }

    /**
     * @TODO Need to validate keyword from controller
     */
    @Test
    void findByNameContainingOrSimpleDetailContaining_blank() {
        // Given
        String keyword = "";

        // When
        Page<Product> product = productRepository.findByNameContainingIgnoreCaseOrSimpleDetailContainingIgnoreCase(
                keyword,
                keyword,
                Pageable.ofSize(10));

        // Then
        product.forEach(p -> {
            assertThat(p.getName().contains(keyword) || p.getSimpleDetail().contains(keyword)).isTrue();
        });
    }
}