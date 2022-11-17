package kr.makeappsgreat.onlinemall.product;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
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
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class Detail {

        @Test
        @DisplayName("Proper request [200]")
        public void detail() throws Exception {
            /** @TODO : Do save test data first, if run the application with NOT in-memory DB(Test DB). */
            // Given
            Product product = productRepository.findAll(Pageable.ofSize(1)).stream().findFirst().get();

            // When & Then
            mockMvc.perform(get("/product/detail/{id}", product.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("product"));
        }

        @Test
        @DisplayName("No Product Id [404]")
        public void detail_withNoProductId_404() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }

        @Test
        @DisplayName("Wrong Product Id with type mismatched [400]")
        public void detail_typeMismatchedProductId_400() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/{id}", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Wrong Product Id with not exist [404]")
        public void detail_notExistedProductId_404() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/{id}", -1))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }

    @Nested
    @TestMethodOrder(MethodOrderer.DisplayName.class)
    class ListProduct {

        @Test
        @DisplayName("No condition [200]")
        public void list() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list"))
                    .andDo(print())
                    .andExpect(status().isOk());
        }

        @Test
        @DisplayName("Page [200]")
        public void list_properPage_200() throws Exception {
            // Given
            int page = 2;

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", String.valueOf(page)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("productPageRequest", hasProperty("page", is(page))));
        }

        @Test
        @DisplayName("Page with blank [200, Same as no conditional request]")
        public void list_blankPage_200() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", ""))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("page", is(ProductPageRequest.DEFAULT_PAGE_VALUE)))
                    );
        }

        @Test
        @DisplayName("Page with type mismatched [200, Same as default]")
        public void list_typeMismatchedPage_200WithDefaultPage() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("page", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("page", is(ProductPageRequest.DEFAULT_PAGE_VALUE)))
                    );
        }

        @Test
        @DisplayName("Page with not exist [200]")
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
        @DisplayName("Sort Method [200]")
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
        @DisplayName("Sort Method with blank [200, Same with default]")
        public void list_blankSortMethod_200() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sort_method", ""))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sortMethod", is(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE)))
                    );
        }

        @Test
        @DisplayName("Sort Method with type mismatch [200, Same with default]")
        public void list_typeMismatchedSortMethod_200WithDefaultSortMethod() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sort_method", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sortMethod", is(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE)))
                    );
        }

        @Test
        @DisplayName("Sort Method with mot exist [200, Same with default]")
        public void list_notExistedSortMethod_200WithDefaultSortMethod() throws Exception {
            ProductPageRequest defaultProductPageRequest = new ProductPageRequest();

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sort_method", "-1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sort", is(defaultProductPageRequest.getSort()))
                    ));

            mockMvc.perform(get("/product/list")
                            .param("sort_method", String.valueOf(1_000_000_000)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sort", is(defaultProductPageRequest.getSort()))
                    ));
        }

        @Test
        @DisplayName("Keyword [200]")
        public void list_keyword_200() throws Exception {
            // Given
            String keyword = "샴푸";

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("keyword", keyword))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "products",
                            hasProperty(
                                    "content",
                                    hasItem(anyOf(
                                            Matchers.<Product>hasProperty("name", containsString(keyword)),
                                            Matchers.<Product>hasProperty("simpleDetail", containsString(keyword))))
                                    )

                    ));
        }

        @Test
        @DisplayName("Keyword with blank [200, Same as no conditional request]")
        public void list_blankKeyword_200() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("keyword", ""))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attributeExists("products"));
        }

        @Test
        @DisplayName("Keyword with too short [200, with having errors at keyword]")
        public void list_shortKeyword_200() throws Exception {
            // Given
            String keyword = "마";

            // When & Then
            mockMvc.perform(get("/product/list")
                            .header("Referer", "/product/list")
                            .param("keyword", keyword))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attributeHasFieldErrors("productPageRequest", "keyword"));
        }

        @Test
        @DisplayName("Manufacturer [200]")
        public void list_manufacturer_200() throws Exception {
            // Given
            Product product = productRepository.findAll(Pageable.ofSize(1)).stream().findFirst().get();
            Manufacturer target = product.getManufacturer();

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("manufacturer", String.valueOf(target.getId())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "products",
                            hasProperty(
                                    "content",
                                    hasItem(Matchers.<Product>hasProperty(
                                            "manufacturer"
                                            , is(target))))
                    ));
        }

        @Test
        @DisplayName("Manufacturer with blank [200, Same as no conditional request]")
        public void list_blankManufacturer_200() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("manufacturer", ""))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "products",
                            hasProperty("content", hasSize(greaterThan(0))))
                    );
        }

        @Test
        @DisplayName("Manufacturer with type mismatched [400]")
        public void list_typeMismatchedManufacturer_400() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("manufacturer", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Manufacturer with not exist [404]")
        public void list_notExistManufacturer_404() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("manufacturer", "-1"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }


        @Test
        @DisplayName("Category [200]")
        public void list_category_200() throws Exception {
            // Given
            Product product = productRepository.findAll(Pageable.ofSize(1)).stream().findFirst().get();
            Category target = product.getCategory();

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("category", String.valueOf(target.getId())))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "products",
                            hasProperty(
                                    "content",
                                    hasItem(Matchers.<Product>hasProperty(
                                            "category"
                                            , is(target))))
                    ));
        }

        @Test
        @DisplayName("Category with blank [200, Same as no conditional request]")
        public void list_blankCategory_200() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("category", ""))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "products",
                            hasProperty("content", hasSize(greaterThan(0))))
                    );
        }

        @Test
        @DisplayName("Category with type mismatched [400]")
        public void list_typeMismatchedCategory_400() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("category", "notANumber"))
                    .andDo(print())
                    .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("Category with not exist [404]")
        public void list_notExistCategory_404() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("category", "-1"))
                    .andDo(print())
                    .andExpect(status().isNotFound());
        }
    }
}