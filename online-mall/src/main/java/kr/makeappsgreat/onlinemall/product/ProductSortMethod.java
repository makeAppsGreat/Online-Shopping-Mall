package kr.makeappsgreat.onlinemall.product;

import kr.makeappsgreat.onlinemall.common.Link;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Locale;

@Component
@RequiredArgsConstructor
public class ProductSortMethod {

    private final MessageSource messageSource;

    public List<Link> get(UriComponentsBuilder currentRequest, Locale locale) {
        List<Link> list = List.of(
                /** @TODO Refactor to type-safe (Using Map<String, String>?) */
                new Link(messageSource.getMessage("product.sort-method.nameAtoZ", null, locale)),
                new Link(messageSource.getMessage("product.sort-method.priceLowToHigh", null, locale)),
                new Link(messageSource.getMessage("product.sort-method.priceHighToLow", null, locale)),
                new Link(messageSource.getMessage("product.sort-method.newProduct", null, locale))
        );

        for (int i = 0; i < list.size(); i++) {
            list.get(i).setLink(currentRequest.replaceQueryParam("sortMethod", i).build());
        }


        return list;
    }
}
