package kr.makeappsgreat.onlinemall.product;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

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
    private final MessageSource messageSource;

    @Value("${product.list.page_request.page_size}")
    private int PAGE_SIZE;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Errors errors, Model model) {
        if (errors.hasErrors()) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

        Product product = productRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("product", product);


        return "/product/detail";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute @Valid ProductPageRequest productPageRequest, BindingResult bindingResult,
                       Model model) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(e -> {
                switch (e.getField()) {
                    case "keyword":
                        if (!productPageRequest.getKeyword().isBlank())
                            // Case : Keyword is too short.
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                        break;
                    case "manufacturer":
                    case "category":
                        if (e.isBindingFailure() && !((String) e.getRejectedValue()).isBlank())
                            // Case : Type Mismatched
                            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                        break;
                    case "page":
                        productPageRequest.setPage(ProductPageRequest.DEFAULT_PAGE_VALUE);
                        break;
                    case "sort":
                        productPageRequest.setSortMethod(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE);
                        break;
                }
            });
        }


        Page<Product> result;
        PageRequest pageRequest = PageRequest.of(productPageRequest.getPage() - 1, PAGE_SIZE, productPageRequest.getSort());

        if (productPageRequest.getKeyword() != null && !productPageRequest.getKeyword().isBlank())
            result = productRepository.findByNameContainingOrSimpleDetailContaining(
                    productPageRequest.getKeyword(),
                    productPageRequest.getKeyword(),
                    pageRequest);
        else if (productPageRequest.getManufacturer() != null && productPageRequest.getCategory() != null)
            result = productRepository.findByManufacturerAndCategory(
                    Manufacturer.of(productPageRequest.getManufacturer()),
                    Category.of(productPageRequest.getCategory()),
                    pageRequest);
        else if (productPageRequest.getManufacturer() != null)
            result = productRepository.findByManufacturer(
                    Manufacturer.of(productPageRequest.getManufacturer()),
                    pageRequest);
        else if (productPageRequest.getCategory() != null)
            result = productRepository.findByCategory(
                    Category.of(productPageRequest.getCategory()),
                    pageRequest);
        else result = productRepository.findAll(pageRequest);


        model.addAttribute("manufacturers", manufacturerRepository.findAll(Sort.by("name")));
        model.addAttribute("categories", categoryRepository.findAll(Sort.by("name")));
        model.addAttribute("products", result);

        if (productPageRequest.getManufacturer() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null)
                model.addAttribute("manufacturer", product.getManufacturer().getName());
            else
                model.addAttribute(
                        "manufacturer",
                        manufacturerRepository.findById(productPageRequest.getManufacturer())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
                                .getName()
                );
        }

        if (productPageRequest.getCategory() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null)
                model.addAttribute("category", product.getCategory().getName());
            else
                model.addAttribute(
                        "category",
                        categoryRepository.findById(productPageRequest.getCategory())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
                                .getName()
                );
        }


        return "/product/list";
    }
}
