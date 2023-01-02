package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.common.SimpleResult;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.util.Locale;

@Controller
@RequestMapping("/account")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService<Account> accountService;
    private final MessageSource messageSource;

    @GetMapping({"/usable-username/", "/usable-username/{username}"})
    public ResponseEntity<SimpleResult> isUsableUsername(@ModelAttribute @Validated Username username, BindingResult bindingResult,
                                                         HttpServletRequest request, Locale locale) {
        SimpleResult result;

        if (bindingResult.hasErrors()) {
            if (!request.isUserInRole(AccountRole.ROLE_ADMIN.name())) {
                FieldError fieldError = bindingResult.getFieldError();
                return ResponseEntity.ok(
                        SimpleResult.builder()
                                .request(fieldError.getRejectedValue().toString())
                                .result(false)
                                .code(HttpStatus.BAD_REQUEST.value())
                                .name(fieldError.getField())
                                .message(fieldError.getDefaultMessage())
                                .build());
            }
        }

        if (accountService.isDuplicatedUser(username.toString())) {
            result = SimpleResult.builder()
                    .request(username.toString())
                    .result(false)
                    .code(HttpStatus.CONFLICT.value())
                    .name("username")
                    .message(messageSource.getMessage("account.username.duplicated", null, locale))
                    .build();
        } else {
            result = SimpleResult.builder()
                    .request(username.toString())
                    .result(true)
                    .code(HttpStatus.OK.value())
                    .name("username")
                    .message(null)
                    .build();
        }

        return ResponseEntity.ok(result);
    }

    @Setter
    protected class Username {
        @NotEmpty
        @Email
        private String username;

        @Override
        public String toString() {
            return username;
        }
    }
}
