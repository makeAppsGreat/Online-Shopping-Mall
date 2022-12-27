package kr.makeappsgreat.onlinemall.user.member;

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

@Controller @RequestMapping("/member/join")
@SessionAttributes("agreement")
@RequiredArgsConstructor
public class MemberJoinController {
    /** @TODO Block access of authenticated user */

    private final MemberService memberService;

    private final ModelMapper modelMapper;
    private final MessageSource messageSource;


    // Unnecessary handling
    /* @InitBinder("memberRequest")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("username");
    } */

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
                              RedirectAttributes attributes, SessionStatus status) {
        if (bindingResult.hasErrors() && bindingResult.getFieldErrors().size() > 1) {
            // Field 'username' must have field error.
            return "/member/join/step2";
        }
        status.setComplete();

        Member member = modelMapper.map(memberRequest, Member.class);
        member.setAgreement(agreement);
        Member savedMember = memberService.join(member);


        attributes.addFlashAttribute("name", savedMember.getName());
        return "redirect:/member/join/welcome";
    }

    @GetMapping("/welcome")
    public String welcome(@ModelAttribute String name) {
        return "/member/join/welcome";
    }
}
