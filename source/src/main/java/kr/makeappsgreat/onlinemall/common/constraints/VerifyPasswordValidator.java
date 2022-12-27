package kr.makeappsgreat.onlinemall.common.constraints;

import kr.makeappsgreat.onlinemall.user.AccountRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VerifyPasswordValidator implements ConstraintValidator<VerifyPassword, AccountRequest> {

    private String message;

    @Override
    public void initialize(VerifyPassword constraintAnnotation) {
        message = constraintAnnotation.message();
    }

    @Override
    public boolean isValid(AccountRequest request, ConstraintValidatorContext context) {
        String password = request.getPassword();
        String passwordVerify = request.getPasswordVerify();

        if ((password != null && !password.equals(passwordVerify)) || (password == null && passwordVerify != null)) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(message)
                    .addPropertyNode("passwordVerify")
                    .addConstraintViolation();

            return false;
        } else return true;
    }
}
