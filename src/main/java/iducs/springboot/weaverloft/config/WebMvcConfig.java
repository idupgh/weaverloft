package iducs.springboot.weaverloft.config;

import iducs.springboot.weaverloft.interceptor.AdminInterceptor;
import iducs.springboot.weaverloft.interceptor.LoginInterceptor;
import org.springframework.boot.web.servlet.ServletContextInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.SessionCookieConfig;
import javax.servlet.SessionTrackingMode;
import java.util.Collections;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {

        AdminInterceptor adminInterceptor = new AdminInterceptor();
        registry.addInterceptor(adminInterceptor)
                .addPathPatterns(adminInterceptor.adminEssential);

        LoginInterceptor loginInterceptor = new LoginInterceptor();
        registry.addInterceptor(loginInterceptor)
                .addPathPatterns(loginInterceptor.loginEssential) // 인터셉터 적용될 패스
                .excludePathPatterns(loginInterceptor.loginInessential); // 제외될 패스
    }

    //j-session 삭제
    @Bean
    public ServletContextInitializer clearJsession() {
        return new ServletContextInitializer() {
            @Override
            public void onStartup(ServletContext servletContext) throws ServletException {
                servletContext.setSessionTrackingModes(Collections.singleton(SessionTrackingMode.COOKIE));
                SessionCookieConfig sessionCookieConfig=servletContext.getSessionCookieConfig();
                sessionCookieConfig.setHttpOnly(true);
            }
        };
    }
}
