package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.common.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Locale;

@Controller @RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    /**
     * @TODO detail(detail of product and shopping mall notice ( shared details)
     * @TODO list(manufacturer, category and sorting)
     * @TODO list -> Print no product if selecting manufacturer or category that not saved product and no search result found.
     * @TODO search(with product name, in manufacturer or category)
     */

    private final ProductService productService;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final ProductSortMethod productSortMethod;


    @GetMapping("/detail/{id}")
    @Transactional(readOnly = true)
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);

        model.addAttribute("product", product);
        if (!product.getOptions().isEmpty()) {
            model.addAttribute("options", product.getOptions());
        }

        return "/product/detail";
    }

    @GetMapping("/list")
    public String list(@ModelAttribute @Validated ProductPageRequest productPageRequest, BindingResult bindingResult,
                       Model model, Locale locale) {
        if (bindingResult.hasErrors()) productPageRequest.handleBindingResult(bindingResult);

        productPageRequest.setKeyword(null); // Ignore keyword in this handler method.
        Page<Product> result = productService.getPagedProducts(productPageRequest);
        ServletUriComponentsBuilder currentRequest = ServletUriComponentsBuilder.fromCurrentRequest();

        if (productPageRequest.getManufacturer() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null) {
                model.addAttribute("manufacturer", product.getManufacturer());
            } else {
                model.addAttribute(
                        "manufacturer",
                        manufacturerRepository.findById(productPageRequest.getManufacturer())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                );
            }
        }

        if (productPageRequest.getCategory() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null) {
                model.addAttribute("category", product.getCategory());
            } else {
                model.addAttribute(
                        "category",
                        categoryRepository.findById(productPageRequest.getCategory())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                );
            }
        }


        model.addAttribute("products", result);
        model.addAttribute("pagination", new Pagination(currentRequest, result));
        model.addAttribute("sortMethod", productSortMethod.get(currentRequest.replaceQueryParam("page"), locale));

        return "/product/list";
    }

    @GetMapping("/search")
    public String search(@ModelAttribute @Validated ProductPageRequest productPageRequest, BindingResult bindingResult,
                         Model model, Locale locale) {
        Page<Product> result;
        ServletUriComponentsBuilder currentRequest = ServletUriComponentsBuilder.fromCurrentRequest();

        if (bindingResult.hasErrors() && productPageRequest.handleBindingResult(bindingResult)) {
            result = Page.empty();
        } else {
            result = productService.getPagedProducts(productPageRequest);
        }


        model.addAttribute("products", result);
        model.addAttribute("pagination", new Pagination(currentRequest, result));
        model.addAttribute("sortMethod", productSortMethod.get(currentRequest.replaceQueryParam("page"), locale));

        return "/product/list";
    }
}
