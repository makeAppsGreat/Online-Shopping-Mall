package kr.makeappsgreat.onlinemall.config;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;

@Configuration
public class ApplicationConfig {

    @Bean
    public ModelMapper modelMapper() {
        return baseModelMapper();
    }

    @Bean
    public ModelMapper deepModelMapper() {
        // 'deepModelMapper()' has same address as 'modelMapper()' if using 'modelMapper()'
        ModelMapper modelMapper = baseModelMapper();
        modelMapper.getConfiguration().setDeepCopyEnabled(true);

        return modelMapper;
    }

    private ModelMapper baseModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true)
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE)
                .setSkipNullEnabled(true);

        return modelMapper;
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
