package lev.filippov.demomvc.config;

import lev.filippov.demomvc.models.User;
import lev.filippov.demomvc.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        String referer = request.getHeader("referer");
        User user = userService.findByUsername(authentication.getName());
        request.getSession().setAttribute("user", user);
        if(!request.getHeader("referer").contains("login") && !request.getHeader("referer").contains("registration")) {
            response.sendRedirect(referer);
        } else {
            response.sendRedirect(request.getContextPath() + "/shop");
        }
    }
}
