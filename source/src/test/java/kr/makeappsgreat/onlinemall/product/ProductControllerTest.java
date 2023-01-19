package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.config.ApplicationConfig;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.MockBeans;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static org.hamcrest.Matchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = ProductController.class,
        excludeAutoConfiguration = SecurityAutoConfiguration.class)
@MockBeans({@MockBean(ManufacturerRepository.class), @MockBean(CategoryRepository.class)})
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductSortMethod productSortMethod;

    private ModelMapper modelMapper = new ApplicationConfig().modelMapper();

    // Given
    private Product product = createProduct();

    @Nested
    class Detail {

        @BeforeEach
        void mock() {
            given(productService.getProduct(product.getId())).willReturn(Optional.of(product));
        }

        @Test
        @DisplayName("Proper request [200]")
        public void detail() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/detail/{id}", product.getId()))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("product", product));
        }

        @Test
        @DisplayName("No Product Id [404]")
        public void detail_noProductId_404() throws Exception {
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
    class List {

        @BeforeEach
        void mock() {
            mockListAndKeyword();
        }

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
            int sortMethod = 2;

            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sortMethod", String.valueOf(sortMethod)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute("productPageRequest", hasProperty("sortMethod", is(sortMethod))));
        }

        @Test
        @DisplayName("Sort Method with blank [200, Same with default]")
        public void list_blankSortMethod_200() throws Exception {
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sortMethod", ""))
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
                            .param("sortMethod", "notANumber"))
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
            // When & Then
            mockMvc.perform(get("/product/list")
                            .param("sortMethod", "-1"))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sortMethod", is(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE))
                    ));

            mockMvc.perform(get("/product/list")
                            .param("sortMethod", String.valueOf(4_000_000_000L)))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attribute(
                            "productPageRequest",
                            hasProperty("sortMethod", is(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE))
                    ));
        }

        @Test
        @DisplayName("Manufacturer [200]")
        public void list_manufacturer_200() throws Exception {
            // Given
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

    @Nested
    class Keyword {

        @BeforeEach
        void mock() {
            mockListAndKeyword();
        }

        @Test
        @DisplayName("Keyword [200]")
        public void list_keyword_200() throws Exception {
            // Given
            String keyword = "샴푸";

            // When & Then
            mockMvc.perform(get("/product/search")
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
            mockMvc.perform(get("/product/search")
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
            mockMvc.perform(get("/product/search")
                            .param("keyword", keyword))
                    .andDo(print())
                    .andExpect(status().isOk())
                    .andExpect(model().attributeHasFieldErrors("productPageRequest", "keyword"));
        }
    }

    private Product createProduct() {
        Map manufacturer = new HashMap();
        manufacturer.put("id", 200L);
        manufacturer.put("name", "제조사");

        Map category = new HashMap();
        category.put("id", 300L);
        category.put("name", "분류");


        Map product = new HashMap();
        product.put("id", 100L);
        product.put("name", "제품명");
        product.put("price", 19_800);
        product.put("manufacturer", modelMapper.map(manufacturer, Manufacturer.class));
        product.put("category", modelMapper.map(category, Category.class));
        product.put("simpleDetail", "샴푸");

        return modelMapper.map(product, Product.class);
    }

    private void mockListAndKeyword() {
        given(productService.getPagedProducts(any(ProductPageRequest.class)))
                .willReturn(new PageImpl<>(java.util.List.of(product)));
        given(productService.getPagedProducts(argThat(argument -> {
            if (argument != null) {
                if (argument.getManufacturer() != null && argument.getManufacturer() < 0L) return true;
                if (argument.getCategory() != null && argument.getCategory() < 0L) return true;
            }

            return false;
        })))
                .willReturn(Page.empty());
        given(productService.getManufacturerById(product.getManufacturer().getId()))
                .willReturn(Optional.of(product.getManufacturer()));
        given(productService.getCategoryById(product.getCategory().getId()))
                .willReturn(Optional.of(product.getCategory()));
    }
}