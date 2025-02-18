package site.iotify.userservice.global.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import site.iotify.userservice.global.interceptor.LoggingInterceptor;

@Configuration
public class WebConfig implements WebMvcConfigurer {

//    @Bean
//    public LoggingInterceptor loggingInterceptor() {
//        return new LoggingInterceptor();
//    }
//
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(loggingInterceptor())
//                .addPathPatterns("/**");  // 모든 요청에 대해 인터셉터 적용
//    }
}
