package kr.makeappsgreat.onlinemall.main;

import kr.makeappsgreat.onlinemall.common.ResultAttribute;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class IndexController {

    public static String result(ResultAttribute resultAttribute) {
        if (resultAttribute == null || resultAttribute.getTitle() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ResultAttribute or resultAttribute.title is null.");
        }

        return "/result";
    }

    @GetMapping("/")
    public String index() {
        return "/index";
    }
}
