package kr.makeappsgreat.onlinemall.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@Getter @Setter
public class ProductPageRequest {

    public static final int DEFAULT_SORT_METHOD_VALUE = 0;
    public static final int DEFAULT_PAGE_VALUE = 1;

    private static final ProductPageRequest EMPTY = new ProductPageRequest();

    @Size(min = 2)
    private String keyword;
    private Long manufacturer;
    private Long category;
    @Min(1)
    private int page = ProductPageRequest.DEFAULT_PAGE_VALUE;
    @Min(0)
    private int sortMethod = ProductPageRequest.DEFAULT_SORT_METHOD_VALUE;


    public static ProductPageRequest empty() {
        return ProductPageRequest.EMPTY;
    }

    /**
     * Sort by product name(A-Z), price(high-low, low-high), new product, best seller
     */
    public Sort getSort() {
        return ProductSortMethod.SortMethod.SORT.get(this.sortMethod);
    }

    /**
     * @param bindingResult to be handled
     */
    public void handleBindingResult(BindingResult bindingResult) {
        for (FieldError e : bindingResult.getFieldErrors()) {
            switch (e.getField()) {
                case "manufacturer":
                case "category":
                    if (e.isBindingFailure() && !((String) e.getRejectedValue()).isBlank())
                        // Case : Type mismatched.
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
                    break;
                case "page":
                    page = ProductPageRequest.DEFAULT_PAGE_VALUE;
                    break;
                case "sortMethod":
                    sortMethod = ProductPageRequest.DEFAULT_SORT_METHOD_VALUE;
                    break;
            }
        }
    }
}
