package kr.makeappsgreat.onlinemall.common.constraints;

import kr.makeappsgreat.onlinemall.user.AccountRequest;
import org.junit.jupiter.api.*;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class VerifyPasswordValidatorTest {

    private static ValidatorFactory validatorFactory;
    private static Validator validator;

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
    @Order(1)
    void shouldReturnViolation() {
        // Given
        AccountRequest request = new AccountRequest();

        // When
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSizeGreaterThan(0);
    }

    @Test
    @Order(2)
    void success() {
        // Given
        AccountRequest request = createRequest();
        request.setPasswordVerify(password);

        // When
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).isEmpty();
    }

    @Test
    @Order(3)
    void fail() {
        // Given
        AccountRequest request = createRequest();
        request.setPasswordVerify("complicated");

        // When
        Set<ConstraintViolation<AccountRequest>> violations = validator.validate(request);

        // Then
        assertThat(violations).hasSize(1);
        ConstraintViolation<AccountRequest> violation = violations.iterator().next();
        assertThat(violation.getPropertyPath().toString()).isEqualTo("passwordVerify");
        assertThat(violation.getMessage()).isEqualTo("{message.password-verify-not-match}");
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