package kr.makeappsgreat.onlinemall.user.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/member")
public class MemberController {

    private final MemberRepository memberRepository;
    private final AgreementRepository agreementRepository;

    @GetMapping("/join/")
    public String join() {
        return "redirect:/member/join/step1";
    }

    @GetMapping("/join/step1")
    public String step1(@ModelAttribute Agreement agreement) {
        System.out.println(">> 1 " + agreement.getAcceptanceDate());
        return "/member/join/step1";
    }

    @PostMapping("/join/step2")
    public String step2(@ModelAttribute Agreement agreement) {
        System.out.println(">> 2 " + agreement.getAcceptanceDate());
        System.out.println(agreement.getTerms1());
        System.out.println(agreement.getMarketing());
        return "/member/join/step2";
    }

}
