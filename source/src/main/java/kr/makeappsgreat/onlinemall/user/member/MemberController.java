package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.common.Link;
import kr.makeappsgreat.onlinemall.common.ResultAttribute;
import kr.makeappsgreat.onlinemall.common.constraints.EditProfileGroup;
import kr.makeappsgreat.onlinemall.main.IndexController;
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
    private static final Map<Locale, ResultAttribute> marketingResult = new HashMap<>();
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

    @ModelAttribute
    public void addAttributes(Authentication authentication, Model model) {
        model.addAttribute("user", ((AccountUserDetails) authentication.getPrincipal()).getAccount());
    }

    @GetMapping("/")
    public String root() {
        return "redirect:/member/my-info";
    }

    @GetMapping("/my-info")
    public String myInfo() {
        return "/member/my-info";
    }

    @GetMapping("/update/password")
    public String changePassword(@ModelAttribute PasswordRequest passwordRequest) {
        return "/member/update/password";
    }

    @PostMapping("/update/password")
    public String changePasswordSubmit(@ModelAttribute @Validated PasswordRequest passwordRequest, BindingResult bindingResult,
                                       @ModelAttribute("user") Member user,
                                       RedirectAttributes attributes, Locale locale) {
        if (bindingResult.hasErrors()) return "/member/update/password";

        try {
            Member member = memberService.retrieve(user);
            memberService.changePassword(member, passwordRequest.getOldPassword(), passwordRequest.getNewPassword());
        } catch (BadCredentialsException e) {
            bindingResult.addError(oldPasswordNotMatch);
            return "/member/update/password";
        }


        ResultAttribute resultAttribute = passwordResult.get(locale);
        if (resultAttribute == null) {
            String job = messageSource.getMessage("account.update.change-password", null, locale);
            resultAttribute = ResultAttribute.builder()
                    .title(job)
                    .subTitle(job)
                    .message(messageSource.getMessage("account.update.change-password-success", null, locale))
                    .build();

            resultAttribute.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.my-info", null, locale)));
            resultAttribute.addLinkToBreadcrumb(new Link(job));
            resultAttribute.getBreadcrumb().get(0).setLink("/member/my-info");

            passwordResult.put(locale, resultAttribute);
        }
        attributes.addFlashAttribute("result", resultAttribute);
        return "redirect:/member/update/password/success";
    }

    @GetMapping("/update/profile")
    public String updateProfile(@ModelAttribute("user") Member user, Model model) {
        Member member = memberService.retrieve(user);
        model.addAttribute("memberRequest", deepModelMapper.map(member, MemberRequest.class));

        return "/member/update/profile";
    }

    @PostMapping("/update/profile")
    public String updateProfileSubmit(@ModelAttribute @Validated(EditProfileGroup.class) MemberRequest memberRequest, BindingResult bindingResult,
                                      @ModelAttribute("user") Member user,
                                      RedirectAttributes attributes, Locale locale) {
        Member member = memberService.retrieve(user);

        if (bindingResult.hasErrors()) {
            deepModelMapper.map(member, memberRequest);
            return "/member/update/profile";
        }

        memberService.updateProfile(member, memberRequest);


        ResultAttribute resultAttribute = profileResult.get(locale);
        if (resultAttribute == null) {
            String job = messageSource.getMessage("account.update.profile", null, locale);
            resultAttribute = ResultAttribute.builder()
                    .title(job)
                    .subTitle(job)
                    .message(messageSource.getMessage("account.update.profile-success", null, locale))
                    .build();

            resultAttribute.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.my-info", null, locale)));
            resultAttribute.addLinkToBreadcrumb(new Link(job));
            resultAttribute.getBreadcrumb().get(0).setLink("/member/my-info");

            profileResult.put(locale, resultAttribute);
        }
        attributes.addFlashAttribute("result", resultAttribute);
        return "redirect:/member/update/profile/success";
    }

    @GetMapping("/update/marketing")
    public String updateMarketing(@ModelAttribute("user") Member user, Model model) {
        Agreement agreement = memberService.retrieve(user).getAgreement();

        model.addAttribute(
                "marketing",
                deepModelMapper.map(agreement.getMarketing(), MarketingRequest.class));
        model.addAttribute("updateDate", agreement.getUpdateDate());

        return "/member/update/marketing";
    }

    @PostMapping("/update/marketing")
    public String updateMarketingSubmit(@ModelAttribute("marketing") MarketingRequest marketingRequest,
                                        @ModelAttribute("user") Member user,
                                        RedirectAttributes attributes, Locale locale) {
        Member member = memberService.retrieve(user);
        memberService.updateMarketing(member, marketingRequest);


        ResultAttribute resultAttribute = marketingResult.get(locale);
        if (resultAttribute == null) {
            String job = messageSource.getMessage("account.update.marketing", null, locale);
            resultAttribute = ResultAttribute.builder()
                    .title(job)
                    .subTitle(job)
                    .message(messageSource.getMessage("account.update.marketing-success", null, locale))
                    .build();

            resultAttribute.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.my-info", null, locale)));
            resultAttribute.addLinkToBreadcrumb(new Link(job));
            resultAttribute.getBreadcrumb().get(0).setLink("/member/my-info");

            marketingResult.put(locale, resultAttribute);
        }
        attributes.addFlashAttribute("result", resultAttribute);
        return "redirect:/member/update/marketing/success";
    }

    @GetMapping({"/update/profile/success", "/update/password/success", "/update/marketing/success"})
    public String success(@ModelAttribute("result") ResultAttribute resultAttribute) {
        return IndexController.result(resultAttribute, "/member/");
    }
}
