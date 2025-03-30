package jwt.auth.demo.dto.response;

import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;

public class LoginResponse extends CommonResponse {
  public LoginResponse(ErrorCode errorCode) {
    super(errorCode);
  }
}
