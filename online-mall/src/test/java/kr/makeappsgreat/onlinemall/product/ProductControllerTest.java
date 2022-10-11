package kr.makeappsgreat.onlinemall.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    ProductRepository productRepository;

    @Autowired
    MockMvc mockMvc;

    @Nested
    class Detail {

        @Test
        @DisplayName("정상요청")
        public void detail() throws Exception {
            /** @TODO : Do save test data first, if run the application with NOT in-memory DB(Test DB). */
            // Given
            List<Product> all = productRepository.findAll();

            // When & Then
            mockMvc.perform(get("/product/detail/{id}", all.get(0).getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("product"));
        }

        @Test
        @DisplayName("Product Id 없이 요청")
        public void detail_withNoProductId() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("잘못된 Product Id 요청")
        public void detail_withWrongProductId() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    class ListProduct {

        @Test
        @DisplayName("정상요청")
        public void list() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("정상요청 with Page")
        public void list_withPage() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", "1"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        /** @TODO : Check that page is default value(1), when wrong case. */
        @Test
        @DisplayName("정상요청 with 잘못된 Page(Type mismatch)")
        public void list_withWrongPage() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("정상요청 with 잘못된 Page(Not exist)")
        public void list_withWrongPage2() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", "-1"))
                    .andDo(print())
                    .andExpect(status().isOk());

            mockMvc.perform(get("/product/list")
                            .param("page", String.valueOf(Integer.MAX_VALUE)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }
    }

}