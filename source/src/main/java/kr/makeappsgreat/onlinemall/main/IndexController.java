package kr.makeappsgreat.onlinemall.main;

import kr.makeappsgreat.onlinemall.common.ResultAttribute;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class IndexController {

    /**
     * @param resultAttribute the ResultAttribute to pass on to the view
     * @param viewname the view name to redirect, if bad request
     * @return
     */
    public static String result(ResultAttribute resultAttribute, String viewname) {
        if (resultAttribute == null || resultAttribute.getTitle() == null) {
            if (viewname != null) return "redirect:" + viewname;
            else throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ResultAttribute or resultAttribute.title is null.");
        }

        return "/result";
    }
    public static String result(ResultAttribute resultAttribute) { return result(resultAttribute, null); }

    @GetMapping("/")
    public String index() {
        return "/index";
    }
}
