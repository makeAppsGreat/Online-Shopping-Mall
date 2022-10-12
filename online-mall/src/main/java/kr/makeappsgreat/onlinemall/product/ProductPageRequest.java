package kr.makeappsgreat.onlinemall.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

import javax.validation.constraints.Min;

@Getter @Setter
public class ProductPageRequest {

    public static int DEFAULT_SORT_METHOD_VALUE = 0;
    public static int DEFAULT_PAGE_VALUE = 1;

    private String keyword;
    private String manufacturer;
    private String category;
    @Min(0)
    private int sortMethod = this.DEFAULT_SORT_METHOD_VALUE;
    @Min(1)
    private int page = this.DEFAULT_PAGE_VALUE;

    public void setSort_method(int sortMethod) {
        this.sortMethod = sortMethod;
    }

    /**
     * Sort by product name(A-Z), price(high-low, low-high), new product, best seller
     */
    public Sort getSort() {
        switch (this.sortMethod) {
            case 0: default:
                return Sort.by("name").ascending();
            case 1:
                return Sort.by("price").ascending();
            case 2:
                return Sort.by("price").descending();
            case 3:
                return Sort.by("registeredDate").descending();
        }
    }
}
