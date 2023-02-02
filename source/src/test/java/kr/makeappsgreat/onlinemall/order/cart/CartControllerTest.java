package kr.makeappsgreat.onlinemall.order.cart;

import kr.makeappsgreat.onlinemall.product.Product;
import kr.makeappsgreat.onlinemall.product.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;

    private List<Product> products;

    @BeforeEach
    void beforeEach() {
        products = new ArrayList<>();
        productRepository.findByNameContainingIgnoreCase("프리머스", Pageable.unpaged())
                .stream().sorted(Comparator.comparingInt(Product::getPrice))
                .iterator().forEachRemaining(products::add);
        assertThat(products).hasSizeGreaterThan(1);
    }

    @Test
    @WithUserDetails
    void addToCart() throws Exception {
        mockMvc.perform(post("/cart/add")
                        .param("product", products.get(0).getId().toString())
                        .param("quantity", "2")
                        .with(csrf())
                )
                .andDo(print())
                .andExpect(status().isOk());
    }

}