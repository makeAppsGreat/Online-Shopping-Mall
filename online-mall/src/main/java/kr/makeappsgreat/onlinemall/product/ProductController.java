package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.common.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

@Controller @RequestMapping("/product")
@RequiredArgsConstructor
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
    @Transactional(readOnly = true)
    public String detail(@PathVariable Long id, Model model) {
        Product product = productService.getProduct(id);

        model.addAttribute("product", product);
        if (!product.getOptions().isEmpty())
            model.addAttribute("options", product.getOptions());
        return "/product/detail";
    }

    @GetMapping("/list")
    public ModelAndView list(@ModelAttribute @Validated ProductPageRequest productPageRequest, BindingResult bindingResult,
                             @RequestHeader(value = "Referer", required = false) String referer, RedirectAttributes attributes,
                             Locale locale) {
        ModelAndView modelAndView = new ModelAndView();
        String viewName = "/product/list";
        String badKeyword = null;

        if (bindingResult.hasErrors()) {
            for (FieldError e : bindingResult.getFieldErrors()) {
                switch (e.getField()) {
                    case "keyword":
                        // Case : Keyword is too short or blank.
                        if (referer == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);

                        URI uri = URI.create(referer);
                        if (!uri.getPath().equals(viewName)) {
                            attributes.addFlashAttribute(productPageRequest);
                            attributes.addFlashAttribute(
                                    "org.springframework.validation.BindingResult.productPageRequest",
                                    bindingResult);

                            modelAndView.setViewName("redirect:" + referer);
                            return modelAndView;
                        } else {
                            badKeyword = productPageRequest.getKeyword();
                            productPageRequest.setKeyword(null);
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
        } else {
            if (productPageRequest.getKeyword() != null && !productPageRequest.isKeywordOnly()) {
                attributes.addFlashAttribute(
                        "productPageRequest",
                        ProductPageRequest.of(productPageRequest.getKeyword()));
                modelAndView.setViewName(
                        "redirect:/product/list?keyword=" +
                        URLEncoder.encode(productPageRequest.getKeyword(), StandardCharsets.UTF_8));

                return modelAndView;
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
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
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
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND))
                );
            }
        }

        modelAndView.addObject("products", result);
        modelAndView.addObject(
                "pagination",
                new Pagination(ServletUriComponentsBuilder.fromCurrentRequest(), result));
        modelAndView.addObject(
                "sortMethod",
                productSortMethod.get(
                        ServletUriComponentsBuilder.fromCurrentRequest().replaceQueryParam("page"), locale));

        if (badKeyword != null) productPageRequest.setKeyword(badKeyword);


        return modelAndView;
    }
}
