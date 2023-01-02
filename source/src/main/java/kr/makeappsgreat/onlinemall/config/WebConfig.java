package kr.makeappsgreat.onlinemall.config;

import kr.makeappsgreat.onlinemall.common.Pagination;
import kr.makeappsgreat.onlinemall.main.GlobalInterceptor;
import lombok.RequiredArgsConstructor;
import nz.net.ultraq.thymeleaf.layoutdialect.LayoutDialect;
import nz.net.ultraq.thymeleaf.layoutdialect.decorators.strategies.GroupingStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    @Value("${common.pagination_size}")
    private int PAGINATION_SIZE;

    @Autowired
    private final GlobalInterceptor globalInterceptor;

    @Bean
    public ApplicationRunner initApplication() {
        return args -> {
            Pagination.init(PAGINATION_SIZE);
        };
    }

    /**
     * Config of Thymeleaf Layout Dialect
     * [<head> element merging]\
     * (https://ultraq.github.io/thymeleaf-layout-dialect/processors/decorate/#head-element-merging)
     */
    @Bean
    public LayoutDialect layoutDialect() {
        return new LayoutDialect(new GroupingStrategy());
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(globalInterceptor);
    }
}
