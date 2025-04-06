package jwt.auth.demo.dto.response;

import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;
import lombok.Getter;

@Getter
public class LoginResponse extends CommonResponse {

  private String accessToken;

  public LoginResponse(ErrorCode errorCode, TokenResponse tokenResponse) {
    super(errorCode);
    this.accessToken = tokenResponse.getAccessToken();
  }
}
