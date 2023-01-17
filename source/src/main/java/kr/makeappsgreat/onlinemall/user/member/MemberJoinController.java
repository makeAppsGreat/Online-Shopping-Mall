package kr.makeappsgreat.onlinemall.user.member;

import kr.makeappsgreat.onlinemall.common.Link;
import kr.makeappsgreat.onlinemall.common.ResultAttribute;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

@Controller @RequestMapping("/member/join")
@SessionAttributes("agreement")
@RequiredArgsConstructor
public class MemberJoinController {

    private static Map<Locale, ResultAttribute> welcomeResult = new HashMap<>();

    private final MemberService memberService;
    private final MessageSource messageSource;
    private final ModelMapper modelMapper;

    @GetMapping("/")
    public String join() {
        return "redirect:/member/join/step1";
    }

    @GetMapping("/step1")
    public String step1(@ModelAttribute AgreementRequest agreementRequest, SessionStatus status) {
        status.setComplete();
        return "/member/join/step1";
    }

    @PostMapping("/step1")
    public String step1Submit(@ModelAttribute @Validated AgreementRequest agreementRequest, BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) return "/member/join/step1";

        Agreement agreement = modelMapper.map(agreementRequest, Agreement.class);
        model.addAttribute("agreement", agreement);


        return "redirect:/member/join/step2";
    }

    @GetMapping("/step2")
    public String step2(@ModelAttribute MemberRequest memberRequest, @SessionAttribute Agreement agreement) {
        return "/member/join/step2";
    }

    @PostMapping("/step2")
    public String step2Submit(@ModelAttribute @Validated MemberRequest memberRequest, BindingResult bindingResult,
                              @SessionAttribute Agreement agreement,
                              RedirectAttributes redirectAttributes, Locale locale, SessionStatus status) {
        if (bindingResult.hasErrors()) return "/member/join/step2";
        status.setComplete();

        Member member = modelMapper.map(memberRequest, Member.class);
        member.setAgreement(agreement);
        Member savedMember = memberService.join(member);


        ResultAttribute result = welcomeResult.get(locale);
        if (result == null) {
            System.out.println(">> new locale(welcomeResult) " + locale);
            result = ResultAttribute.builder()
                    .title(messageSource.getMessage("account.join", null, locale))
                    .subTitle("Welcome!")
                    .message(String.format(
                            "<span style='font-size:x-large'>%s</span>%s",
                            savedMember.getName(),
                            messageSource.getMessage("account.join-welcome", null, locale)))
                    .build();

            result.addLinkToBreadcrumb(new Link(messageSource.getMessage("account.join", null, locale)));

            welcomeResult.put(locale, result);
        }
        redirectAttributes.addFlashAttribute("result", result);
        return "redirect:/member/join/welcome";
    }

    @GetMapping("/welcome")
    public String welcome() {
        return "/result";
    }
}
