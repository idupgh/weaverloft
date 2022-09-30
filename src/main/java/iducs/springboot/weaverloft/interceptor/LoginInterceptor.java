package iducs.springboot.weaverloft.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    // 적용할 경로 > 로그인 안되어 있으면 접속 x
    public List loginEssential
            = Arrays.asList("/boards/regform","/boards/{bno}/**","/members/{id}/**");

    // 적용 안할 경로 > 로그인 안해도 접속 가능
    public List loginInessential
            = Arrays.asList("/boards","/boards/{bno}","/members/regform","/members/login","/members");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("login") == null) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('로그인이 필요한 페이지입니다.'); location.href='/members/login'; </script>";
            out.println(element);
            out.flush();//브라우저 출력 비우기
            out.close();//아웃객체 닫기
        }
        return true;
    }

}
