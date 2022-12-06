package kr.makeappsgreat.onlinemall.common.constraints;

import kr.makeappsgreat.onlinemall.user.AccountRequest;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class VerifyPasswordValidator implements ConstraintValidator<VerifyPassword, AccountRequest> {

    @Override
    public void initialize(VerifyPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(AccountRequest request, ConstraintValidatorContext context) {
        String password = request.getPassword();
        String passwordVerify = request.getPasswordVerify();

        if (password != null) return password.equals(passwordVerify);
        else return passwordVerify == null;
    }
}
