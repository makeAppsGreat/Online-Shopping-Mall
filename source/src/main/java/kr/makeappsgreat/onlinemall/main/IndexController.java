package kr.makeappsgreat.onlinemall.main;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class IndexController {

    @GetMapping("/")
    public String index() {
        return "/index";
    }

    @GetMapping("{*path}")
    public String result(@PathVariable String path) {
        String[] splitPath = path.split("/");

        switch (splitPath[1]) {
            case "member":
                switch (splitPath[2]) {
                    case "password":
                    case "profile":
                        switch (splitPath[3]) {
                            case "success":
                                break;
                            default: throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                        } break;
                    default: throw new ResponseStatusException(HttpStatus.NOT_FOUND);
                } break;
            default: throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        return "/result";
    }
}
