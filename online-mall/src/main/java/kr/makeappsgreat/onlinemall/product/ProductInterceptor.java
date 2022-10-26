package kr.makeappsgreat.onlinemall.product;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ProductInterceptor implements HandlerInterceptor {

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) {
        if (modelAndView.getModel().get("productPageRequest") == null)
            modelAndView.getModel().put("productPageRequest", new ProductPageRequest());
    }
}
