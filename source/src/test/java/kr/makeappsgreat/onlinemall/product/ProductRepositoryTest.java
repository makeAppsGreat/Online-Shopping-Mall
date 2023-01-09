package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import org.assertj.core.api.Condition;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.jdbc.Sql;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.anyOf;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    /**
     * @TODO Do test removing option of product and check query.
     */

    @Autowired
    private ProductRepository productRepository;

    @BeforeAll
    @Sql("classpath:data-h2.sql")
    static void saveTestProducts() { }

    private Product savedProduct;

    @BeforeEach
    void getTestProduct() {
        // Given
        Page<Product> one = productRepository.findAll(Pageable.ofSize(1));
        one.forEach(p -> savedProduct = p);
    }

    @Nested
    class Create {

        private ModelMapper modelMapper = new ApplicationConfig().modelMapper();

        @Test
        void save() {
            // Given
            Product product = createProduct("시제품");
            long count = productRepository.count();
            LocalDateTime start = LocalDateTime.now();

            // When
            Product savedProduct = productRepository.save(product);

            // Then
            assertThat(savedProduct).isNotNull();
            assertThat(productRepository.count() - count).isEqualTo(1L);
            assertThat(savedProduct.getRegisteredDate()).isAfter(start);
        }

        private Product createProduct(String name) {
            Map product = new HashMap();
            product.put("name", name);
            product.put("price", 19_800);
            product.put("manufacturer", savedProduct.getManufacturer());
            product.put("category", savedProduct.getCategory());

            return modelMapper.map(product, Product.class);
        }
    }

    @Nested
    class Retrieve {

        @Test
        void findByManufacturer() {
            // When
            Page<Product> product = productRepository.findByManufacturer(
                    savedProduct.getManufacturer(),
                    Pageable.ofSize(1));

            // Then
            product.forEach(p -> assertThat(p.getManufacturer()).isEqualTo(savedProduct.getManufacturer()));
        }

        @Test
        void findByCategory() {
            // When
            Page<Product> product = productRepository.findByCategory(
                    savedProduct.getCategory(),
                    Pageable.ofSize(1));

            // Then
            product.forEach(p -> assertThat(p.getCategory()).isEqualTo(savedProduct.getCategory()));
        }

        @Test
        void findByManufacturerAndCategory() {
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
            product.forEach(p -> assertThat(p.getName()).contains(keyword));
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
                assertThat(p).is(anyOf(
                        new Condition<>((__p) -> __p.getName().contains(keyword), ""),
                        new Condition<>((__p) -> __p.getSimpleDetail().contains(keyword), "")
                ));
            });
        }
    }
}