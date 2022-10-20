package kr.makeappsgreat.onlinemall.common;

import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@Slf4j
public class Pagination {

    private static int PAGINATION_SIZE = 5;

    public static void init(int paginationSize) {
        log.info("Set PAGINATION_SIZE to \"{}\".", paginationSize);
        Pagination.PAGINATION_SIZE = paginationSize;
    }

    private final Integer first;
    private final Integer last;
    private final Integer next;
    private final Integer previous;
    private final List<Integer> page = new ArrayList<>();

    public Pagination(Page page) {
        int current = page.getNumber() + 1;

        first = (current == 1 ? null : 1);
        last = (current == page.getTotalPages() ? null : page.getTotalPages());
        next = (page.hasNext() ? page.nextPageable().getPageNumber() + 1: null);
        previous = (page.hasPrevious() ? page.previousPageable().getPageNumber() + 1 : null);

        for (int start = current / PAGINATION_SIZE * PAGINATION_SIZE + 1,
             i = 0; i < PAGINATION_SIZE; i++) {
            int now = start + i;

            this.page.add(now);
            if (now >= page.getTotalPages()) break;
        }

    }

}
