package kr.makeappsgreat.onlinemall.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@RequiredArgsConstructor
public class ProductInterceptor implements HandlerInterceptor {

    private final CategoryRepository categoryRepository;
    private final ManufacturerRepository manufacturerRepository;

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
            throws Exception {
        // HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
        request.setAttribute("manufacturers", manufacturerRepository.findAll(Sort.by("name")));;
        request.setAttribute("categories", categoryRepository.findAll(Sort.by("name")));
    }
}
