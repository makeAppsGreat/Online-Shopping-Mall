package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.common.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;

@Controller
@RequiredArgsConstructor
@RequestMapping("/product")
public class ProductController {

    /**
     * @TODO : detail(detail of product and shopping mall notice(shared details)
     * @TODO : list(manufacturer, category and sorting)
     * @TODO : list -> Print no product if selecting manufacturer or category that not saved product and no search result found.
     * @TODO : search(with product name, in manufacturer or category)
     */

    private final ProductService productService;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProduct(id));
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
                            // Case : Type Mismatched.
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

        Page<Product> result = productService.getPagedProducts(productPageRequest);
        if (productPageRequest.getManufacturer() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null) {
                model.addAttribute("manufacturer", product.getManufacturer().getName());
            } else {
                model.addAttribute(
                        "manufacturer",
                        manufacturerRepository.findById(productPageRequest.getManufacturer())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
                                .getName()
                );
            }
        }

        if (productPageRequest.getCategory() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null) {
                model.addAttribute("category", product.getCategory().getName());
            } else {
                model.addAttribute(
                        "category",
                        categoryRepository.findById(productPageRequest.getCategory())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
                                .getName()
                );
            }
        }

        model.addAttribute("manufacturers", manufacturerRepository.findAll(Sort.by("name")));
        model.addAttribute("categories", categoryRepository.findAll(Sort.by("name")));
        model.addAttribute("products", result);
        model.addAttribute(
                "pagination",
                new Pagination(ServletUriComponentsBuilder.fromCurrentRequest(), result));


        return "/product/list";
    }
}
