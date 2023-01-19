package kr.makeappsgreat.onlinemall.common;

import lombok.Builder;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Builder
@Getter
public class ResultAttribute {

    private final String title;
    @Builder.Default
    private final List<Link> breadcrumb = new ArrayList<>();
    private final String subTitle;
    private final String message;

    public void addLinkToBreadcrumb(Link link) {
        breadcrumb.add(link);
    }

    @Override
    public String toString() {
        return String.format("(ResultAttribute) '%s' : '%s'", title, message);
    }
}
