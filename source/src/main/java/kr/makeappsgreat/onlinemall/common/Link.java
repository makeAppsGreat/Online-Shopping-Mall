package kr.makeappsgreat.onlinemall.common;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.util.MultiValueMap;
import org.springframework.web.util.UriComponents;

import java.util.stream.Collectors;

@RequiredArgsConstructor
@Getter
public class Link {

    private final String name;
    private String link;

    public void setLink(String link) {
        this.link = link;
    }

    public void setLink(UriComponents uriComponents) {
        MultiValueMap<String, String> queryParams = uriComponents.getQueryParams();

        if (!queryParams.isEmpty()) {
            this.link = "?" + queryParams.entrySet().stream()
                    .map(e -> e.getKey() + "=" + e.getValue().stream().findFirst().orElse(""))
                    .collect(Collectors.joining("&"));
        }
    }
}
