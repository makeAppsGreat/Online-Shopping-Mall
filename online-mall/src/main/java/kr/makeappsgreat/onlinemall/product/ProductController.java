package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.common.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
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
    private final ProductSortMethod productSortMethod;

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProduct(id));
        return "/product/detail";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute @Valid ProductPageRequest productPageRequest, BindingResult bindingResult,
                       Model model, HttpServletRequest request) {
        if (bindingResult.hasErrors()) {
            for (FieldError e : bindingResult.getFieldErrors()) {
                switch (e.getField()) {
                    case "keyword":
                        if (!productPageRequest.getKeyword().isBlank()) {
                            // Case : Keyword is too short.
                            // throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                            productPageRequest.setKeyword(null);

                            String referer = request.getHeader("Referer");
                            if (referer == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                            else return "redirect:" + UriComponentsBuilder.fromUriString(referer).build().getPath();
                        }
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
                    case "sortMethod":
                        productPageRequest.setSortMethod(ProductPageRequest.DEFAULT_SORT_METHOD_VALUE);
                        break;
                }
            }
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

        model.addAttribute("products", result);
        model.addAttribute(
                "pagination",
                new Pagination(ServletUriComponentsBuilder.fromCurrentRequest(), result));
        model.addAttribute(
                "sort_method",
                productSortMethod.get(ServletUriComponentsBuilder.fromCurrentRequest()));


        return "/product/list";
    }
}
