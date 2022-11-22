package kr.makeappsgreat.onlinemall.user.member;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Locale;

@Controller @RequestMapping("/member/join")
@SessionAttributes("agreement")
@RequiredArgsConstructor
public class MemberJoinController {

    private final ModelMapper modelMapper;
    private final MessageSource messageSource;

    @InitBinder("memberRequest")
    public void initBinder(WebDataBinder binder) {
        binder.setDisallowedFields("username");
    }

    @GetMapping("/")
    public String join() {
        return "redirect:/member/join/step1";
    }

    @GetMapping("/step1")
    public String step1(@ModelAttribute AgreementRequest agreementRequest) {
        return "/member/join/step1";
    }

    @PostMapping("/step1")
    public String step1Submit(@ModelAttribute @Validated AgreementRequest agreementRequest, BindingResult bindingResult,
                              Model model) {
        if (bindingResult.hasErrors()) return "/member/join/step1";

        Agreement agreement = modelMapper.map(agreementRequest, Agreement.class);
        model.addAttribute("agreement", agreement);


        System.out.println(">> 100 " + agreement.getAcceptanceDate());
        return "redirect:/member/join/step2";
    }

    @GetMapping("/step2")
    public String step2(@ModelAttribute MemberRequest memberRequest, Model model) { // , @SessionAttribute Agreement agreement
        /* if (agreement == null) {
            // Case : Not a proper request.
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        } */

        // System.out.println(">> 200 " + agreement.getAcceptanceDate());
        model.addAttribute("agreement", new Agreement());
        return "/member/join/step2";
    }

    @PostMapping("/step2")
    public String step2Submit(@ModelAttribute @Validated MemberRequest memberRequest, BindingResult bindingResult,
                              @SessionAttribute Agreement agreement,
                              RedirectAttributes attributes, SessionStatus status, Locale locale) {

        System.out.println(">> 300 Acceptance Date  " + agreement.getAcceptanceDate());
        System.out.println(">> 300 User Name        " + memberRequest.getUsername());
        System.out.println(">> 300 Password         " + memberRequest.getPassword());
        System.out.println(">> 300 Password Confirm " + memberRequest.getPasswordConfirm());
        System.out.println(">> 300 Email            " + memberRequest.getEmail());
        System.out.println(">> 300 Address.Zipcode  " + memberRequest.getAddress().getZipcode());


        if (!memberRequest.verify()) {
            bindingResult.addError(new FieldError(
                    "memberRequest", "passwordConfirm",
                    messageSource.getMessage("account.rules.password-confirm", null, locale)));
        }
        if (bindingResult.hasErrors()) return "/member/join/step2";

        // Member member = modelMapper.map(memberRequest, Member.class);
        attributes.addFlashAttribute("member", memberRequest);
        status.setComplete();
        return "redirect:/member/join/welcome";
    }

    @GetMapping("/welcome")
    public String welcome(@ModelAttribute MemberRequest member) {
        if (member == null) throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        return "/member/join/welcome";
    }
}
