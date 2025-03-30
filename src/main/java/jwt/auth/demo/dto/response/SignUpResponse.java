package jwt.auth.demo.dto.response;

import jwt.auth.demo.dto.common.CommonResponse;
import jwt.auth.demo.exception.ErrorCode;

public class SignUpResponse extends CommonResponse {
  public SignUpResponse(ErrorCode errorCode) {
    super(errorCode);
  }
}
