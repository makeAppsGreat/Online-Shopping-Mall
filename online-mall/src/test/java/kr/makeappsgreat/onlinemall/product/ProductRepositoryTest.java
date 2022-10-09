package kr.makeappsgreat.onlinemall.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

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

    @BeforeEach
    void deleteAll() {
        productRepository.deleteAll();
    }

    @Test
    void selectAll() {
        saveTestProducts();

        List<Product> all = productRepository.findAll();
        all.forEach(System.out::println);
    }

    private void saveTestProducts() {
        Manufacturer m1 = new Manufacturer();
        m1.setName("MAFRA");
        manufacturerRepository.save(m1);

        Category c1 = new Category();
        c1.setName("프리워시/APC");
        categoryRepository.save(c1);

        Category c2 = new Category();
        c2.setName("휠&타이어");
        categoryRepository.save(c2);

        Product p1 = Product.builder()
                .name("라보코스메티카 프리머스")
                .price(28_000)
                .manufacturer(m1)
                .category(c1)
                .build();
        productRepository.save(p1);

        Product p2 = Product.builder()
                .name("마프라 휠앤타이어클리너 400mL")
                .price(14_000)
                .manufacturer(m1)
                .category(c2)
                .build();
        productRepository.save(p2);

        Product p3 = Product.builder()
                .name("마프라 휠앤타이어클리너 4.5L")
                .price(74_000)
                .manufacturer(m1)
                .category(c2)
                .build();
        productRepository.save(p3);

        p2.getOptions().add(p3);
    }

}