package kr.makeappsgreat.onlinemall.user;

import kr.makeappsgreat.onlinemall.common.SimpleResult;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.security.Principal;
import java.util.Locale;
import java.util.Set;

@Controller @RequestMapping("/account")
@Validated
@RequiredArgsConstructor
@Slf4j
public class AccountController {

    private final AccountService<Account> accountService;
    private final MessageSource messageSource;

    /** ID 사용 가능(중복 확인) 요청 시, 최초 요청 받는 Handler Method */
    @GetMapping({"/usable-username/", "/usable-username/{username}"})
    public String isUsableUsername(@PathVariable(required = false) @NotEmpty @Email String username,
                                   HttpServletRequest request) {
        return String.format("forward:%s/response", request.getRequestURI());
    }

    /** ID 사용 가능(중복 확인) 요청 시, 실제 응답하는 Handler Method */
    @GetMapping("/usable-username/{username}/response")
    @ResponseBody
    public ResponseEntity<SimpleResult> isUsableUsernameResponse(@PathVariable String username) {
        SimpleResult result;

        if (accountService.isDuplicatedUser(username)) {
            result = SimpleResult.builder()
                    .request(username)
                    .result(false)
                    .code(HttpStatus.CONFLICT.value())
                    .name("username")
                    .message(messageSource.getMessage("account.duplicated-username", null, Locale.getDefault()))
                    .build();
        } else {
            result = SimpleResult.builder()
                    .request(username)
                    .result(true)
                    .code(HttpStatus.OK.value())
                    .name("username")
                    .message(null)
                    .build();
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/error")
    @ResponseBody
    public ResponseEntity<SimpleResult> error(@RequestAttribute Exception exception,
                                              @RequestAttribute HttpServletRequest request) {
        SimpleResult result;

        /** Case : ID 사용 가능(중복 확인) 요청 시, 올바른 형식의 Email 주소가 아닌 경우 */
        if (exception != null && exception.getClass().isAssignableFrom(ConstraintViolationException.class)) {
            Set<ConstraintViolation<?>> constraintViolations =
                    ((ConstraintViolationException) exception).getConstraintViolations();
            ConstraintViolation[] constraintViolations2 =
                    constraintViolations.toArray(new ConstraintViolation[constraintViolations.size()]);
            String[] nodes = constraintViolations2[0].getPropertyPath().toString().split("\\.");

            result = SimpleResult.builder()
                    .request((String) constraintViolations2[0].getInvalidValue())
                    .result(false)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .name(nodes[nodes.length - 1])
                    .message(constraintViolations2[0].getMessage())
                    .build();
        } else {
            log.warn("Unexpected exception {request_uri : \"{}\"}", request.getRequestURI(), exception);
            result = SimpleResult.builder()
                    .result(false)
                    .code(HttpStatus.BAD_REQUEST.value())
                    .build();
        }

        return ResponseEntity.badRequest().body(result);
    }

    @ExceptionHandler
    public String handleIsUsableUsername(ConstraintViolationException exception,
                              Principal principal,
                              HttpServletRequest request, Model model) {
        if (principal != null &&
                ((AccountUserDetails) ((Authentication) principal).getPrincipal()).getAccount().getRoles().contains(AccountRole.ROLE_ADMIN))
            return String.format("forward:%s/response", request.getRequestURI());

        model.addAttribute("exception", exception);
        model.addAttribute("request", request);
        return "forward:/account/error";
    }
}
