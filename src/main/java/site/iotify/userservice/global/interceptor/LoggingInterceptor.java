package site.iotify.userservice.global.interceptor;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class LoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        System.out.println("===== HTTP Request =====");
        System.out.println("URI         : " + request.getRequestURI());
        System.out.println("Method      : " + request.getMethod());
        System.out.println("Headers     : " + request.getHeaderNames());
        System.out.println("Request body: " + request.getAttributeNames());
        System.out.println("========================");
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        System.out.println("===== HTTP Response =====");
        System.out.println("Status code  : " + response.getStatus());
        System.out.println("Headers      : " + response.getHeaderNames());

        // 응답 바디 읽기
        System.out.println("Response body: " + response.toString());
        System.out.println("=========================");
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }
}
