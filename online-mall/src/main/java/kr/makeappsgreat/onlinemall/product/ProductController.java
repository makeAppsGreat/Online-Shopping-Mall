package kr.makeappsgreat.onlinemall.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
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
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;

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
        model.addAttribute("manufacturers", manufacturerRepository.findAll(Sort.by("name")));
        model.addAttribute("categories", categoryRepository.findAll(Sort.by("name")));

        if (bindingResult.hasErrors()) {
            for (ObjectError e : bindingResult.getAllErrors()) {
                if (e instanceof FieldError) {
                    FieldError fieldError = (FieldError) e;

                    switch (fieldError.getField()) {
                        case "keyword":
                            if (!productPageRequest.getKeyword().isBlank())
                                return "/product/list"; // Case : Keyword is too short.
                            break;
                        case "manufacturer":
                        case "category":
                            if (fieldError.isBindingFailure() && !((String) fieldError.getRejectedValue()).isBlank())
                                return "/product/list";
                            break;
                        case "page":
                            productPageRequest.setPage(ProductPageRequest.DEFAULT_PAGE_VALUE);
                            break;
                        case "sort":
                            productPageRequest.setSortMethod(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE);
                            break;
                    }
                }
            }
        }


        Page<Product> result;
        PageRequest pageRequest = PageRequest.of(productPageRequest.getPage() - 1, SIZE, productPageRequest.getSort());

        if (productPageRequest.getKeyword() != null && !productPageRequest.getKeyword().isBlank())
            result = productRepository.findByNameContainingOrSimpleDetailContaining(
                    productPageRequest.getKeyword(),
                    productPageRequest.getKeyword(),
                    pageRequest);
        else if (productPageRequest.getManufacturer() != null && productPageRequest.getCategory() != null) {
            result = productRepository.findByManufacturerAndCategory(
                    Manufacturer.of(productPageRequest.getManufacturer()),
                    Category.of(productPageRequest.getCategory()),
                    pageRequest);

            Product product = result.stream().findFirst().orElse(null);
            if (product != null) {
                model.addAttribute("manufacturer", product.getManufacturer().getName());
                model.addAttribute("category", product.getCategory().getName());
            } else {
                /** @TODO : Handle case -> if MA or CA is not found. */
                model.addAttribute("manufacturer", manufacturerRepository.findById(productPageRequest.getManufacturer()));
                model.addAttribute("category", categoryRepository.findById(productPageRequest.getCategory()));
            }
        } else if (productPageRequest.getManufacturer() != null) {
            result = productRepository.findByManufacturer(
                    Manufacturer.of(productPageRequest.getManufacturer()),
                    pageRequest);

            Product product = result.stream().findFirst().orElse(null);
            if (product != null)
                model.addAttribute("manufacturer", product.getManufacturer());
            else
                model.addAttribute("manufacturer", manufacturerRepository.findById(productPageRequest.getManufacturer()));
        } else if (productPageRequest.getCategory() != null) {
            result = productRepository.findByCategory(
                    Category.of(productPageRequest.getCategory()),
                    pageRequest);

            Product product = result.stream().findFirst().orElse(null);
            if (product != null)
                model.addAttribute("category", product.getCategory());
            else
                model.addAttribute("category", categoryRepository.findById(productPageRequest.getCategory()));
        } else result = productRepository.findAll(pageRequest);
        model.addAttribute("products", result);


        return "/product/list";
    }
}
