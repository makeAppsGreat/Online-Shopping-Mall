package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.common.Link;
import kr.makeappsgreat.onlinemall.common.ResultAttribute;
import kr.makeappsgreat.onlinemall.common.constraints.EditProfileGroup;
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
import java.util.Set;

@Controller @RequestMapping("/member")
@RequiredArgsConstructor
@Slf4j
public class MemberController {

    private static final Map<Locale, ResultAttribute> passwordResult = new HashMap<>();
    private static final Map<Locale, ResultAttribute> profileResult = new HashMap<>();
    private static final Set<String> disallowedFields = Set.of("name", "username", "password", "email");
    private static final FieldError oldPasswordNotMatch = new FieldError(
            "passwordRequest", "oldPassword", null, false, new String[]{"account.password-not-match"}, null, "Password not match"
    );

    private final MemberService memberService;
    private final MessageSource messageSource;
    private final ModelMapper deepModelMapper;

    @InitBinder("memberRequest")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields(disallowedFields.toArray(String[]::new));
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/member/my-info";
    }

    @GetMapping("/my-info")
    public String myInfo() {
        return "/member/my-info";
    }

    @GetMapping("/password")
    public String changePassword(@ModelAttribute PasswordRequest passwordRequest) {
        return "/member/password";
    }

    @PostMapping("/password")
    public String changePasswordSubmit(@ModelAttribute @Validated PasswordRequest passwordRequest, BindingResult bindingResult,
                                       Authentication authentication,
                                       RedirectAttributes attributes, Locale locale) {
        if (bindingResult.hasErrors()) return "/member/password";

        try {
            Account account = ((AccountUserDetails) authentication.getPrincipal()).getAccount();
            memberService.changePassword((Member) account, passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
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
        attributes.addFlashAttribute("result", resultAttribute);
        return "redirect:/member/password/success";
    }

    @GetMapping("/profile")
    public String updateProfile(Authentication authentication, Model model) {
        Account account = ((AccountUserDetails) authentication.getPrincipal()).getAccount();
        MemberRequest memberRequest = deepModelMapper.map(account, MemberRequest.class);
        model.addAttribute("memberRequest", memberRequest);

        return "/member/profile";
    }

    @PostMapping("/profile")
    public String updateProfileSubmit(@ModelAttribute @Validated(EditProfileGroup.class) MemberRequest memberRequest, BindingResult bindingResult,
                                      Authentication authentication,
                                      RedirectAttributes attributes, Locale locale) {
        AccountUserDetails userDetails = (AccountUserDetails) authentication.getPrincipal();
        Account account = userDetails.getAccount();

        if (bindingResult.hasErrors()) {
            deepModelMapper.map(account, memberRequest);
            return "/member/profile";
        }

        userDetails.updateAccount(memberService.updateProfile(account.getId(), memberRequest));


        ResultAttribute resultAttribute = profileResult.get(locale);
        if (resultAttribute == null) {
            resultAttribute = ResultAttribute.builder()
                    .title(messageSource.getMessage("account.profile", null, locale))
                    .subTitle(messageSource.getMessage("account.profile", null, locale))
                    .message(messageSource.getMessage("account.profile-success", null, locale))
                    .build();

            resultAttribute.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.my-info", null, locale)));
            resultAttribute.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.profile", null, locale)));
            resultAttribute.getBreadcrumb().get(0).setLink("/member/my-info");

            profileResult.put(locale, resultAttribute);
        }
        attributes.addFlashAttribute("result", resultAttribute);
        return "redirect:/member/profile/success";
    }

    @GetMapping({"/profile/success", "/password/success"})
    public String success(@ModelAttribute("result") ResultAttribute resultAttribute) {
        return IndexController.result(resultAttribute, "/member/");
    }
}
