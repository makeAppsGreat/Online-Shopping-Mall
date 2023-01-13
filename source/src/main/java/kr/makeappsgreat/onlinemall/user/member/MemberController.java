package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.common.Link;
import kr.makeappsgreat.onlinemall.common.ResultAttribute;
import kr.makeappsgreat.onlinemall.user.Account;
import kr.makeappsgreat.onlinemall.user.AccountUserDetails;
import kr.makeappsgreat.onlinemall.user.PasswordRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller @RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private static Map<Locale, ResultAttribute> passwordResult = new HashMap<>();

    private final MemberService memberService;
    private final MessageSource messageSource;

    @GetMapping("/")
    public String root() {
        return "redirect:/member/my-info";
    }

    @GetMapping("/my-info")
    public String main() {
        return "/member/my-info";
    }

    @GetMapping("/profile")
    public String editProfile(Model model) {
        return "/member/profile";
    }

    @GetMapping("/password")
    public String changePassword(@ModelAttribute PasswordRequest passwordRequest, BindingResult bindingResult) {
        return "/member/password";
    }

    @PostMapping("/password")
    public String changePasswordSubmit(@ModelAttribute @Validated PasswordRequest passwordRequest, BindingResult bindingResult,
                                       RedirectAttributes redirectAttributes, Locale locale,
                                       Authentication authentication) {
        if (bindingResult.hasErrors()) return "/member/password";

        Account account = ((AccountUserDetails) authentication.getPrincipal()).getAccount();
        memberService.changePassword(account, passwordRequest.getOldPassword(), passwordRequest.getPassword());

        ResultAttribute result = passwordResult.get(locale);
        if (result == null) {
            result = ResultAttribute.builder()
                    .title(messageSource.getMessage("account.change-password", null, locale))
                    .subTitle(messageSource.getMessage("account.change-password", null, locale))
                    .message(messageSource.getMessage("account.change-password-success", null, locale))
                    .build();

            result.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.my-info", null, locale)));
            result.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.change-password", null, locale)));
            result.getBreadcrumb().get(0).setLink("/member/my-info");

            passwordResult.put(locale, result);
        }
        redirectAttributes.addFlashAttribute("result", result);


        return "redirect:/member/password/success";
    }

    /* @GetMapping("/{job}/{result}")
    public String done() {
        return "/result";
    } */
}
