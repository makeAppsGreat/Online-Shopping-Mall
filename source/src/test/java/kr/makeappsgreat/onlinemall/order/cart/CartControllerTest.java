package kr.makeappsgreat.onlinemall.order.cart;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import kr.makeappsgreat.onlinemall.product.Product;
import kr.makeappsgreat.onlinemall.product.ProductRepository;
import kr.makeappsgreat.onlinemall.user.AccountRepository;
import kr.makeappsgreat.onlinemall.user.member.Member;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class CartControllerTest {

    private static ObjectMapper objectMapper;

    @Autowired private MockMvc mockMvc;
    @Autowired private ProductRepository productRepository;
    @Autowired private ModelMapper modelMapper;

    private List<Product> products;

    @BeforeAll
    static void beforeAll() {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.addMixIn(Product.class, ProductMixIn.class);
    }

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
        // Given
        CartRequest cartRequest = getCartRequest();

        // When & Then
        mockMvc.perform(post("/cart/add")
                        .header("Content-Type", "application/json")
                        .content(objectMapper.writeValueAsString(cartRequest))
                        .with(csrf().asHeader()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.result").value(true));
    }

    @Nested
    class preinserted {

        @Autowired private AccountRepository<Member> memberRepository;
        @Autowired private CartService cartService;

        @BeforeEach
        void beforeEach() {
            Member member = memberRepository.findByUsername("user").get();
            Cart cart = modelMapper.map(getCartRequest(), Cart.class);
            cart.setMember(member);

            cartService.addToCart(cart);
        }

        /**
         * Test rendering of thymeleaf.
         */
        @Test
        @WithUserDetails
        void index() throws Exception {
            mockMvc.perform(get("/cart"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

    private CartRequest getCartRequest() {
        Map<String, Object> item = new HashMap<>();
        Map<String, Object> option = new HashMap<>();

        option.put("productId", products.get(1).getId());
        option.put("quantity", 1);

        item.put("productId", products.get(0).getId());
        item.put("quantity", 2);
        item.put("options", List.of(option));


        return modelMapper.map(item, CartRequest.class);
    }

    @JsonIgnoreProperties("options")
    abstract class ProductMixIn { }
}