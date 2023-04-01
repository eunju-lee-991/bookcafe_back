package cafe_in.cafe_in;

import cafe_in.cafe_in.filter.JwtAuthenticationFilter;
import cafe_in.cafe_in.repository.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {
    private final MemberRepository repository;

    @Bean
    public FilterRegistrationBean<JwtAuthenticationFilter> jwtAuthenticationFilterFilterRegistrationBean(
            HttpServletRequest request, HttpServletResponse response, JwtAuthenticationFilter jwtAuthenticationFilter) {
        FilterRegistrationBean<JwtAuthenticationFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(jwtAuthenticationFilter);
        bean.addUrlPatterns("/reviews/*", "/likes/*", "/logout"); //"/api/*",
        bean.setOrder(1);
       return bean;
    }

    @Bean
    public FilterRegistrationBean<CorsFilter> corsFilterRegistrationBean(HttpServletRequest request, HttpServletResponse response, CorsFilter corsFilter) {
        FilterRegistrationBean<CorsFilter> bean = new FilterRegistrationBean<>();
        bean.setFilter(corsFilter);
        bean.addUrlPatterns("/*");
        bean.setOrder(0);
       return bean;
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(MemberRepository repository) {
        return new JwtAuthenticationFilter(repository);
    }

    @Bean
    public CorsFilter corsFilter(){
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true); //내 서버가 응답할 때 JSON을 자바스크립트에서 처리할 수 있게 할지 설정
        configuration.addAllowedHeader("*"); // 모든 ip에 응답 허용
        configuration.addAllowedOriginPattern("http://localhost:5000");
        configuration.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", configuration); // 저 url 패턴으로 들어오는 요청은 configuration 설정

        return new CorsFilter(source);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("http://localhost:5000")
                .allowCredentials(true)
                .allowedMethods("OPTIONS", "GET", "POST", "PATCH", "DELETE")
                .allowedHeaders("*");
    }
}