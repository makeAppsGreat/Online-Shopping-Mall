package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.common.Link;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@Component
@RequiredArgsConstructor
public class ProductSortMethod {

    private static final Map<Locale, List<Link>> cache = new HashMap<>();
    private final MessageSource messageSource;

    public List<Link> get(UriComponentsBuilder currentRequest, Locale locale) {
        List<Link> list = cache.get(locale);
        if (list == null) {
            list = new ArrayList<>();

            for (SortMethod sortMethod : SortMethod.values()) {
                list.add(new Link(messageSource.getMessage(sortMethod.getCode(), null, locale)));
            }
        }

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setLink(currentRequest.replaceQueryParam("sortMethod", i).build());
        }


        return list;
    }

    @RequiredArgsConstructor
    @Getter
    public enum SortMethod {

        NAME(
                "product.sort-method.nameAtoZ",
                Sort.by("name").ascending()),
        PRICE_LOW_HIGH(
                "product.sort-method.priceLowToHigh",
                Sort.by("price").ascending()),
        PRICE_HIGH_LOW(
                "product.sort-method.priceHighToLow",
                Sort.by("price").descending()),
        NEW_PRODUCT(
                "product.sort-method.newProduct",
                Sort.by("registeredDate").descending());

        public static final Map<Integer, Sort> SORT = new HashMap<>();
        private final String code;
        private final Sort sort;

        static {
            for (SortMethod sortMethod : SortMethod.values()) {
                SORT.put(sortMethod.ordinal(), sortMethod.sort);
            }
        }
    }
}
