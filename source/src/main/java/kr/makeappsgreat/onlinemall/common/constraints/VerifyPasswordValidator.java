package kr.makeappsgreat.onlinemall.common.constraints;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.Assert;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VerifyPasswordValidator implements ConstraintValidator<VerifyPassword, Object> {

    private String message;
    private String passwordPropertyName;
    private String passwordVerifyPropertyName;

    @Override
    public void initialize(VerifyPassword constraintAnnotation) {
        message = constraintAnnotation.message();
        passwordPropertyName = constraintAnnotation.passwordPropertyName();
        passwordVerifyPropertyName = constraintAnnotation.passwordVerifyPropertyName();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext context) {
        BeanWrapperImpl wrapper = new BeanWrapperImpl(value);
        Assert.isTrue(wrapper.isReadableProperty(passwordPropertyName),
                String.format("'%s' is not readable property. (password)", passwordPropertyName));
        Assert.isTrue(wrapper.isReadableProperty(passwordVerifyPropertyName),
                String.format("'%s' is not readable property. (passwordVerify)", passwordVerifyPropertyName));

        Object password = wrapper.getPropertyValue(passwordPropertyName);
        Object passwordVerify = wrapper.getPropertyValue(passwordVerifyPropertyName);

        if ((password != null && !password.equals(passwordVerify))
                || (password == null && passwordVerify != null)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode(passwordVerifyPropertyName)
                    .addConstraintViolation();

            return false;
        } else return true;
    }
}
