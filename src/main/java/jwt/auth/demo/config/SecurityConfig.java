package jwt.auth.demo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;
import jwt.auth.demo.filter.JwtFilter;
import jwt.auth.demo.util.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final JwtUtil jwtUtil;
  private static final String[] API_WHITELIST = {"/auth/signup", "/auth/login", "/auth/refresh"};
  private static final String[] SWAGGER_WHITELIST = {"/swagger-ui/**", "/v3/api-docs/**"};
  private static final String[] H2_WHITELIST = {"/h2-console/**"};

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    return http.csrf(AbstractHttpConfigurer::disable)
        .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable))
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(
            auth ->
                auth.requestMatchers(whitelist(API_WHITELIST, SWAGGER_WHITELIST, H2_WHITELIST))
                    .permitAll()
                    .anyRequest()
                    .authenticated())
        .exceptionHandling(
            ex ->
                ex.authenticationEntryPoint(
                    (req, res, authEx) -> {
                      CommonResponse commonResponse =
                          new CommonResponse(ErrorCode.UNAUTHORIZED_TOKEN);
                      res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                      res.setContentType(MediaType.APPLICATION_JSON_VALUE);
                      res.setCharacterEncoding(StandardCharsets.UTF_8.name());
                      res.getWriter().write(new ObjectMapper().writeValueAsString(commonResponse));
                    }))
        .addFilterBefore(new JwtFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class)
        .build();
  }

  private String[] whitelist(String[]... arrays) {
    return Arrays.stream(arrays).flatMap(Arrays::stream).toArray(String[]::new);
  }
}
