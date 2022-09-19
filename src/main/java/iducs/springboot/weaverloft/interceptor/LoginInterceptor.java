package iducs.springboot.weaverloft.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 적용할 경로 > 로그인 안되어 있으면 접속 x
    public List loginEssential
            = Arrays.asList("/boards/{bno}/**");

    // 적용 안할 경로 > 로그인 안해도 접속 가능
    public List loginInessential
            = Arrays.asList("/boards","/boards/{bno}");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("login") == null) {
            response.sendRedirect("/members/login");
        }
        return true;
    }

}
