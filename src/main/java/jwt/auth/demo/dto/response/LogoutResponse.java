package jwt.auth.demo.dto.response;

import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;

public class LogoutResponse extends CommonResponse {
  public LogoutResponse(ErrorCode errorCode) {
    super(errorCode);
  }
}
