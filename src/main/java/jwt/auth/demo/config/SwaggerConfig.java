package jwt.auth.demo.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

  private static final String JWT = "JWT";
  private static final String COOKIE = "COOKIE";

  @Bean
  public OpenAPI openAPI() {
    return new OpenAPI()
        .info(info())
        .addSecurityItem(securityRequirement())
        .components(securityComponents());
  }

  private Info info() {
    return new Info().title("jwt demo").version("0.0.1").description("JWT 토큰 연습");
  }

  private SecurityRequirement securityRequirement() {
    return new SecurityRequirement().addList(JWT).addList(COOKIE);
  }

  private Components securityComponents() {
    return new Components()
        .addSecuritySchemes(JWT, jwtScheme())
        .addSecuritySchemes(COOKIE, cookieScheme());
  }

  private SecurityScheme jwtScheme() {
    return new SecurityScheme()
        .name(JWT)
        .type(SecurityScheme.Type.HTTP)
        .scheme("bearer")
        .bearerFormat("JWT");
  }

  private SecurityScheme cookieScheme() {
    return new SecurityScheme()
        .name(COOKIE)
        .type(SecurityScheme.Type.APIKEY)
        .in(SecurityScheme.In.COOKIE);
  }
}
