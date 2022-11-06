package kr.makeappsgreat.onlinemall.main;

import kr.makeappsgreat.onlinemall.product.CategoryRepository;
import kr.makeappsgreat.onlinemall.product.ManufacturerRepository;
import kr.makeappsgreat.onlinemall.product.ProductPageRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class GlobalInterceptor implements HandlerInterceptor {

    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (response.getStatus() == HttpStatus.OK.value()) {
            Map<String, Object> model = modelAndView.getModel();

            /* Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            Object principal = authentication.getPrincipal();
            Account account = principal.equals("anonymousUser") ? null : ((AccountUserDetails) principal).getAccount();

            model.put("currentUser", account); */
            model.put("manufacturers", manufacturerRepository.findAll(Sort.by("name")));
            model.put("categories", categoryRepository.findAll(Sort.by("name")));

            if (model.get("productPageRequest") == null) model.put("productPageRequest", ProductPageRequest.empty());
        }
    }
}
