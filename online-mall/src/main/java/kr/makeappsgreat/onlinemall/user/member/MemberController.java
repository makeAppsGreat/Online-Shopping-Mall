package kr.makeappsgreat.onlinemall.user.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;
    private final AgreementRepository agreementRepository;

    @GetMapping("/join/")
    public String join() {
        return "redirect:/member/join/step1";
    }

    @GetMapping("/join/step1")
    public String step1(@ModelAttribute("agreement") AgreementRequest agreement) {
        return "/member/join/step1";
    }

    @PostMapping("/join/step2")
    public String step2(@ModelAttribute("agreement") @Validated AgreementRequest agreement, BindingResult bindingResult,
                        RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) {
            bindingResult.getFieldErrors().forEach(field -> {
                System.out.println(String.format(">> 2 [%s:%s] : %s", field.getField(), field.getRejectedValue(), field.getDefaultMessage()));
            });

            attributes.addFlashAttribute("agreement", agreement);
            attributes.addFlashAttribute(
                    "org.springframework.validation.BindingResult.agreement",
                    bindingResult);
            return "redirect:/member/join/step1";
        }

        return "/member/join/step2";
    }
}
