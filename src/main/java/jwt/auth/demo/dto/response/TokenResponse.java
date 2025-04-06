package jwt.auth.demo.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class TokenResponse {
  private String accessToken;
  private String refreshToken;
}
