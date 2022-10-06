package kr.makeappsgreat.onlinemall.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductRepository productRepository;

    @BeforeEach
    void deleteAll() {
        productRepository.deleteAll();
    }

    @Test
    void selectAll() {
        saveTestProducts();

        productRepository.findAll();
    }

    private void saveTestProducts() {
        /* Manufacturer m1 = new Manufacturer();
        m1.setName("MAFRA");

        Category c1 = new Category();
        c1.setName("프리워시/APC"); */

        Product p1 = Product.builder()
                .name("라보코스메티카 프리머스")
                .price(28_000)
                // .manufacturer(m1)
                // .category(c1)
                .build();

        productRepository.save(p1);
    }

}