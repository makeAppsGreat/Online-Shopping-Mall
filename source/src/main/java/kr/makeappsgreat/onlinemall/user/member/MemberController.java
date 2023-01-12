package kr.makeappsgreat.onlinemall.user.member;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller @RequestMapping("/member")
@RequiredArgsConstructor
public class MemberController {

    @GetMapping("/")
    public String root() {
        return "redirect:/member/main";
    }

    @GetMapping("/main")
    public String main() {
        return "/member/main";
    }
}
