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

    public List<Link> get(UriComponentsBuilder currentRequest) {
        List<Link> list = List.of(
                /** @TODO : Refactor to type-safe (Using Map<String, String>?) */
                new Link(messageSource.getMessage("nameAtoZ", null, Locale.getDefault())),
                new Link(messageSource.getMessage("priceLowToHigh", null, Locale.getDefault())),
                new Link(messageSource.getMessage("priceHighToLow", null, Locale.getDefault())),
                new Link(messageSource.getMessage("newProduct", null, Locale.getDefault()))
        );

        for (int i = 0; i < list.size(); i++)
            list.get(i).setLink(currentRequest.replaceQueryParam("sort_method", i).build());


        return list;
    }
}
