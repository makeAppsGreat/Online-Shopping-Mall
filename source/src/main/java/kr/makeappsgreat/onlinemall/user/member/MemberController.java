package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.common.Link;
import kr.makeappsgreat.onlinemall.common.ResultAttribute;
import kr.makeappsgreat.onlinemall.main.IndexController;
import kr.makeappsgreat.onlinemall.user.Account;
import kr.makeappsgreat.onlinemall.user.AccountUserDetails;
import kr.makeappsgreat.onlinemall.user.PasswordRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller @RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private static Map<Locale, ResultAttribute> passwordResult = new HashMap<>();
    private static Map<Locale, ResultAttribute> profileResult = new HashMap<>();
    private static FieldError oldPasswordNotMatch = new FieldError(
            "passwordRequest", "oldPassword", null, false, new String[]{"account.password-not-match"}, null, "Password not match"
    );

    private final MemberService memberService;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;

    @InitBinder("memberRequest")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("name");
        binder.setDisallowedFields("username");
        binder.setDisallowedFields("password");
        binder.setDisallowedFields("email");
    }

    @ModelAttribute
    public void addAttributes(Model model, Authentication authentication) {
        model.addAttribute("account",((AccountUserDetails) authentication.getPrincipal()).getAccount());
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/member/my-info";
    }

    @GetMapping("/my-info")
    public String main() {
        return "/member/my-info";
    }

    @GetMapping("/password")
    public String changePassword(@ModelAttribute PasswordRequest passwordRequest) {
        return "/member/password";
    }

    @PostMapping("/password")
    public String changePasswordSubmit(@ModelAttribute @Validated PasswordRequest passwordRequest, BindingResult bindingResult,
                                       @ModelAttribute Account account,
                                       RedirectAttributes redirectAttributes, Locale locale) {
        if (bindingResult.hasErrors()) return "/member/password";

        try {
            memberService.changePassword(account, passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
        } catch (BadCredentialsException e) {
            bindingResult.addError(oldPasswordNotMatch);
            return "/member/password";
        }

        ResultAttribute resultAttribute = passwordResult.get(locale);
        if (resultAttribute == null) {
            resultAttribute = ResultAttribute.builder()
                    .title(messageSource.getMessage("account.change-password", null, locale))
                    .subTitle(messageSource.getMessage("account.change-password", null, locale))
                    .message(messageSource.getMessage("account.change-password-success", null, locale))
                    .build();

            resultAttribute.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.my-info", null, locale)));
            resultAttribute.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.change-password", null, locale)));
            resultAttribute.getBreadcrumb().get(0).setLink("/member/my-info");

            passwordResult.put(locale, resultAttribute);
        }
        redirectAttributes.addFlashAttribute("result", resultAttribute);
        return "redirect:/member/password/success";
    }

    @GetMapping("/profile")
    public String editProfile(@ModelAttribute MemberRequest memberRequest, @ModelAttribute AgreementRequest agreementRequest) {
        return "/member/profile";
    }

    @GetMapping({"/profile/success", "/password/success"})
    public String success(@ModelAttribute("result") ResultAttribute resultAttribute) {
        return IndexController.result(resultAttribute);
    }
}
