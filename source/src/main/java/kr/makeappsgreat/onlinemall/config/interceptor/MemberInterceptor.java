package kr.makeappsgreat.onlinemall.config.interceptor;

import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.web.WebAttributes;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
public class MemberInterceptor implements HandlerInterceptor {

    private final MessageSource messageSource;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String requestURI = request.getRequestURI();

        if (request.getUserPrincipal() != null) {
            if (requestURI.startsWith("/member/join")) {
                response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
                response.setHeader("Location", "/member/");

                return false;
            }
        } else {
            if (!requestURI.startsWith("/member/join")) {
                // org.springframework.security.web.authentication.ui.DefaultLoginPageGeneratingFilter
                request.getSession().setAttribute(
                        WebAttributes.AUTHENTICATION_EXCEPTION,
                        new AuthenticationCredentialsNotFoundException(
                                messageSource.getMessage(
                                        "account.authentication-credentials-not-found", null, LocaleContextHolder.getLocale()))
                );

                response.setStatus(HttpStatus.TEMPORARY_REDIRECT.value());
                response.setHeader("Location", "/login?error");

                return false;
            }
        }

        return true;
    }
}
