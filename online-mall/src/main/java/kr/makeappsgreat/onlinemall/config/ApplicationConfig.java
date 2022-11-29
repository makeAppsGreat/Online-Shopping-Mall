package kr.makeappsgreat.onlinemall.config;

import kr.makeappsgreat.onlinemall.common.Pagination;
import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.stereotype.Component;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Component
public class ApplicationConfig implements ApplicationRunner {

    @Value("${common.pagination_size}")
    private int PAGINATION_SIZE;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Pagination.init(PAGINATION_SIZE);
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

    @Bean
    public ModelMapper modelMapper() {
        return ApplicationConfig.myModelMapper();
    }

    public static ModelMapper myModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(Configuration.AccessLevel.PRIVATE);

        return modelMapper;
    }
}
