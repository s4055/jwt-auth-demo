package jwt.auth.demo.dto.response;

import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;
import lombok.Getter;

@Getter
public class RefreshTokenResponse extends CommonResponse {

  private String newAccessToken;

  public RefreshTokenResponse(ErrorCode errorCode, String newAccessToken) {
    super(errorCode);
    this.newAccessToken = newAccessToken;
  }
}
