package kr.makeappsgreat.onlinemall.order.cart;

import com.fasterxml.jackson.databind.ObjectMapper;
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

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;

    private ObjectMapper objectMapper;
    private List<Product> products;

    @BeforeEach
    void beforeEach() {
        objectMapper = new ObjectMapper();

        products = new ArrayList<>();
        productRepository.findByNameContainingIgnoreCase("프리머스", Pageable.unpaged())
                .stream().sorted(Comparator.comparingInt(Product::getPrice))
                .iterator().forEachRemaining(products::add);
        assertThat(products).hasSizeGreaterThan(1);
    }

    @Test
    @WithUserDetails
    void addToCart() throws Exception {
        // Given
        Map<String, Object> body = new HashMap<>();
        Map<String, Object> option = new HashMap<>();

        option.put("product", products.get(1).getId());
        option.put("quantity", 1);

        body.put("product", products.get(0).getId());
        body.put("quantity", 2);
        body.put("options", List.of(option));

        // When & Then
        mockMvc.perform(post("/cart/add")
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(body))
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

}