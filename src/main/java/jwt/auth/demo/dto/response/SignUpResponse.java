package jwt.auth.demo.dto.response;

import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;

public class SignupResponse extends CommonResponse {
  public SignupResponse(ErrorCode errorCode) {
    super(errorCode);
  }
}
