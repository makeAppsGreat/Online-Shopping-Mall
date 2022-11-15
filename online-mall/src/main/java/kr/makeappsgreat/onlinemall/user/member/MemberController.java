package kr.makeappsgreat.onlinemall.user.member;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller @RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    private final ModelMapper modelMapper;


    @GetMapping("/join/")
    public String join() {
        return "redirect:/member/join/step1";
    }

    @GetMapping("/join/step1")
    public String step1(@ModelAttribute("agreement") AgreementRequest agreementRequest) {
        return "/member/join/step1";
    }

    @PostMapping("/join/step1")
    public String step1Submit(@ModelAttribute("agreement") @Validated AgreementRequest agreementRequest, BindingResult bindingResult,
                              RedirectAttributes attributes) {
        if (bindingResult.hasErrors()) return "/member/join/step1";

        Agreement agreement = modelMapper.map(agreementRequest, Agreement.class);
        attributes.addFlashAttribute("agreement", agreement);


        System.out.println(">> 100 " + agreement.getAcceptanceDate());
        return "redirect:/member/join/step2";
    }

    @GetMapping("/join/step2")
    public String step2(@ModelAttribute("member") MemberRequest memberRequest,
                        Model model, RedirectAttributes attributes) {
        Agreement agreement = (Agreement) model.getAttribute("agreement");
        if (agreement == null) {
            // Case : Not a proper request.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        attributes.addFlashAttribute("agreement", agreement);

        System.out.println(">> 200 " + agreement.getAcceptanceDate());
        return "/member/join/step2";
    }
}
