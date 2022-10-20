package kr.makeappsgreat.onlinemall;

import kr.makeappsgreat.onlinemall.common.Pagination;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InitApplication implements ApplicationRunner {

    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Pagination.init(environment.getProperty("common.pagination_size", Integer.class));
    }
}
