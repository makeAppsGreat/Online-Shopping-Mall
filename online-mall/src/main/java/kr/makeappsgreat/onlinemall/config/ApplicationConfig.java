package kr.makeappsgreat.onlinemall.config;

import kr.makeappsgreat.onlinemall.common.Pagination;
import kr.makeappsgreat.onlinemall.user.Account;
import kr.makeappsgreat.onlinemall.user.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
@RequiredArgsConstructor
public class ApplicationConfig {

    private final AccountRepository<Account> accountRepository;

    @Value("${common.pagination_size}")
    private int PAGINATION_SIZE;

    @Bean
    public ApplicationRunner initApplication() {
        return args -> {
            Pagination.init(PAGINATION_SIZE);

            if (accountRepository.count() < 1L) {
                // @TODO : register master admin account
            }
        };
    }

    @Bean
    public LocalValidatorFactoryBean getValidator() {
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("messages/validationMessages");
        messageSource.setDefaultEncoding("UTF-8");

        LocalValidatorFactoryBean bean = new LocalValidatorFactoryBean();
        bean.setValidationMessageSource(messageSource);

        return bean;
    }
}
