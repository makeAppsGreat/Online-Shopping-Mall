package kr.makeappsgreat.onlinemall;

import kr.makeappsgreat.onlinemall.common.Pagination;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
public class InitApplication implements ApplicationRunner {

    @Value("${common.pagination_size}")
    private int PAGINATION_SIZE;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Pagination.init(PAGINATION_SIZE);
    }
}
