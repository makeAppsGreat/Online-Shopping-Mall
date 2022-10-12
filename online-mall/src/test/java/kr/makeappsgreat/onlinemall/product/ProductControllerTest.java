package kr.makeappsgreat.onlinemall.product;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.hamcrest.Matchers.hasProperty;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductControllerTest {

    @Autowired
    // ProductRepository productRepository;
    ProductRepositoryForTest productRepository;

    @Autowired
    MockMvc mockMvc;

    @Nested
    class Detail {

        @Test
        @DisplayName("정상요청")
        public void detail() throws Exception {
            /** @TODO : Do save test data first, if run the application with NOT in-memory DB(Test DB). */
            // Given
            Optional<Product> one = productRepository.findTopByOrderById();

            // When & Then
            mockMvc.perform(get("/product/detail/{id}",
                            one.orElseThrow().getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("product"));
        }

        @Test
        @DisplayName("Product Id 없이 요청")
        public void detail_withNoProductId_404() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("잘못된 Product Id 요청(Type mismatch)")
        public void detail_typeMismatchedProductId_404() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/{id}", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isNotFound()); // @TODO : Do handle with ExceptionHandler
        }

        @Test
        @DisplayName("잘못된 Product Id 요청(Not exist)")
        public void detail_notExistedProductId_404() throws Exception {
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
        public void list_properPage_200() throws Exception {
            // Given
            int page = 1;

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", String.valueOf(page)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("productPageRequest", hasProperty("page", is(page))));
        }

        @Test
        @DisplayName("잘못된 Page 요청(Type mismatch)")
        public void list_typeMismatchedPage_200WithDefaultPage() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("page", is(ProductPageRequest.DEFAULT_PAGE_VALUE))
                    ));
        }

        @Test
        @DisplayName("잘못된 Page 요청(Not exist)")
        public void list_notExistedPage_200WithDefaultPage() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", "-1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("page", is(ProductPageRequest.DEFAULT_PAGE_VALUE))
                    ));

            mockMvc.perform(get("/product/list")
                            .param("page", String.valueOf(1_000_000_000)))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("정상요청 with Sort Method")
        public void list_properSortMethod() throws Exception {
            // Given
            int sort_method = 2;

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sort_method", String.valueOf(sort_method)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("productPageRequest", hasProperty("sortMethod", is(sort_method))));
        }

        @Test
        @DisplayName("잘못된 Sort Method 요청(Type mismatch)")
        public void list_typeMismatchedSortMethod_200WithDefaultSortMethod() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sort_method", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sortMethod", is(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE))
                    ));
        }

        @Test
        @DisplayName("잘못된 Sort Method 요청(Not exist)")
        public void list_notExistedSortMethod_200WithDefaultSortMethod() throws Exception {
            ProductPageRequest productPageRequest = new ProductPageRequest();

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sort_method", "-1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sort", is(productPageRequest.getSort()))
                    ));

            mockMvc.perform(get("/product/list")
                            .param("sort_methodr", String.valueOf(1_000_000_000)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sort", is(productPageRequest.getSort()))
                    ));
        }
    }

}