package kr.makeappsgreat.onlinemall.common.constraints;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

// @Documented
@Constraint(validatedBy = VerifyPasswordValidator.class)
@Target({ TYPE })
@Retention(RUNTIME)
public @interface VerifyPassword {

    String message() default "{message.password-verify-not-match}";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ TYPE })
    @Retention(RUNTIME)
    @interface List {
        VerifyPassword[] value();
    }
}
