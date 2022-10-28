package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.common.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
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
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.net.URI;

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

    private final MessageSource messageSource;

    @ModelAttribute
    public void navigation(Model model) {
        model.addAttribute(
                "manufacturers",
                manufacturerRepository.findAll(Sort.by("name")));
        model.addAttribute(
                "categories",
                categoryRepository.findAll(Sort.by("name")));
    }

    @GetMapping("/detail/{id}")
    public String detail(@PathVariable Long id, Model model) {
        model.addAttribute("product", productService.getProduct(id));
        return "/product/detail";
    }

    @GetMapping("/list")
    public ModelAndView list(@ModelAttribute @Valid ProductPageRequest productPageRequest, BindingResult bindingResult,
                             HttpServletRequest request, RedirectAttributes attributes) {
        ModelAndView modelAndView = new ModelAndView();
        String viewName = "/product/list";

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

                            URI uri = URI.create(referer);
                            if (!uri.getPath().equals(viewName)) {
                                attributes.addFlashAttribute(productPageRequest);
                                attributes.addFlashAttribute(
                                        "org.springframework.validation.BindingResult.productPageRequest",
                                        bindingResult);

                                modelAndView.setViewName("redirect:" + referer);
                                return modelAndView;
                            }
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
        modelAndView.setViewName(viewName);

        Page<Product> result = productService.getPagedProducts(productPageRequest);
        if (productPageRequest.getManufacturer() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null) {
                modelAndView.addObject("manufacturer", product.getManufacturer());
            } else {
                modelAndView.addObject(
                        "manufacturer",
                        manufacturerRepository.findById(productPageRequest.getManufacturer())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
                );
            }
        }

        if (productPageRequest.getCategory() != null) {
            Product product = result.stream().findFirst().orElse(null);

            if (product != null) {
                modelAndView.addObject("category", product.getCategory());
            } else {
                modelAndView.addObject(
                        "category",
                        categoryRepository.findById(productPageRequest.getCategory())
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST))
                );
            }
        }

        modelAndView.addObject("products", result);
        modelAndView.addObject(
                "pagination",
                new Pagination(ServletUriComponentsBuilder.fromCurrentRequest(), result));
        modelAndView.addObject(
                "sort_method",
                productSortMethod.get(
                        ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam("page")));


        return modelAndView;
    }
}
