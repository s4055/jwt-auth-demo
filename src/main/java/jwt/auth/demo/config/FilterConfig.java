package jwt.auth.demo.config;

import jwt.auth.demo.filter.JwtFilter;
import jwt.auth.demo.filter.LoggingFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

  @Bean
  public FilterRegistrationBean<LoggingFilter> loggingFilter() {
    FilterRegistrationBean<LoggingFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new LoggingFilter());
    registrationBean.addUrlPatterns("/auth/*");
    registrationBean.setOrder(1);
    return registrationBean;
  }

  @Bean
  public FilterRegistrationBean<JwtFilter> jwtFilter() {
    FilterRegistrationBean<JwtFilter> registrationBean = new FilterRegistrationBean<>();
    registrationBean.setFilter(new JwtFilter());
    registrationBean.addUrlPatterns("/auth/*");
    registrationBean.setOrder(2);
    return registrationBean;
  }
}
