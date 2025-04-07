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

  private static final String JWT = "jwtAuth";

  @Bean
  public OpenAPI openAPI() {
    Info info = new Info().title("jwt demo").version("0.0.1").description("JWT 토큰 연습");
    SecurityRequirement securityRequirement = new SecurityRequirement().addList(JWT);
    Components components =
        new Components()
            .addSecuritySchemes(
                "jwtAuth",
                new SecurityScheme()
                    .name("jwtAuth")
                    .type(SecurityScheme.Type.HTTP)
                    .scheme("bearer")
                    .bearerFormat("JWT"));

    return new OpenAPI().info(info).addSecurityItem(securityRequirement).components(components);
  }
}
