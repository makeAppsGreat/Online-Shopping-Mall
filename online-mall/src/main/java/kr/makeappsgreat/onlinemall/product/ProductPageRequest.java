package kr.makeappsgreat.onlinemall.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Sort;

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
    @Min(0)
    private int sortMethod = this.DEFAULT_SORT_METHOD_VALUE;
    @Min(1)
    private int page = this.DEFAULT_PAGE_VALUE;


    public static ProductPageRequest of(String keyword) {
        ProductPageRequest request = new ProductPageRequest();
        request.setKeyword(keyword);

        return request;
    }

    public static ProductPageRequest empty() {
        return ProductPageRequest.EMPTY;
    }

    public int getSort_method() { return this.sortMethod; }
    public void setSort_method(int sortMethod) {
        this.sortMethod = sortMethod;
    }

    /**
     * Sort by product name(A-Z), price(high-low, low-high), new product, best seller7
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

    public boolean isKeywordOnly() {
        if (keyword == null) throw new RuntimeException("Unexpected usage.");
        return manufacturer == null && category == null;
    }
}
