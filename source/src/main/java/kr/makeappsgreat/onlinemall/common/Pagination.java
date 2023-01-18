package kr.makeappsgreat.onlinemall.common;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Getter
@Slf4j
public class Pagination {

    private static int PAGINATION_SIZE = 5;

    public static void init(int paginationSize) {
        Pagination.PAGINATION_SIZE = paginationSize;
        log.info("Set PAGINATION_SIZE to {}.", paginationSize);
    }

    private Link first = new Link("|<");
    private Link previous = new Link("<");
    private Link next = new Link(">");
    private Link last = new Link(">|");
    private List<Link> page = new ArrayList<>();

    public Pagination(UriComponentsBuilder currentRequest, Page page) {
        int current = page.getNumber() + 1;


        if (current == 1) first.setLink("#none");
        else first.setLink(currentRequest.replaceQueryParam("page", 1).build());

        if (!page.hasPrevious()) previous.setLink("#none");
        else previous.setLink(currentRequest.replaceQueryParam("page", page.previousPageable().getPageNumber() + 1).build());

        if (!page.hasNext()) next.setLink("#none");
        else next.setLink(currentRequest.replaceQueryParam("page", page.nextPageable().getPageNumber() + 1).build());

        if (current >= page.getTotalPages()) last.setLink("#none");
        else last.setLink(currentRequest.replaceQueryParam("page", page.getTotalPages()).build());

        for (int start = page.getNumber() / PAGINATION_SIZE * PAGINATION_SIZE + 1, i = 0; i < PAGINATION_SIZE; i++) {
            int now = start + i;

            Link link = new Link(String.valueOf(now));
            if (now == current) link.setLink("#none");
            else link.setLink(currentRequest.replaceQueryParam("page", now).build());

            this.page.add(link);
            if (now >= page.getTotalPages()) break;
        }
    }

}
