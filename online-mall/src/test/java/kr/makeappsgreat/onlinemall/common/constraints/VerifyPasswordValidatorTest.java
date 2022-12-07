package kr.makeappsgreat.onlinemall.common.constraints;

import kr.makeappsgreat.onlinemall.user.AccountRequest;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class VerifyPasswordValidatorTest {

    static ValidatorFactory validatorFactory;
    static Validator validator;

    @BeforeAll
    static void createValidator() {
        validatorFactory = Validation.buildDefaultValidatorFactory();
        validator = validatorFactory.getValidator();
    }

    @AfterAll
    static void close() {
        validatorFactory.close();
    }

    @Test
    void shouldReturnViolation() {
        // Given
        AccountRequest request = new AccountRequest();

        // When
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);
        violations.forEach(System.out::println);

        // Then
        assertThat(violations).hasSizeGreaterThan(0);
    }

    @Test
    void success() {
        // Given
        AccountRequest request = createRequest();
        request.setPasswordVerify(password);

        // When
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);
        violations.forEach(System.out::println);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    void fail() {
        // Given
        AccountRequest request = createRequest();
        request.setPasswordVerify("complicated");

        // When
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);
        violations.forEach(System.out::println);

        // Then
        assertThat(violations).hasSize(1);
        assertThat(violations).allSatisfy(input -> {
                    assertThat(input.getMessageTemplate()).isEqualTo("{message.password-verify-not-match}");
                });
    }

    private String password = "simple";
    private AccountRequest createRequest() {
        AccountRequest request = new AccountRequest();
        request.setName("name");
        request.setUsername("username");
        request.setPassword(password);

        return request;
    }
}