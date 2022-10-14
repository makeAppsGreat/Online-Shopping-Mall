package kr.makeappsgreat.onlinemall.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    /**
     * @TODO : detail(detail of product and shopping mall notice(shared details)
     * @TODO : list(manufacturer, category and sorting)
     * @TODO : list -> Print no product if selecting manufacturer or category that not saved product and no search result found.
     * @TODO : search(with product name, in manufacturer or category
     */

    private final ProductRepository productRepository;

    @Value("${product.list.page_request.size}")
    private int SIZE;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {

        Optional<Product> productOptional = productRepository.findById(id);
        model.addAttribute("product",
                productOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND)));

        return "/product/detail";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute @Valid ProductPageRequest productPageRequest, BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getAllErrors().forEach(e -> {
                if (e instanceof FieldError) {
                    FieldError fieldError = (FieldError) e;

                    switch (fieldError.getField()) {
                        case "keyword":
                            /** @TODO : Handle case -> Keyword is too short. */
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                        case "page":
                            productPageRequest.setPage(ProductPageRequest.DEFAULT_PAGE_VALUE);
                            break;
                        case "sort":
                            productPageRequest.setSortMethod(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE);
                            break;
                    }
                }
            });
        }

        PageRequest pageRequest = PageRequest.of(productPageRequest.getPage() - 1, SIZE, productPageRequest.getSort());
        Page<Product> result;

        if (productPageRequest.getKeyword() != null && !productPageRequest.getKeyword().isBlank())
            result = productRepository.findByNameContainingOrSimpleDetailContaining(
                    productPageRequest.getKeyword(),
                    productPageRequest.getKeyword(),
                    pageRequest);
        else if (productPageRequest.getManufacturer() >= 0 && productPageRequest.getCategory() >= 0) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setId(productPageRequest.getManufacturer());
            Category category = new Category();
            category.setId(productPageRequest.getCategory());
            result = productRepository.findByManufacturerAndCategory(manufacturer, category, pageRequest);
        } else if (productPageRequest.getManufacturer() >= 0) {
            Manufacturer manufacturer = new Manufacturer();
            manufacturer.setId(productPageRequest.getManufacturer());
            result = productRepository.findByManufacturer(manufacturer, pageRequest);
        } else if (productPageRequest.getCategory() >= 0) {
            Category category = new Category();
            category.setId(productPageRequest.getCategory());
            result = productRepository.findByCategory(category, pageRequest);
        } else result = productRepository.findAll(pageRequest);

        model.addAttribute("products", result);

        return "/product/list";
    }
}
