package iducs.springboot.weaverloft.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;

@Component
public class AdminInterceptor implements HandlerInterceptor {

    public List adminEssential
            = Arrays.asList("/members/list");

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        HttpSession session = request.getSession();
        if (session.getAttribute("isadmin") == null) {
            response.setContentType("text/html; charset=utf-8");
            PrintWriter out = response.getWriter();
            String element =
                    "<script> alert('관리자만 접근 가능 합니다.'); location.href='/'; </script>";
            out.println(element);
            out.flush();
            out.close();
        }
        return true;
    }
}
